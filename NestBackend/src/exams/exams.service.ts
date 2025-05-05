import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Exam, ExamDocument } from './schemas/exam.schema';
import { Question, QuestionDocument } from './schemas/question.schema';
import { 
  CreateExamDto, 
  UpdateExamDto, 
  CreateQuestionDto, 
  UpdateQuestionDto,
  ExamQueryDto
} from './dto/exam.dto';

@Injectable()
export class ExamsService {
  constructor(
    @InjectModel(Exam.name) private examModel: Model<ExamDocument>,
    @InjectModel(Question.name) private questionModel: Model<QuestionDocument>,
  ) {}

  
  async createExam(createExamDto: CreateExamDto): Promise<ExamDocument> {
    const createdExam = new this.examModel(createExamDto);
    return createdExam.save();
  }

  async findAllExams(query: ExamQueryDto): Promise<ExamDocument[]> {
    const { category, difficulty, limit = 10, skip = 0 } = query;
    
    const filter: any = {};
    if (category) filter.category = category;
    if (difficulty) filter.difficulty = difficulty;
    
    return this.examModel
      .find(filter)
      .skip(skip)
      .limit(limit)
      .exec();
  }

  async findExamById(id: string): Promise<ExamDocument> {
    const exam = await this.examModel.findById(id).exec();
    if (!exam) {
      throw new NotFoundException(`Exam with ID ${id} not found`);
    }
    return exam;
  }

  async updateExam(id: string, updateExamDto: UpdateExamDto): Promise<ExamDocument> {
    const updatedExam = await this.examModel
      .findByIdAndUpdate(id, updateExamDto, { new: true })
      .exec();
      
    if (!updatedExam) {
      throw new NotFoundException(`Exam with ID ${id} not found`);
    }
    
    return updatedExam;
  }

  async removeExam(id: string): Promise<{ deleted: boolean }> {
   
    await this.questionModel.deleteMany({ examId: id }).exec();
    
    
    const result = await this.examModel.deleteOne({ _id: id }).exec();
    if (result.deletedCount === 0) {
      throw new NotFoundException(`Exam with ID ${id} not found`);
    }
    return { deleted: true };
  }

 
  async addQuestion(examId: string, createQuestionDto: CreateQuestionDto): Promise<QuestionDocument> {
    
    const exam = await this.findExamById(examId);
    
   
    const correctAnswerIndex = createQuestionDto.correctAnswer;
    if (correctAnswerIndex < 0 || correctAnswerIndex >= createQuestionDto.options.length) {
      throw new BadRequestException('Correct answer index is out of range');
    }
    
    const question = new this.questionModel({
      ...createQuestionDto,
      examId,
    });
    
    return question.save();
  }

  async findAllQuestions(examId: string): Promise<QuestionDocument[]> {
    
    await this.findExamById(examId);
    
    return this.questionModel.find({ examId }).exec();
  }

  async findQuestionById(examId: string, questionId: string): Promise<QuestionDocument> {
    const question = await this.questionModel.findOne({ _id: questionId, examId }).exec();
    if (!question) {
      throw new NotFoundException(`Question with ID ${questionId} not found in exam ${examId}`);
    }
    return question;
  }

  async updateQuestion(
    examId: string, 
    questionId: string, 
    updateQuestionDto: UpdateQuestionDto
  ): Promise<QuestionDocument> {
    
    if (updateQuestionDto.options && updateQuestionDto.correctAnswer !== undefined) {
      if (
        updateQuestionDto.correctAnswer < 0 || 
        updateQuestionDto.correctAnswer >= updateQuestionDto.options.length
      ) {
        throw new BadRequestException('Correct answer index is out of range');
      }
    }
    
  
    if (updateQuestionDto.correctAnswer !== undefined && !updateQuestionDto.options) {
      const question = await this.findQuestionById(examId, questionId);
      if (
        updateQuestionDto.correctAnswer < 0 || 
        updateQuestionDto.correctAnswer >= question.options.length
      ) {
        throw new BadRequestException('Correct answer index is out of range');
      }
    }
    
    const updatedQuestion = await this.questionModel
      .findOneAndUpdate(
        { _id: questionId, examId }, 
        updateQuestionDto, 
        { new: true }
      )
      .exec();
      
    if (!updatedQuestion) {
      throw new NotFoundException(`Question with ID ${questionId} not found in exam ${examId}`);
    }
    
    return updatedQuestion;
  }

  async removeQuestion(examId: string, questionId: string): Promise<{ deleted: boolean }> {
    const result = await this.questionModel
      .deleteOne({ _id: questionId, examId })
      .exec();
      
    if (result.deletedCount === 0) {
      throw new NotFoundException(`Question with ID ${questionId} not found in exam ${examId}`);
    }
    
    return { deleted: true };
  }
}
