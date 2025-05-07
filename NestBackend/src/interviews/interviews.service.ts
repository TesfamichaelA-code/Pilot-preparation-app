import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { InterviewQuestion, InterviewQuestionDocument } from './schemas/interview-question.schema';
import { CreateInterviewQuestionDto, UpdateInterviewQuestionDto, InterviewQueryDto } from './dto/interview.dto';

@Injectable()
export class InterviewsService {
  constructor(
    @InjectModel(InterviewQuestion.name) private interviewQuestionModel: Model<InterviewQuestionDocument>,
  ) {}

  async create(createInterviewQuestionDto: CreateInterviewQuestionDto): Promise<InterviewQuestionDocument> {
    const createdQuestion = new this.interviewQuestionModel(createInterviewQuestionDto);
    return createdQuestion.save();
  }

  async findAll(query: InterviewQueryDto): Promise<InterviewQuestionDocument[]> {
    const { category, difficulty, limit = 10, skip = 0 } = query;
    
    const filter: any = {};
    if (category) filter.category = category;
    if (difficulty) filter.difficulty = difficulty;
    
    return this.interviewQuestionModel
      .find(filter)
      .skip(skip)
      .limit(limit)
      .exec();
  }

  async findOne(id: string): Promise<InterviewQuestionDocument> {
    const question = await this.interviewQuestionModel.findById(id).exec();
    if (!question) {
      throw new NotFoundException(`Interview question with ID ${id} not found`);
    }
    return question;
  }

  async update(
    id: string,
    updateInterviewQuestionDto: UpdateInterviewQuestionDto,
  ): Promise<InterviewQuestionDocument> {
    const updatedQuestion = await this.interviewQuestionModel
      .findByIdAndUpdate(id, updateInterviewQuestionDto, { new: true })
      .exec();
      
    if (!updatedQuestion) {
      throw new NotFoundException(`Interview question with ID ${id} not found`);
    }
    
    return updatedQuestion;
  }

  async remove(id: string): Promise<{ deleted: boolean }> {
    const result = await this.interviewQuestionModel.deleteOne({ _id: id }).exec();
    if (result.deletedCount === 0) {
      throw new NotFoundException(`Interview question with ID ${id} not found`);
    }
    return { deleted: true };
  }
}
