import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ProgressController } from './progress.controller';
import { ProgressService } from './progress.service';
import { ExamResult, ExamResultSchema } from './schemas/exam-result.schema';
import { ExamsModule } from '../exams/exams.module';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: ExamResult.name, schema: ExamResultSchema },
    ]),
    ExamsModule,
  ],
  controllers: [ProgressController],
  providers: [ProgressService],
  exports: [ProgressService],
})
export class ProgressModule {}
