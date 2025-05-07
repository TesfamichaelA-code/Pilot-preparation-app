import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Resource, ResourceDocument } from './schemas/resource.schema';
import { CreateResourceDto, ResourceQueryDto, UpdateResourceDto } from './dto/resource.dto';
import * as fs from 'fs';
import * as path from 'path';

@Injectable()
export class ResourcesService {
  constructor(
    @InjectModel(Resource.name) private resourceModel: Model<ResourceDocument>,
  ) {}

  async create(createResourceDto: CreateResourceDto, file?: Express.Multer.File): Promise<ResourceDocument> {
    // Using type assertion to allow adding additional properties
    const resourceData: any = { ...createResourceDto };
    
    if (file) {
      resourceData.fileUrl = `/uploads/${file.filename}`;
      resourceData.fileSize = file.size;
      resourceData.mimeType = file.mimetype;
    }
    
    const createdResource = new this.resourceModel(resourceData);
    return createdResource.save();
  }

  async findAll(query: ResourceQueryDto): Promise<ResourceDocument[]> {
    const { type, category, search, tag, limit = 10, skip = 0 } = query;
    
    const filter: any = {};
    
    if (type) {
      filter.type = type;
    }
    
    if (category) {
      filter.category = category;
    }
    
    if (tag) {
      filter.tags = tag;
    }
    
    if (search) {
      filter.$or = [
        { title: { $regex: search, $options: 'i' } },
        { description: { $regex: search, $options: 'i' } },
      ];
    }
    
    return this.resourceModel
      .find(filter)
      .skip(skip)
      .limit(limit)
      .sort({ createdAt: -1 })
      .exec();
  }

  async findOne(id: string): Promise<ResourceDocument> {
    const resource = await this.resourceModel.findById(id).exec();
    
    if (!resource) {
      throw new NotFoundException(`Resource with ID ${id} not found`);
    }
    
    return resource;
  }

  async update(id: string, updateResourceDto: UpdateResourceDto): Promise<ResourceDocument> {
    const updatedResource = await this.resourceModel
      .findByIdAndUpdate(id, updateResourceDto, { new: true })
      .exec();
      
    if (!updatedResource) {
      throw new NotFoundException(`Resource with ID ${id} not found`);
    }
    
    return updatedResource;
  }

  async remove(id: string): Promise<{ deleted: boolean }> {
    const resource = await this.resourceModel.findById(id).exec();
    
    if (!resource) {
      throw new NotFoundException(`Resource with ID ${id} not found`);
    }
    
    // If resource has a file, delete it
    if (resource.fileUrl) {
      try {
        const filePath = path.join(process.cwd(), resource.fileUrl.replace(/^\/uploads/, 'uploads'));
        if (fs.existsSync(filePath)) {
          fs.unlinkSync(filePath);
        }
      } catch (error) {
        console.error('Error deleting file:', error);
      }
    }
    
    await this.resourceModel.findByIdAndDelete(id).exec();
    return { deleted: true };
  }

  async getCategories(): Promise<string[]> {
    const resources = await this.resourceModel.distinct('category').exec();
    return resources.filter(category => category); // Filter out null/undefined
  }
  
  async getTags(): Promise<string[]> {
    const resources = await this.resourceModel.distinct('tags').exec();
    return resources.filter(tag => tag); // Filter out null/undefined
  }
}