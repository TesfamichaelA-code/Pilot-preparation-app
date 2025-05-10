import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ResourcesController } from './resources.controller';
import { ResourcesService } from './resources.service';
import { Resource, ResourceSchema } from './schemas/resource.schema';
import { MulterModule } from '@nestjs/platform-express';
import { diskStorage } from 'multer';
import { extname } from 'path';
import { v4 as uuidv4 } from 'uuid';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Resource.name, schema: ResourceSchema }]),
    MulterModule.register({
      storage: diskStorage({
        destination: './uploads',
        filename: (req, file, callback) => {
          const uniqueSuffix = uuidv4();
          const ext = extname(file.originalname);
          callback(null, `${uniqueSuffix}${ext}`);
        },
      }),
      fileFilter: (req, file, callback) => {
        // Allow only certain file types
        const allowedMimeTypes = [
          // Documents
          'application/pdf',
          'application/msword',
          'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
          // Images
          'image/jpeg',
          'image/png',
          // Audio
          'audio/mpeg',
          'audio/mp4',
          // Video
          'video/mp4',
          'video/mpeg',
        ];
        
        if (allowedMimeTypes.includes(file.mimetype)) {
          callback(null, true);
        } else {
          callback(new Error('Unsupported file type'), false);
        }
      },
      limits: {
        fileSize: 100 * 1024 * 1024, // 100MB max file size
      },
    }),
  ],
  controllers: [ResourcesController],
  providers: [ResourcesService],
  exports: [ResourcesService],
})
export class ResourcesModule {}