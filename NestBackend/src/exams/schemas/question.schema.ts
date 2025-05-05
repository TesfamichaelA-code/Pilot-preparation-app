import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Schema as MongooseSchema } from 'mongoose';

export type QuestionDocument = Question & Document;

@Schema({ timestamps: true })
export class Question {
  @Prop({ required: true, type: MongooseSchema.Types.ObjectId, ref: 'Exam' })
  examId: string;

  @Prop({ required: true })
  text: string;

  @Prop({ required: true, type: [String] })
  options: string[];

  @Prop({ required: true, min: 0 })
  correctAnswer: number;

  @Prop()
  explanation: string;
}

export const QuestionSchema = SchemaFactory.createForClass(Question);
