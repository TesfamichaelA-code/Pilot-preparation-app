import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument, Role } from '../users/schemas/user.schema';
import { ExamResult, ExamResultDocument } from '../progress/schemas/exam-result.schema';
import { Exam, ExamDocument } from '../exams/schemas/exam.schema';
import { Question, QuestionDocument } from '../exams/schemas/question.schema';
import { 
  StudentProgressQueryDto, 
  UserStatisticsQueryDto,
  StudentProgressResponse,
  ExamStatisticsResponse,
  UserStatisticsResponse,
  OverallStatisticsResponse
} from './dto/admin.dto';

@Injectable()
export class AdminService {
  constructor(
    @InjectModel(User.name) private userModel: Model<UserDocument>,
    @InjectModel(ExamResult.name) private examResultModel: Model<ExamResultDocument>,
    @InjectModel(Exam.name) private examModel: Model<ExamDocument>,
    @InjectModel(Question.name) private questionModel: Model<QuestionDocument>,
  ) {}

  async getAllStudents() {
    return this.userModel
      .find({ roles: { $in: [Role.STUDENT] } })
      .select('_id name email createdAt')
      .sort({ createdAt: -1 })
      .exec();
  }

  async getStudentProgress(
    userId: string, 
    query: StudentProgressQueryDto
  ): Promise<StudentProgressResponse> {
    const user = await this.userModel.findById(userId).select('_id name email').exec();
    
    if (!user) {
      throw new NotFoundException(`User with ID ${userId} not found`);
    }

    // Get exam results
    const examResults = await this.examResultModel
      .find({ userId })
      .sort({ completedAt: -1 })
      .populate('examId', 'title difficulty category')
      .exec();

    // Calculate average score
    const totalScore = examResults.reduce((sum, result) => sum + result.score, 0);
    const averageScore = examResults.length > 0 ? totalScore / examResults.length : 0;

    // Calculate progress over time
    const progressOverTime = await this.calculateProgressOverTime(userId);

    // Get attempts by category
    const attemptsByCategory = await this.examResultModel.aggregate([
      { $match: { userId } },
      { $lookup: { from: 'exams', localField: 'examId', foreignField: '_id', as: 'exam' } },
      { $unwind: '$exam' },
      { $group: { 
          _id: '$exam.category', 
          count: { $sum: 1 },
          averageScore: { $avg: '$score' }
        }
      },
      { $project: { 
          category: '$_id', 
          count: 1, 
          averageScore: 1,
          _id: 0
        } 
      }
    ]);

    // Get attempts by difficulty
    const attemptsByDifficulty = await this.examResultModel.aggregate([
      { $match: { userId } },
      { $lookup: { from: 'exams', localField: 'examId', foreignField: '_id', as: 'exam' } },
      { $unwind: '$exam' },
      { $group: { 
          _id: '$exam.difficulty', 
          count: { $sum: 1 },
          averageScore: { $avg: '$score' }
        }
      },
      { $project: { 
          difficulty: '$_id', 
          count: 1, 
          averageScore: 1,
          _id: 0
        } 
      }
    ]);

    return {
      user: {
        id: user._id.toString(),
        name: user.name,
        email: user.email,
      },
      examResults: examResults.map(result => {
        // Using type assertion to avoid null/undefined checks
        const examIdValue = result.examId as any;
        let examId = '';
        let examTitle = 'Unknown Exam';
        
        if (examIdValue) {
          if (typeof examIdValue === 'object') {
            if (examIdValue._id) {
              examId = examIdValue._id.toString();
            }
            if (examIdValue.title) {
              examTitle = examIdValue.title;
            }
          } else {
            examId = examIdValue.toString();
          }
        }
        
        return {
          id: result._id.toString(),
          examId,
          examTitle,
          score: result.score,
          totalQuestions: result.totalQuestions,
          correctAnswers: result.correctAnswers,
          completedAt: result.completedAt,
        };
      }),
      summary: {
        totalExamsTaken: examResults.length,
        averageScore,
        progressOverTime,
        attemptsByCategory,
        attemptsByDifficulty,
      }
    };
  }

  async getUserStatistics(query: UserStatisticsQueryDto): Promise<UserStatisticsResponse> {
    // Total number of users
    const totalUsers = await this.userModel.countDocuments().exec();
    
    // Users by role
    const usersByRole = await this.userModel.aggregate([
      { $unwind: '$roles' },
      { $group: { _id: '$roles', count: { $sum: 1 } } },
      { $project: { role: '$_id', count: 1, _id: 0 } }
    ]);

    // New users in the last 30 days
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
    
    const newUsersLast30Days = await this.userModel
      .countDocuments({ createdAt: { $gte: thirtyDaysAgo } })
      .exec();

    // Active users (users who have taken exams in the last 30 days)
    const activeUsers = await this.examResultModel
      .distinct('userId', { completedAt: { $gte: thirtyDaysAgo } })
      .exec();

    // User engagement data (average exams taken per user)
    const userEngagement = await this.examResultModel.aggregate([
      { $group: { _id: '$userId', examCount: { $sum: 1 } } },
      { $group: { _id: null, avgExamsPerUser: { $avg: '$examCount' } } }
    ]);

    // User registration over time (monthly)
    const userRegistrationOverTime = await this.userModel.aggregate([
      { 
        $group: {
          _id: { 
            year: { $year: '$createdAt' },
            month: { $month: '$createdAt' } 
          },
          count: { $sum: 1 }
        }
      },
      { 
        $project: {
          _id: 0,
          date: { 
            $dateFromParts: { 
              year: '$_id.year', 
              month: '$_id.month',
              day: 1
            } 
          },
          count: 1
        }
      },
      { $sort: { date: 1 } }
    ]);

    return {
      totalUsers,
      usersByRole,
      newUsersLast30Days,
      activeUsers: activeUsers.length,
      userEngagement: userEngagement.length > 0 
        ? userEngagement[0].avgExamsPerUser
        : 0,
      userRegistrationOverTime
    };
  }

