import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type ResourceDocument = Resource & Document;

export enum ResourceType {
  VIDEO = 'video',
  DOCUMENT = 'document',
  AUDIO = 'audio',
  LINK = 'link',
  IMAGE = 'image',
}

export enum ResourceCategory {
  FLIGHT_THEORY = 'flightTheory',
  AIRCRAFT_SYSTEMS = 'aircraftSystems',
  NAVIGATION = 'navigation',
  METEOROLOGY = 'meteorology',
  REGULATIONS = 'regulations',
  HUMAN_FACTORS = 'humanFactors',
  ETHIOPIAN_AIRLINES = 'ethiopianAirlines',
  INTERVIEW_PREP = 'interviewPrep',
}

@Schema({ timestamps: true })
export class Resource {
  @Prop({ required: true })
  title: string;

  @Prop()
  description: string;

  @Prop({ required: true, enum: ResourceType })
  type: ResourceType;

  @Prop({ enum: ResourceCategory })
  category: ResourceCategory;

  @Prop()
  author: string;

  @Prop()
  tags: string[];

  @Prop()
  fileUrl: string;

  @Prop()
  fileSize: number;

  @Prop()
  mimeType: string;

  @Prop()
  externalUrl: string;

  @Prop({ default: true })
  isActive: boolean;

  @Prop()
  publishedDate: Date;
}

export const ResourceSchema = SchemaFactory.createForClass(Resource);