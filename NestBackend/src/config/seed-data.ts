import { Injectable, OnModuleInit } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument, Role } from '../users/schemas/user.schema';
import { Exam, ExamDocument, ExamCategory, Difficulty } from '../exams/schemas/exam.schema';
import { Question, QuestionDocument } from '../exams/schemas/question.schema';
import { ExamResult, ExamResultDocument } from '../progress/schemas/exam-result.schema';
import * as bcrypt from 'bcrypt';

@Injectable()
export class SeedService implements OnModuleInit {
  constructor(
    @InjectModel(User.name) private userModel: Model<UserDocument>,
    @InjectModel(Exam.name) private examModel: Model<ExamDocument>,
    @InjectModel(Question.name) private questionModel: Model<QuestionDocument>,
    @InjectModel(ExamResult.name) private examResultModel: Model<ExamResultDocument>,
  ) {}

  async onModuleInit() {
    const userCount = await this.userModel.countDocuments().exec();
    
    if (userCount === 0) {
      console.log('Seeding database with initial data...');
      await this.seedUsers();
      const exams = await this.seedExams();
      await this.seedQuestions(exams);
      await this.seedExamResults(exams);
      console.log('Database seeding completed.');
    }
  }

  private async seedUsers(): Promise<UserDocument[]> {
    console.log('Seeding users...');
    
    const adminPassword = await bcrypt.hash('admin123', 10);
    const studentPassword = await bcrypt.hash('student123', 10);
    
    const users = [
      {
        name: 'Admin User',
        email: 'admin@example.com',
        password: adminPassword,
        roles: [Role.ADMIN],
      },
      {
        name: 'Student One',
        email: 'student1@example.com',
        password: studentPassword,
        roles: [Role.STUDENT],
      },
      {
        name: 'Student Two',
        email: 'student2@example.com',
        password: studentPassword,
        roles: [Role.STUDENT],
      },
      {
        name: 'Student Three',
        email: 'student3@example.com',
        password: studentPassword,
        roles: [Role.STUDENT],
      },
    ];
    
    const createdUsers = await this.userModel.insertMany(users);
    console.log(`${createdUsers.length} users created`);
    return createdUsers;
  }

  private async seedExams(): Promise<ExamDocument[]> {
    console.log('Seeding exams...');
    
    const exams = [
      {
        title: 'Basic Aerodynamics',
        description: 'Test your knowledge of fundamental aerodynamics principles',
        category: ExamCategory.PILOT_TRAINEE,
        difficulty: Difficulty.EASY,
        durationMinutes: 30,
        isActive: true,
      },
      {
        title: 'Aircraft Systems',
        description: 'Comprehensive test on aircraft systems',
        category: ExamCategory.PILOT_TRAINEE,
        difficulty: Difficulty.MEDIUM,
        durationMinutes: 45,
        isActive: true,
      },
      {
        title: 'Navigation Principles',
        description: 'Test your understanding of aviation navigation',
        category: ExamCategory.PILOT_TRAINEE,
        difficulty: Difficulty.HARD,
        durationMinutes: 60,
        isActive: true,
      },
      {
        title: 'Flight Instruction Techniques',
        description: 'Assessment for flight instructors on teaching methodologies',
        category: ExamCategory.FLIGHT_INSTRUCTOR,
        difficulty: Difficulty.MEDIUM,
        durationMinutes: 45,
        isActive: true,
      },
    ];
    
    const createdExams = await this.examModel.insertMany(exams);
    console.log(`${createdExams.length} exams created`);
    return createdExams;
  }

