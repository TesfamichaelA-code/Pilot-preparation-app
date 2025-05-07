import { Controller, Get, Post, Body, Param, Query, Req } from '@nestjs/common';
import { ProgressService } from './progress.service';
import { SubmitExamResultDto, ProgressQueryDto } from './dto/progress.dto';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth } from '@nestjs/swagger';

@ApiTags('Progress')
@ApiBearerAuth()
@Controller('progress')
export class ProgressController {
  constructor(private readonly progressService: ProgressService) {}

  @Post('exams/:examId/submit')
  @ApiOperation({ summary: 'Submit exam results' })
  @ApiResponse({ status: 201, description: 'Exam results successfully submitted' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async submitExamResult(
    @Param('examId') examId: string,
    @Body() submitExamResultDto: SubmitExamResultDto,
    @Req() req,
  ) {
    return this.progressService.submitExamResult(examId, submitExamResultDto, req.user._id);
  }

  @Get('exams')
  @ApiOperation({ summary: 'Get user exam results' })
  @ApiResponse({ status: 200, description: 'Return exam results' })
  async getUserExamResults(@Req() req, @Query() query: ProgressQueryDto) {
    return this.progressService.getUserExamResults(req.user._id, query);
  }

  @Get('exams/:examId')
  @ApiOperation({ summary: 'Get user results for a specific exam' })
  @ApiResponse({ status: 200, description: 'Return exam results' })
  @ApiResponse({ status: 404, description: 'Exam not found' })
  async getUserExamResultByExam(@Param('examId') examId: string, @Req() req) {
    return this.progressService.getUserExamResultByExam(examId, req.user._id);
  }

  @Get('stats')
  @ApiOperation({ summary: 'Get user progress statistics' })
  @ApiResponse({ status: 200, description: 'Return progress statistics' })
  async getUserProgressStats(@Req() req) {
    return this.progressService.getUserProgressStats(req.user._id);
  }
}
