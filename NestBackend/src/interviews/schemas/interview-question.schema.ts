import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type InterviewQuestionDocument = InterviewQuestion & Document;

export enum InterviewCategory {
  TECHNICAL = 'technical',
  BEHAVIORAL = 'behavioral',
  SITUATIONAL = 'situational',
  PILOT_SPECIFIC = 'pilotSpecific',
  FLIGHT_INSTRUCTOR = 'flightInstructor',
}

export enum InterviewDifficulty {
  EASY = 'easy',
  MEDIUM = 'medium',
  HARD = 'hard',
}

@Schema({ timestamps: true })
export class InterviewQuestion {
  @Prop({ required: true })
  question: string;

  @Prop()
  sampleAnswer: string;

  @Prop({ 
    type: String, 
    enum: InterviewCategory, 
    default: InterviewCategory.TECHNICAL 
  })
  category: InterviewCategory;

  @Prop({ 
    type: String, 
    enum: InterviewDifficulty, 
    default: InterviewDifficulty.MEDIUM 
  })
  difficulty: InterviewDifficulty;

  @Prop()
  tipsForAnswering: string;

  @Prop()
  yearAsked: number;
}

export const InterviewQuestionSchema = SchemaFactory.createForClass(InterviewQuestion);
