import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Delete,
  UseGuards,
  Query,
  Put,
  UseInterceptors,
  UploadedFile,
  BadRequestException,
  Res,
} from '@nestjs/common';
import { ResourcesService } from './resources.service';
import { CreateResourceDto, ResourceQueryDto, UpdateResourceDto } from './dto/resource.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../users/schemas/user.schema';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApiTags, ApiOperation, ApiConsumes, ApiBody } from '@nestjs/swagger';
import { Response } from 'express';
import * as path from 'path';

@ApiTags('resources')
@Controller('resources')
@UseGuards(JwtAuthGuard)
export class ResourcesController {
  constructor(private readonly resourcesService: ResourcesService) {}

  @Post()
  @UseGuards(RolesGuard)
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Create a new resource (admin only)' })
  async create(@Body() createResourceDto: CreateResourceDto) {
    return this.resourcesService.create(createResourceDto);
  }

  @Post('upload')
  @UseGuards(RolesGuard)
  @Roles(Role.ADMIN)
  @UseInterceptors(FileInterceptor('file'))
  @ApiOperation({ summary: 'Upload a file and create a resource (admin only)' })
  @ApiConsumes('multipart/form-data')
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        file: {
          type: 'string',
          format: 'binary',
        },
        title: { type: 'string' },
        description: { type: 'string' },
        type: { type: 'string', enum: ['video', 'document', 'audio', 'image'] },
        category: { type: 'string' },
        author: { type: 'string' },
        tags: { type: 'array', items: { type: 'string' } },
        isActive: { type: 'boolean' },
      },
    },
  })
  async uploadFile(
    @UploadedFile() file: Express.Multer.File,
    @Body() createResourceDto: CreateResourceDto,
  ) {
    if (!file) {
      throw new BadRequestException('File is required');
    }
    
    return this.resourcesService.create(createResourceDto, file);
  }

  @Get()
  @ApiOperation({ summary: 'Get all resources with filtering options' })
  async findAll(@Query() query: ResourceQueryDto) {
    return this.resourcesService.findAll(query);
  }

  @Get('categories')
  @ApiOperation({ summary: 'Get all resource categories in use' })
  async getCategories() {
    return this.resourcesService.getCategories();
  }

  @Get('tags')
  @ApiOperation({ summary: 'Get all resource tags in use' })
  async getTags() {
    return this.resourcesService.getTags();
  }

  @Get('file/:filename')
  @ApiOperation({ summary: 'Download a resource file' })
  async getFile(@Param('filename') filename: string, @Res() res: Response) {
    return res.sendFile(filename, { root: './uploads' });
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get a resource by ID' })
  async findOne(@Param('id') id: string) {
    return this.resourcesService.findOne(id);
  }

  @Put(':id')
  @UseGuards(RolesGuard)
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Update a resource (admin only)' })
  async update(
    @Param('id') id: string,
    @Body() updateResourceDto: UpdateResourceDto,
  ) {
    return this.resourcesService.update(id, updateResourceDto);
  }

  @Delete(':id')
  @UseGuards(RolesGuard)
  @Roles(Role.ADMIN)
  @ApiOperation({ summary: 'Delete a resource (admin only)' })
  async remove(@Param('id') id: string) {
    return this.resourcesService.remove(id);
  }
}