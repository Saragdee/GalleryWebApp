import {TagEntity} from "./TagEntity";

export interface PhotoEntity {
  id: number;
  image: any;
  thumbnail: string;
  description: string;
  uploadDate: any;
  tags: TagEntity[];
}
