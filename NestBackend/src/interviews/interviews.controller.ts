import { Controller, Get, Post, Put, Delete, Body, Param, Query } from '@nestjs/common';
import { InterviewsService } from './interviews.service';
import { CreateInterviewQuestionDto, UpdateInterviewQuestionDto, InterviewQueryDto } from './dto/interview.dto';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth } from '@nestjs/swagger';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../users/schemas/user.schema';

@ApiTags('Interviews')
@ApiBearerAuth()
@Controller('interviews')
export class InterviewsController {
  constructor(private readonly interviewsService: InterviewsService) {}

  @Post()
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Create a new interview question' })
  @ApiResponse({ status: 201, description: 'Interview question successfully created' })
  async create(@Body() createInterviewQuestionDto: CreateInterviewQuestionDto) {
    return this.interviewsService.create(createInterviewQuestionDto);
  }

  @Get()
  @ApiOperation({ summary: 'Get all interview questions' })
  @ApiResponse({ status: 200, description: 'Return all interview questions' })
  async findAll(@Query() query: InterviewQueryDto) {
    return this.interviewsService.findAll(query);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get an interview question by ID' })
  @ApiResponse({ status: 200, description: 'Return the interview question' })
  @ApiResponse({ status: 404, description: 'Interview question not found' })
  async findOne(@Param('id') id: string) {
    return this.interviewsService.findOne(id);
  }

  @Put(':id')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Update an interview question' })
  @ApiResponse({ status: 200, description: 'Interview question successfully updated' })
  @ApiResponse({ status: 404, description: 'Interview question not found' })
  async update(
    @Param('id') id: string,
    @Body() updateInterviewQuestionDto: UpdateInterviewQuestionDto,
  ) {
    return this.interviewsService.update(id, updateInterviewQuestionDto);
  }

  @Delete(':id')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Delete an interview question' })
  @ApiResponse({ status: 200, description: 'Interview question successfully deleted' })
  @ApiResponse({ status: 404, description: 'Interview question not found' })
  async remove(@Param('id') id: string) {
    return this.interviewsService.remove(id);
  }
}
