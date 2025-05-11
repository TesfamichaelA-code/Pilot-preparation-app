import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type ExamDocument = Exam & Document;

export enum ExamCategory {
  PILOT_TRAINEE = 'pilotTrainee',
  FLIGHT_INSTRUCTOR = 'flightInstructor',
}

export enum Difficulty {
  EASY = 'easy',
  MEDIUM = 'medium',
  HARD = 'hard',
}

@Schema({ timestamps: true })
export class Exam {
  @Prop({ required: true })
  title: string;

  @Prop()
  description: string;

  @Prop({ 
    type: String, 
    enum: ExamCategory, 
    default: ExamCategory.PILOT_TRAINEE 
  })
  category: ExamCategory;

  @Prop({ 
    type: String, 
    enum: Difficulty, 
    default: Difficulty.MEDIUM 
  })
  difficulty: Difficulty;

  @Prop({ default: 0 })
  durationMinutes: number;

  @Prop({ default: true })
  isActive: boolean;
}

export const ExamSchema = SchemaFactory.createForClass(Exam);
