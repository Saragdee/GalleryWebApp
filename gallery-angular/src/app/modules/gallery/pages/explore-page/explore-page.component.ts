import {Component, OnInit} from '@angular/core';
import {ImageInfoEntity} from "../../../../core/entity/ImageInfoEntity";
import {PhotoEntity} from "../../../../core/entity/PhotoEntity";
import {PhotoService} from "../../../../core/service/photo.service";
import {Page} from "../../../../core/entity/page";

@Component({
  selector: 'app-explore-page',
  templateUrl: './explore-page.component.html',
  styleUrl: './explore-page.component.css'
})
export class ExplorePageComponent implements OnInit {
  public photos: ImageInfoEntity[] | undefined;
  public photo: PhotoEntity | undefined;
  public page = 0;
  public size = 12;
  public selectedImageTags = '';
  public searchByDescription = '';
  public searchByTags = '';
  public filterByDescription = '';
  public filterByTags = '';


  constructor(private photoService: PhotoService) {
  }

  ngOnInit() {
    this.fetchPage(this.page);
  }

  fetchPage(page: number): void {
    if (this.filterByDescription || this.filterByTags) {
      this.photoService.searchPhotos(
        this.filterByDescription,
        this.filterByTags,
        page,
        this.size
      ).subscribe((response: Page) => {
          this.photos = response.content
          console.log(response.size)
        }
      );
    } else {
      this.photoService.getAllPhotoImages(page, this.size).subscribe((response: Page) => {
          this.photos = response.content
          console.log(response.totalElements)
          console.log(response.totalPages)
        }
      );
    }
  }


  doSearch()
    :
    void {
    if (this.searchByDescription.trim() || this.searchByTags.trim()
    ) {
      this.filterByDescription = this.searchByDescription;
      this.filterByTags = this.searchByTags;
    } else {
      this.filterByDescription = '';
      this.filterByTags = '';
    }
    this.fetchPage(0);
  }

  prevPage()
    :
    void {
    if (this.page > 0
    ) {
      this.page--;
      this.fetchPage(this.page);
    }
  }

  nextPage()
    :
    void {
    this.page++;
    this.fetchPage(this.page);
  }
}