  private async seedQuestions(exams: ExamDocument[]): Promise<void> {
    console.log('Seeding exam questions...');
    
    const questionsByExam = {
      'Basic Aerodynamics': [
        {
          text: 'What is the primary function of the ailerons?',
          options: ['Control roll', 'Control pitch', 'Control yaw', 'Reduce drag'],
          correctAnswer: 0,
          explanation: 'Ailerons control roll by creating differential lift on the wings.',
        },
        {
          text: 'Which of the following affects the lift force on a wing?',
          options: ['Angle of attack', 'Wing shape', 'Air density', 'All of the above'],
          correctAnswer: 3,
          explanation: 'Lift is affected by angle of attack, wing shape, air density, and airspeed.',
        },
        {
          text: 'What is the boundary layer in aerodynamics?',
          options: [
            'The layer of air between the atmosphere and space',
            'The thin layer of air close to the surface of a wing where air velocity changes from zero to free stream value',
            'The boundary between subsonic and supersonic flight',
            'The area where lift and drag forces are equal',
          ],
          correctAnswer: 1,
          explanation: 'The boundary layer is the thin layer of air close to the surface of a wing where air velocity changes from zero at the surface to the free stream value.',
        },
      ],
      'Aircraft Systems': [
        {
          text: 'What is the purpose of the pitot-static system?',
          options: [
            'To measure engine performance',
            'To measure aircraft altitude, airspeed, and vertical speed',
            'To control the aircraft\'s electrical system',
            'To regulate cabin pressure',
          ],
          correctAnswer: 1,
          explanation: 'The pitot-static system provides airspeed, altitude, and vertical speed information to the pilot.',
        },
        {
          text: 'What is the function of an aircraft\'s alternator?',
          options: [
            'To start the engine',
            'To generate electricity during flight',
            'To control the aircraft\'s hydraulic system',
            'To reduce engine vibration',
          ],
          correctAnswer: 1,
          explanation: 'The alternator generates electricity to power aircraft systems and charge the battery during flight.',
        },
      ],
      'Navigation Principles': [
        {
          text: 'What information does a VOR provide to pilots?',
          options: [
            'Distance to the station',
            'Bearing to or from the station',
            'Altitude above the station',
            'Weather at the station',
          ],
          correctAnswer: 1,
          explanation: 'VOR (VHF Omnidirectional Range) provides bearing information to or from the station.',
        },
        {
          text: 'What is the primary purpose of ADS-B?',
          options: [
            'To provide weather information',
            'To automatically broadcast aircraft position, altitude, and velocity',
            'To communicate with air traffic control',
            'To navigate using satellite signals',
          ],
          correctAnswer: 1,
          explanation: 'ADS-B (Automatic Dependent Surveillance-Broadcast) automatically broadcasts aircraft position, altitude, and velocity to ground stations and other aircraft.',
        },
      ],
      'Flight Instruction Techniques': [
        {
          text: 'Which teaching method involves the instructor demonstrating a skill before the student attempts it?',
          options: [
            'Guided discussion',
            'Demonstration-performance',
            'Computer-based training',
            'Lecture',
          ],
          correctAnswer: 1,
          explanation: 'Demonstration-performance involves the instructor demonstrating a skill before the student attempts to perform it.',
        },
        {
          text: 'What is the primary purpose of a preflight briefing?',
          options: [
            'To discuss the student\'s career goals',
            'To prepare the student for the upcoming lesson',
            'To review weather conditions only',
            'To complete administrative paperwork',
          ],
          correctAnswer: 1,
          explanation: 'A preflight briefing prepares the student for the upcoming lesson, including objectives, procedures, and safety considerations.',
        },
      ],
    };
    
    for (const exam of exams) {
      const questions = questionsByExam[exam.title];
      
      if (questions) {
        const questionsWithExamId = questions.map(q => ({
          ...q,
          examId: exam._id.toString(),
        }));
        
        await this.questionModel.insertMany(questionsWithExamId);
        console.log(`${questions.length} questions created for exam: ${exam.title}`);
      }
    }
  }

  private async seedExamResults(exams: ExamDocument[]): Promise<void> {
    console.log('Seeding exam results...');
    
    const students = await this.userModel.find({ roles: { $in: [Role.STUDENT] } }).exec();
    
    if (students.length === 0 || exams.length === 0) {
      console.log('No students or exams found. Skipping exam results seeding.');
      return;
    }
    
    const examResults = [];
    
    // For each student, create some exam results
    for (const student of students) {
      // Create results for each exam
      for (const exam of exams) {
        const questions = await this.questionModel.find({ examId: exam._id }).exec();
        
        if (questions.length === 0) {
          continue;
        }
        
        // Generate random answer details
        const answerDetails = questions.map(question => {
          const userAnswer = Math.floor(Math.random() * 4); // Random answer 0-3
          const isCorrect = userAnswer === question.correctAnswer;
          
          return {
            questionId: question._id.toString(),
            questionText: question.text,
            userAnswer,
            correctAnswer: question.correctAnswer,
            isCorrect,
          };
        });
        
        const correctAnswers = answerDetails.filter(a => a.isCorrect).length;
        const totalQuestions = questions.length;
        const score = (correctAnswers / totalQuestions) * 100;
        
        // Create result from 1-3 months ago
        const daysAgo = Math.floor(Math.random() * 90) + 1;
        const completedAt = new Date();
        completedAt.setDate(completedAt.getDate() - daysAgo);
        
        examResults.push({
          userId: student._id.toString(),
          examId: exam._id.toString(),
          score,
          totalQuestions,
          correctAnswers,
          answerDetails,
          completedAt,
        });
        
        // Add another attempt a few days later with improved score for some exams
        if (Math.random() > 0.6) {
          const improvedAnswerDetails = answerDetails.map(detail => {
            // 50% chance to correct a wrong answer
            if (!detail.isCorrect && Math.random() > 0.5) {
              return {
                ...detail,
                userAnswer: detail.correctAnswer,
                isCorrect: true,
              };
            }
            return detail;
          });
          
          const improvedCorrectAnswers = improvedAnswerDetails.filter(a => a.isCorrect).length;
          const improvedScore = (improvedCorrectAnswers / totalQuestions) * 100;
          
          // Create result from 0-30 days ago (more recent than the first attempt)
          const recentDaysAgo = Math.floor(Math.random() * 30);
          const recentCompletedAt = new Date();
          recentCompletedAt.setDate(recentCompletedAt.getDate() - recentDaysAgo);
          
          examResults.push({
            userId: student._id.toString(),
            examId: exam._id.toString(),
            score: improvedScore,
            totalQuestions,
            correctAnswers: improvedCorrectAnswers,
            answerDetails: improvedAnswerDetails,
            completedAt: recentCompletedAt,
          });
        }
      }
    }
    
    await this.examResultModel.insertMany(examResults);
    console.log(`${examResults.length} exam results created`);
  }
}