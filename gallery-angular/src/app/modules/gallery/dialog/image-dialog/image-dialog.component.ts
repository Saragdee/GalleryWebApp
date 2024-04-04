import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {PhotoEntity} from "../../../../core/entity/PhotoEntity";
import {PhotoService} from "../../../../core/service/photo.service";

@Component({
  selector: 'app-image-dialog',
  templateUrl: './image-dialog.component.html',
  styleUrl: './image-dialog.component.css'
})
export class ImageDialogComponent implements OnInit{

  photo: PhotoEntity | undefined;


  ngOnInit() {
    this.viewPhoto(this.data.id);
  }

  constructor(@Inject(MAT_DIALOG_DATA) public data:
                { id: number }, private photoService: PhotoService) {
  }

  viewPhoto(id: number): void {
    this.photoService.getPhoto(id).subscribe(
      photo => {
        this.photo = photo;
      }
    )

  }
}
