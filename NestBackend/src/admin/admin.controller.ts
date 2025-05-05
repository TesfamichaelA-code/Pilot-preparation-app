import { Controller, Get, Param, Query, UseGuards } from '@nestjs/common';
import { AdminService } from './admin.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../users/schemas/user.schema';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth } from '@nestjs/swagger';
import { StudentProgressQueryDto, UserStatisticsQueryDto } from './dto/admin.dto';

@ApiTags('admin')
@ApiBearerAuth()
@Controller('admin')
@UseGuards(JwtAuthGuard, RolesGuard)
@Roles(Role.ADMIN)
export class AdminController {
  constructor(private readonly adminService: AdminService) {}

  @Get('students')
  @ApiOperation({ summary: 'Get all students with basic info (admin only)' })
  @ApiResponse({ status: 200, description: 'Returns a list of all students' })
  async getAllStudents() {
    return this.adminService.getAllStudents();
  }

  @Get('students/:id/progress')
  @ApiOperation({ summary: 'Get detailed progress of a specific student (admin only)' })
  @ApiResponse({ status: 200, description: 'Returns detailed progress data for a student' })
  async getStudentProgress(@Param('id') id: string, @Query() query: StudentProgressQueryDto) {
    return this.adminService.getStudentProgress(id, query);
  }

  @Get('statistics/users')
  @ApiOperation({ summary: 'Get user statistics (admin only)' })
  @ApiResponse({ status: 200, description: 'Returns user statistics' })
  async getUserStatistics(@Query() query: UserStatisticsQueryDto) {
    return this.adminService.getUserStatistics(query);
  }

  @Get('statistics/exams')
  @ApiOperation({ summary: 'Get exam statistics (admin only)' })
  @ApiResponse({ status: 200, description: 'Returns exam statistics' })
  async getExamStatistics() {
    return this.adminService.getExamStatistics();
  }

  @Get('statistics/overall')
  @ApiOperation({ summary: 'Get overall system statistics (admin only)' })
  @ApiResponse({ status: 200, description: 'Returns overall system statistics' })
  async getOverallStatistics() {
    return this.adminService.getOverallStatistics();
  }
}