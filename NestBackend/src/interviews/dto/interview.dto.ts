import { IsString, IsNotEmpty, IsOptional, IsEnum, IsNumber, Min, Max, IsInt } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';
import { InterviewCategory, InterviewDifficulty } from '../schemas/interview-question.schema';

export class CreateInterviewQuestionDto {
  @ApiProperty({ example: 'Describe a situation where you had to make a difficult decision under pressure.' })
  @IsNotEmpty()
  @IsString()
  question: string;

  @ApiProperty({ 
    example: 'During a training flight, I experienced a simulated engine failure...',
    required: false 
  })
  @IsOptional()
  @IsString()
  sampleAnswer?: string;

  @ApiProperty({ enum: InterviewCategory, example: InterviewCategory.SITUATIONAL })
  @IsOptional()
  @IsEnum(InterviewCategory)
  category?: InterviewCategory;

  @ApiProperty({ enum: InterviewDifficulty, example: InterviewDifficulty.MEDIUM })
  @IsOptional()
  @IsEnum(InterviewDifficulty)
  difficulty?: InterviewDifficulty;

  @ApiProperty({ 
    example: 'Focus on the STAR method - Situation, Task, Action, Result',
    required: false
  })
  @IsOptional()
  @IsString()
  tipsForAnswering?: string;

  @ApiProperty({ example: 2023, required: false })
  @IsOptional()
  @IsNumber()
  @IsInt()
  @Min(2000)
  @Max(new Date().getFullYear())
  yearAsked?: number;
}

export class UpdateInterviewQuestionDto {
  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  question?: string;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  sampleAnswer?: string;

  @ApiProperty({ enum: InterviewCategory, required: false })
  @IsOptional()
  @IsEnum(InterviewCategory)
  category?: InterviewCategory;

  @ApiProperty({ enum: InterviewDifficulty, required: false })
  @IsOptional()
  @IsEnum(InterviewDifficulty)
  difficulty?: InterviewDifficulty;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  tipsForAnswering?: string;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  @IsInt()
  @Min(2000)
  @Max(new Date().getFullYear())
  yearAsked?: number;
}

export class InterviewQueryDto {
  @ApiProperty({ enum: InterviewCategory, required: false })
  @IsOptional()
  @IsEnum(InterviewCategory)
  category?: InterviewCategory;

  @ApiProperty({ enum: InterviewDifficulty, required: false })
  @IsOptional()
  @IsEnum(InterviewDifficulty)
  difficulty?: InterviewDifficulty;

  @ApiProperty({ required: false, default: 10 })
  @IsOptional()
  @IsInt()
  @Min(1)
  @Max(100)
  limit?: number;

  @ApiProperty({ required: false, default: 0 })
  @IsOptional()
  @IsInt()
  @Min(0)
  skip?: number;
}
