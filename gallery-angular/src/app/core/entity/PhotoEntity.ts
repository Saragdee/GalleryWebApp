import {TagEntity} from "./TagEntity";

export interface PhotoEntity {
  id: any;
  image: any;
  thumbnail: string;
  description: string;
  uploadDate: any;
  tags: TagEntity[];
}
