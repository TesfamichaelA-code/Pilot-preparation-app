import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Schema as MongooseSchema } from 'mongoose';

export type ExamResultDocument = ExamResult & Document;

@Schema()
export class AnswerDetail {
  @Prop({ type: MongooseSchema.Types.ObjectId, required: true })
  questionId: string;

  @Prop({ required: true })
  questionText: string;

  @Prop({ required: true })
  userAnswer: number;

  @Prop({ required: true })
  correctAnswer: number;

  @Prop({ required: true })
  isCorrect: boolean;
}

@Schema({ timestamps: true })
export class ExamResult {
  @Prop({ 
    type: MongooseSchema.Types.ObjectId, 
    ref: 'User', 
    required: true 
  })
  userId: string;

  @Prop({ 
    type: MongooseSchema.Types.ObjectId, 
    ref: 'Exam', 
    required: true 
  })
  examId: string;

  @Prop({ required: true, min: 0, max: 100 })
  score: number;

  @Prop({ required: true, min: 0 })
  totalQuestions: number;

  @Prop({ required: true, min: 0 })
  correctAnswers: number;

  @Prop({ type: [Object], default: [] })
  answerDetails: AnswerDetail[];

  @Prop({ required: true, default: Date.now })
  completedAt: Date;
}

export const ExamResultSchema = SchemaFactory.createForClass(ExamResult);