  async getExamStatistics(): Promise<ExamStatisticsResponse> {
    // Total exams
    const totalExams = await this.examModel.countDocuments().exec();
    
    // Total questions
    const totalQuestions = await this.questionModel.countDocuments().exec();
    
    // Exams by category
    const examsByCategory = await this.examModel.aggregate([
      { $group: { _id: '$category', count: { $sum: 1 } } },
      { $project: { category: '$_id', count: 1, _id: 0 } }
    ]);
    
    // Exams by difficulty
    const examsByDifficulty = await this.examModel.aggregate([
      { $group: { _id: '$difficulty', count: { $sum: 1 } } },
      { $project: { difficulty: '$_id', count: 1, _id: 0 } }
    ]);
    
    // Most attempted exams
    const mostAttemptedExams = await this.examResultModel.aggregate([
      { $group: { _id: '$examId', attemptCount: { $sum: 1 } } },
      { $sort: { attemptCount: -1 } },
      { $limit: 5 },
      { 
        $lookup: { 
          from: 'exams', 
          localField: '_id', 
          foreignField: '_id', 
          as: 'examDetails' 
        } 
      },
      { $unwind: '$examDetails' },
      { 
        $project: { 
          _id: 0,
          examId: '$_id',
          title: '$examDetails.title',
          attemptCount: 1
        } 
      }
    ]);
    
    // Hardest exams (lowest average score)
    const hardestExams = await this.examResultModel.aggregate([
      { $group: { 
          _id: '$examId', 
          averageScore: { $avg: '$score' },
          attemptCount: { $sum: 1 }
        } 
      },
      { $match: { attemptCount: { $gte: 5 } } }, // Only consider exams with at least 5 attempts
      { $sort: { averageScore: 1 } },
      { $limit: 5 },
      { 
        $lookup: { 
          from: 'exams', 
          localField: '_id', 
          foreignField: '_id', 
          as: 'examDetails' 
        } 
      },
      { $unwind: '$examDetails' },
      { 
        $project: { 
          _id: 0,
          examId: '$_id',
          title: '$examDetails.title',
          averageScore: 1,
          attemptCount: 1
        } 
      }
    ]);

    return {
      totalExams,
      totalQuestions,
      examsByCategory,
      examsByDifficulty,
      mostAttemptedExams,
      hardestExams
    };
  }

  async getOverallStatistics(): Promise<OverallStatisticsResponse> {
    // Total users
    const totalUsers = await this.userModel.countDocuments().exec();
    
    // Total exams
    const totalExams = await this.examModel.countDocuments().exec();
    
    // Total exam attempts
    const totalExamAttempts = await this.examResultModel.countDocuments().exec();
    
    // Average score across all exam attempts
    const averageScoreResult = await this.examResultModel.aggregate([
      { $group: { _id: null, averageScore: { $avg: '$score' } } }
    ]);
    
    const averageScore = averageScoreResult.length > 0 
      ? averageScoreResult[0].averageScore 
      : 0;
    
    // System activity over time (exams taken per month)
    const activityOverTime = await this.examResultModel.aggregate([
      { 
        $group: {
          _id: { 
            year: { $year: '$completedAt' },
            month: { $month: '$completedAt' } 
          },
          count: { $sum: 1 }
        }
      },
      { 
        $project: {
          _id: 0,
          date: { 
            $dateFromParts: { 
              year: '$_id.year', 
              month: '$_id.month',
              day: 1
            } 
          },
          count: 1
        }
      },
      { $sort: { date: 1 } }
    ]);

    return {
      totalUsers,
      totalExams,
      totalExamAttempts,
      averageScore,
      activityOverTime
    };
  }

  private async calculateProgressOverTime(userId: string) {
    // Calculate progress over time (monthly average scores)
    return this.examResultModel.aggregate([
      { $match: { userId } },
      { 
        $group: {
          _id: { 
            year: { $year: '$completedAt' },
            month: { $month: '$completedAt' } 
          },
          averageScore: { $avg: '$score' },
          count: { $sum: 1 }
        }
      },
      { 
        $project: {
          _id: 0,
          date: { 
            $dateFromParts: { 
              year: '$_id.year', 
              month: '$_id.month',
              day: 1
            } 
          },
          averageScore: 1,
          count: 1
        }
      },
      { $sort: { date: 1 } }
    ]);
  }
}