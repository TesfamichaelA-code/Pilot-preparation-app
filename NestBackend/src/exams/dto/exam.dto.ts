import { 
  IsString, 
  IsNotEmpty, 
  IsOptional, 
  IsEnum, 
  IsBoolean, 
  IsNumber, 
  Min, 
  Max,
  IsArray,
  ArrayMinSize,
  ValidateNested,
  IsInt
} from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty } from '@nestjs/swagger';
import { ExamCategory, Difficulty } from '../schemas/exam.schema';

// Exam DTOs
export class CreateExamDto {
  @ApiProperty({ example: 'Aviation Physics Mock Exam' })
  @IsNotEmpty()
  @IsString()
  title: string;

  @ApiProperty({ example: 'Test your knowledge of basic aviation physics' })
  @IsOptional()
  @IsString()
  description?: string;

  @ApiProperty({ enum: ExamCategory, example: ExamCategory.PILOT_TRAINEE })
  @IsOptional()
  @IsEnum(ExamCategory)
  category?: ExamCategory;

  @ApiProperty({ enum: Difficulty, example: Difficulty.MEDIUM })
  @IsOptional()
  @IsEnum(Difficulty)
  difficulty?: Difficulty;

  @ApiProperty({ example: 60, description: 'Duration in minutes' })
  @IsOptional()
  @IsNumber()
  @Min(0)
  durationMinutes?: number;

  @ApiProperty({ example: true })
  @IsOptional()
  @IsBoolean()
  isActive?: boolean;
}

export class UpdateExamDto {
  @ApiProperty({ example: 'Updated Aviation Physics Exam', required: false })
  @IsOptional()
  @IsString()
  title?: string;

  @ApiProperty({ example: 'Updated description', required: false })
  @IsOptional()
  @IsString()
  description?: string;

  @ApiProperty({ enum: ExamCategory, required: false })
  @IsOptional()
  @IsEnum(ExamCategory)
  category?: ExamCategory;

  @ApiProperty({ enum: Difficulty, required: false })
  @IsOptional()
  @IsEnum(Difficulty)
  difficulty?: Difficulty;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  @Min(0)
  durationMinutes?: number;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsBoolean()
  isActive?: boolean;
}

export class ExamQueryDto {
  @ApiProperty({ enum: ExamCategory, required: false })
  @IsOptional()
  @IsEnum(ExamCategory)
  category?: ExamCategory;

  @ApiProperty({ enum: Difficulty, required: false })
  @IsOptional()
  @IsEnum(Difficulty)
  difficulty?: Difficulty;

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

// Question DTOs
export class CreateQuestionDto {
  @ApiProperty({ example: 'What is the primary force that enables an aircraft to fly?' })
  @IsNotEmpty()
  @IsString()
  text: string;

  @ApiProperty({ example: ['Gravity', 'Lift', 'Thrust', 'Drag'] })
  @IsArray()
  @ArrayMinSize(2)
  @IsString({ each: true })
  options: string[];

  @ApiProperty({ example: 1, description: 'Index of the correct answer in options array' })
  @IsNumber()
  @IsInt()
  @Min(0)
  correctAnswer: number;

  @ApiProperty({ 
    example: 'Lift is the force that directly opposes the weight of an aircraft and holds the aircraft in the air', 
    required: false 
  })
  @IsOptional()
  @IsString()
  explanation?: string;
}

export class UpdateQuestionDto {
  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  text?: string;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsArray()
  @ArrayMinSize(2)
  @IsString({ each: true })
  options?: string[];

  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  @IsInt()
  @Min(0)
  correctAnswer?: number;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  explanation?: string;
}
