import { MongoMemoryServer } from 'mongodb-memory-server';

let mongoMemoryServer: MongoMemoryServer;

export const getMongoMemoryServer = async (): Promise<{ uri: string }> => {
  if (!mongoMemoryServer) {
    mongoMemoryServer = await MongoMemoryServer.create();
    const uri = mongoMemoryServer.getUri();
    console.log(`MongoDB Memory Server running at ${uri}`);
    return { uri };
  }
  
  return { uri: mongoMemoryServer.getUri() };
};

export const closeMongoMemoryServer = async (): Promise<void> => {
  if (mongoMemoryServer) {
    await mongoMemoryServer.stop();
    console.log('MongoDB Memory Server stopped');
  }
};