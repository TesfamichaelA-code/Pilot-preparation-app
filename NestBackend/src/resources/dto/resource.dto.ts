import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsEnum, IsNotEmpty, IsOptional, IsString, IsUrl, IsArray, IsBoolean } from 'class-validator';
import { ResourceCategory, ResourceType } from '../schemas/resource.schema';

export class CreateResourceDto {
  @ApiProperty({ description: 'Title of the resource' })
  @IsNotEmpty()
  @IsString()
  title: string;

  @ApiPropertyOptional({ description: 'Description of the resource' })
  @IsOptional()
  @IsString()
  description?: string;

  @ApiProperty({ 
    description: 'Type of resource', 
    enum: ResourceType 
  })
  @IsEnum(ResourceType)
  type: ResourceType;

  @ApiPropertyOptional({ 
    description: 'Category of the resource', 
    enum: ResourceCategory 
  })
  @IsOptional()
  @IsEnum(ResourceCategory)
  category?: ResourceCategory;

  @ApiPropertyOptional({ description: 'Author of the resource' })
  @IsOptional()
  @IsString()
  author?: string;

  @ApiPropertyOptional({ 
    description: 'Tags to categorize the resource',
    type: [String]
  })
  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @ApiPropertyOptional({ 
    description: 'External URL for linked resources',
  })
  @IsOptional()
  @IsUrl()
  externalUrl?: string;

  @ApiPropertyOptional({ 
    description: 'Whether the resource is active and visible to students',
    default: true
  })
  @IsOptional()
  @IsBoolean()
  isActive?: boolean = true;
}

export class UpdateResourceDto {
  @ApiPropertyOptional({ description: 'Title of the resource' })
  @IsOptional()
  @IsString()
  title?: string;

  @ApiPropertyOptional({ description: 'Description of the resource' })
  @IsOptional()
  @IsString()
  description?: string;

  @ApiPropertyOptional({ 
    description: 'Type of resource', 
    enum: ResourceType 
  })
  @IsOptional()
  @IsEnum(ResourceType)
  type?: ResourceType;

  @ApiPropertyOptional({ 
    description: 'Category of the resource', 
    enum: ResourceCategory 
  })
  @IsOptional()
  @IsEnum(ResourceCategory)
  category?: ResourceCategory;

  @ApiPropertyOptional({ description: 'Author of the resource' })
  @IsOptional()
  @IsString()
  author?: string;

  @ApiPropertyOptional({ 
    description: 'Tags to categorize the resource',
    type: [String]
  })
  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @ApiPropertyOptional({ 
    description: 'External URL for linked resources',
  })
  @IsOptional()
  @IsUrl()
  externalUrl?: string;

  @ApiPropertyOptional({ 
    description: 'Whether the resource is active and visible to students'
  })
  @IsOptional()
  @IsBoolean()
  isActive?: boolean;
}

export class ResourceQueryDto {
  @ApiPropertyOptional({ 
    description: 'Filter by resource type', 
    enum: ResourceType 
  })
  @IsOptional()
  @IsEnum(ResourceType)
  type?: ResourceType;

  @ApiPropertyOptional({ 
    description: 'Filter by resource category', 
    enum: ResourceCategory 
  })
  @IsOptional()
  @IsEnum(ResourceCategory)
  category?: ResourceCategory;

  @ApiPropertyOptional({ 
    description: 'Search by title or description keyword' 
  })
  @IsOptional()
  @IsString()
  search?: string;

  @ApiPropertyOptional({ 
    description: 'Filter by tag',
  })
  @IsOptional()
  @IsString()
  tag?: string;

  @ApiPropertyOptional({ 
    description: 'Limit number of results',
    default: 10
  })
  @IsOptional()
  limit?: number = 10;

  @ApiPropertyOptional({ 
    description: 'Skip number of results (for pagination)',
    default: 0
  })
  @IsOptional()
  skip?: number = 0;
}