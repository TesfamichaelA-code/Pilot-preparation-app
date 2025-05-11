import { ApiProperty } from '@nestjs/swagger';
import { IsOptional, IsEnum, IsString, IsNumber, Min, Max } from 'class-validator';
import { Type } from 'class-transformer';
import { SortDirection } from '../../progress/dto/progress.dto';

export class StudentProgressQueryDto {
  @ApiProperty({ required: false, description: 'Start date for filtering exam results' })
  @IsOptional()
  @IsString()
  startDate?: string;

  @ApiProperty({ required: false, description: 'End date for filtering exam results' })
  @IsOptional()
  @IsString()
  endDate?: string;

  @ApiProperty({ required: false, enum: SortDirection, default: SortDirection.DESC })
  @IsOptional()
  @IsEnum(SortDirection)
  sortDirection?: SortDirection = SortDirection.DESC;
}

export class UserStatisticsQueryDto {
  @ApiProperty({ required: false, description: 'Number of days to look back for statistics' })
  @IsOptional()
  @Type(() => Number)
  @IsNumber()
  @Min(1)
  @Max(365)
  days?: number = 30;
}

// Response interfaces for better typing and documentation

export interface UserBasicInfo {
  id: string;
  name: string;
  email: string;
}

export interface ExamResultBasicInfo {
  id: string;
  examId: string;
  examTitle: string;
  score: number;
  totalQuestions: number;
  correctAnswers: number;
  completedAt: Date;
}

export interface CategoryAttempt {
  category: string;
  count: number;
  averageScore: number;
}

export interface DifficultyAttempt {
  difficulty: string;
  count: number;
  averageScore: number;
}

export interface ProgressPoint {
  date: Date;
  averageScore: number;
  count: number;
}

export interface StudentProgressSummary {
  totalExamsTaken: number;
  averageScore: number;
  progressOverTime: ProgressPoint[];
  attemptsByCategory: CategoryAttempt[];
  attemptsByDifficulty: DifficultyAttempt[];
}

export interface StudentProgressResponse {
  user: UserBasicInfo;
  examResults: ExamResultBasicInfo[];
  summary: StudentProgressSummary;
}

export interface RoleCount {
  role: string;
  count: number;
}

export interface RegistrationPoint {
  date: Date;
  count: number;
}

export interface UserStatisticsResponse {
  totalUsers: number;
  usersByRole: RoleCount[];
  newUsersLast30Days: number;
  activeUsers: number;
  userEngagement: number; // Average exams taken per user
  userRegistrationOverTime: RegistrationPoint[];
}

export interface CategoryCount {
  category: string;
  count: number;
}

export interface DifficultyCount {
  difficulty: string;
  count: number;
}

export interface MostAttemptedExam {
  examId: string;
  title: string;
  attemptCount: number;
}

export interface HardestExam {
  examId: string;
  title: string;
  averageScore: number;
  attemptCount: number;
}

export interface ExamStatisticsResponse {
  totalExams: number;
  totalQuestions: number;
  examsByCategory: CategoryCount[];
  examsByDifficulty: DifficultyCount[];
  mostAttemptedExams: MostAttemptedExam[];
  hardestExams: HardestExam[];
}

export interface ActivityPoint {
  date: Date;
  count: number;
}

export interface OverallStatisticsResponse {
  totalUsers: number;
  totalExams: number;
  totalExamAttempts: number;
  averageScore: number;
  activityOverTime: ActivityPoint[];
}