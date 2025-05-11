import { Controller, Get, Post, Put, Delete, Body, Param, Query, UseGuards } from '@nestjs/common';
import { ExamsService } from './exams.service';
import { 
  CreateExamDto, 
  UpdateExamDto, 
  CreateQuestionDto, 
  UpdateQuestionDto,
  ExamQueryDto
} from './dto/exam.dto';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth } from '@nestjs/swagger';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../users/schemas/user.schema';

@ApiTags('Exams')
@ApiBearerAuth()
@Controller('exams')
export class ExamsController {
  constructor(private readonly examsService: ExamsService) {}

  
  @Post()
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Create a new exam' })
  @ApiResponse({ status: 201, description: 'Exam successfully created' })
  async createExam(@Body() createExamDto: CreateExamDto) {
    return this.examsService.createExam(createExamDto);
  }

  @Get()
  @ApiOperation({ summary: 'Get all exams' })
  @ApiResponse({ status: 200, description: 'Return all exams' })
  async findAllExams(@Query() query: ExamQueryDto) {
    return this.examsService.findAllExams(query);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get an exam by ID' })
  @ApiResponse({ status: 200, description: 'Return the exam' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async findExamById(@Param('id') id: string) {
    return this.examsService.findExamById(id);
  }

  @Put(':id')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Update an exam' })
  @ApiResponse({ status: 200, description: 'Exam successfully updated' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async updateExam(@Param('id') id: string, @Body() updateExamDto: UpdateExamDto) {
    return this.examsService.updateExam(id, updateExamDto);
  }

  @Delete(':id')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Delete an exam' })
  @ApiResponse({ status: 200, description: 'Exam successfully deleted' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async removeExam(@Param('id') id: string) {
    return this.examsService.removeExam(id);
  }


  @Post(':examId/questions')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Add a question to an exam' })
  @ApiResponse({ status: 201, description: 'Question successfully added' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async addQuestion(
    @Param('examId') examId: string, 
    @Body() createQuestionDto: CreateQuestionDto
  ) {
    return this.examsService.addQuestion(examId, createQuestionDto);
  }

  @Get(':examId/questions')
  @ApiOperation({ summary: 'Get all questions for an exam' })
  @ApiResponse({ status: 200, description: 'Return all questions' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async findAllQuestions(@Param('examId') examId: string) {
    return this.examsService.findAllQuestions(examId);
  }

  @Get(':examId/questions/:questionId')
  @ApiOperation({ summary: 'Get a question by ID' })
  @ApiResponse({ status: 200, description: 'Return the question' })
  @ApiResponse({ status: 404, description: 'Question not found' })
  async findQuestionById(
    @Param('examId') examId: string,
    @Param('questionId') questionId: string
  ) {
    return this.examsService.findQuestionById(examId, questionId);
  }

  @Put(':examId/questions/:questionId')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Update a question' })
  @ApiResponse({ status: 200, description: 'Question successfully updated' })
  @ApiResponse({ status: 404, description: 'Question not found' })
  async updateQuestion(
    @Param('examId') examId: string,
    @Param('questionId') questionId: string,
    @Body() updateQuestionDto: UpdateQuestionDto
  ) {
    return this.examsService.updateQuestion(examId, questionId, updateQuestionDto);
  }

  @Delete(':examId/questions/:questionId')
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Delete a question' })
  @ApiResponse({ status: 200, description: 'Question successfully deleted' })
  @ApiResponse({ status: 404, description: 'Question not found' })
  async removeQuestion(
    @Param('examId') examId: string,
    @Param('questionId') questionId: string
  ) {
    return this.examsService.removeQuestion(examId, questionId);
  }
}
