import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { InterviewsController } from './interviews.controller';
import { InterviewsService } from './interviews.service';
import { InterviewQuestion, InterviewQuestionSchema } from './schemas/interview-question.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: InterviewQuestion.name, schema: InterviewQuestionSchema },
    ]),
  ],
  controllers: [InterviewsController],
  providers: [InterviewsService],
  exports: [InterviewsService],
})
export class InterviewsModule {}
