import {Component, OnInit} from '@angular/core';
import {ImageInfoEntity} from "../../../../core/entity/ImageInfoEntity";
import {PhotoEntity} from "../../../../core/entity/PhotoEntity";
import {PhotoService} from "../../../../core/service/photo.service";
import {Page} from "../../../../core/entity/page";
import {ImageDialogComponent} from "../../dialog/image-dialog/image-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-explore-page', templateUrl: './explore-page.component.html', styleUrl: './explore-page.component.css'
})
export class ExplorePageComponent implements OnInit {
  public photos: ImageInfoEntity[] | undefined;
  public photo: PhotoEntity | undefined;

  public searchByDescription = '';
  public searchByTags = '';
  public filterByDescription = '';
  public filterByTags = '';

  //
  public page: number = 0;
  public pageSize: number = 12;
  public totalElements: number = 0;

  constructor(private photoService: PhotoService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.fetchPage(this.page);
  }

  fetchPage(page: number): void {
    if (this.filterByDescription || this.filterByTags) {
      this.photoService.searchPhotos(this.filterByDescription, this.filterByTags, page, this.pageSize)
        .subscribe((response: Page) => {
          this.photos = response.content
          // console.log(response.size)
          this.totalElements = response.totalElements
        });
    } else {
      this.photoService.getAllPhotoImages(page, this.pageSize)
        .subscribe((response: Page) => {
          this.photos = response.content
          // console.log(response.totalElements)
          this.totalElements = response.totalElements
          // console.log(response.totalPages)ar
        });
    }
  }

  doSearch(): void {
    if (this.searchByDescription.trim() || this.searchByTags.trim()) {
      this.filterByDescription = this.searchByDescription;
      this.filterByTags = this.searchByTags;
    } else {
      this.filterByDescription = '';
      this.filterByTags = '';
    }
    this.fetchPage(0);
  }

  onPageChange(event: PageEvent) {
    const page = event.pageIndex;
    this.fetchPage(page);
  }

  openDialog(photoId: number): void {
    this.dialog.open(ImageDialogComponent, {
      data: {
        id: photoId
      }
    });
  }

}

