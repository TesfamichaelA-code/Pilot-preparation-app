import { 
  IsArray, 
  IsNumber, 
  IsOptional, 
  IsInt, 
  Min, 
  Max,
  IsString,
  IsEnum
} from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class SubmitExamResultDto {
  @ApiProperty({ 
    example: [0, 2, 1, 3], 
    description: 'Array of answer indices (0-based) in the same order as the questions' 
  })
  @IsArray()
  @IsNumber({}, { each: true })
  @IsInt({ each: true })
  @Min(0, { each: true })
  answers: number[];
}

export enum SortDirection {
  ASC = 'asc',
  DESC = 'desc',
}

export class ProgressQueryDto {
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

  @ApiProperty({ required: false, default: 'completedAt' })
  @IsOptional()
  @IsString()
  sortBy?: string = 'completedAt';

  @ApiProperty({ 
    required: false, 
    default: SortDirection.DESC,
    enum: SortDirection
  })
  @IsOptional()
  @IsEnum(SortDirection)
  sortDirection?: SortDirection = SortDirection.DESC;
}
