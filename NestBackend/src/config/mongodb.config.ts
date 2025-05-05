import { MongooseModuleOptions } from '@nestjs/mongoose';
import { ConfigService } from '@nestjs/config';
import { getMongoMemoryServer } from './mongodb-memory-server';

export const getMongooseConfig = async (
  configService: ConfigService,
): Promise<MongooseModuleOptions> => {
  const useInMemoryDb = process.env.NODE_ENV === 'test' || process.env.USE_MEMORY_DB === 'true';
  
  if (useInMemoryDb) {
    const { uri } = await getMongoMemoryServer();
    return {
      uri,
    };
  }
  
  return {
    uri: configService.get<string>('database.uri'),
  };
};