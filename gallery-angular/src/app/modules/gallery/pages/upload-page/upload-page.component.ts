import {Component} from '@angular/core';
import {PhotoService} from "../../../../core/service/photo.service";
import {PhotoEntity} from "../../../../core/entity/PhotoEntity";
import {Router} from "@angular/router";

@Component({
  selector: 'app-upload-page',
  templateUrl: './upload-page.component.html',
  styleUrl: './upload-page.component.css'
})

export class UploadPageComponent {

  photo: PhotoEntity  = {
    id: null,
    image: null,
    thumbnail: '',
    description: '',
    uploadDate: null,
    tags: []
  };
  tags: string = '';

  constructor(private photoService: PhotoService, private router: Router) {
  }

  // TODO: Performance: Process the file in chunks? File size limit?
  // TODO: Max File Size handling + user notification\
  // TODO: Only allow submitting if picture is uploaded
  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const reader: FileReader = new FileReader();
      reader.onload = (): void => {
        const arrayBuffer: ArrayBuffer = reader.result as ArrayBuffer;
        const fileBytes: Uint8Array = new Uint8Array(arrayBuffer);
        this.photo.image = Array.from(fileBytes);
      };
      reader.readAsArrayBuffer(file);
    }
  }

  onSubmit() {
    const tagsArray: string[] = this.tags.split(',').map(tag => tag.trim());

    this.photo.tags = tagsArray.map(tagName => ({id: null, name: tagName}));

    this.photoService.addPhoto(this.photo).subscribe(() => {
      this.photo = {
        id: null,
        image: null,
        thumbnail: '',
        description: '',
        uploadDate: null,
        tags: []
      };
      this.tags = '';
    });
    this.router.navigate(['/explore']).then().catch(err => {
      alert(err)
    })
  }
}
