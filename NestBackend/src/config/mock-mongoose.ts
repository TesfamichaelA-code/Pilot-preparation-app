import { Injectable } from '@nestjs/common';

// Create a mock implementation of Mongoose for development purposes
@Injectable()
export class MockMongooseService {
  private models = {};
  private mockData = {};

  // Create a mocked model for each schema
  createModel(name: string, schema: any) {
    this.models[name] = {
      name,
      schema,
      find: () => this.createQueryBuilder(name),
      findOne: () => this.createQueryBuilder(name),
      findById: () => this.createQueryBuilder(name),
      create: (doc) => this.mockCreate(name, doc),
      updateOne: () => ({ exec: () => Promise.resolve({ modifiedCount: 1 }) }),
      deleteOne: () => ({ exec: () => Promise.resolve({ deletedCount: 1 }) }),
    };
    
    // Initialize empty collection for this model
    this.mockData[name] = [];
    
    return this.models[name];
  }

  private createQueryBuilder(modelName: string) {
    return {
      exec: () => Promise.resolve(this.mockData[modelName] || []),
      populate: () => this.createQueryBuilder(modelName),
      skip: () => this.createQueryBuilder(modelName),
      limit: () => this.createQueryBuilder(modelName),
      sort: () => this.createQueryBuilder(modelName),
      select: () => this.createQueryBuilder(modelName),
    };
  }

  private mockCreate(modelName: string, doc) {
    const newDoc = {
      _id: `mock_id_${Date.now()}`,
      ...doc,
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    
    this.mockData[modelName].push(newDoc);
    return Promise.resolve(newDoc);
  }
}

// Export a factory function to create a mock connection
export const createMockMongooseConnection = () => {
  console.log('Using mock MongoDB implementation for development');
  return {
    getModelToken: (name: string) => name,
    getConnectionToken: () => 'default',
  };
};