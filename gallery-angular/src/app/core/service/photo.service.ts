import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PhotoEntity} from "../entity/PhotoEntity";
import {Page} from "../entity/page";

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

  private apiServerUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) {
  }

  getAllPhotoImages(page: number, size: number): Observable<Page> {

    return this.http.get<Page>(`${this.apiServerUrl}/photos?page=${page}&size=${size}`);
  }

  searchPhotos(description: string, tags: string, page: number, size: number): Observable<Page> {    const params = new HttpParams()
      .set('description', description)
      .set('tags', tags)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page>(`${this.apiServerUrl}/photos/search`, { params });
  }
  public getPhoto(id: number): Observable<PhotoEntity>{
    return this.http.get<PhotoEntity>(`${this.apiServerUrl}/photos/${id}`)
  }

  public addPhoto(photo: PhotoEntity): Observable<PhotoEntity> {
    return this.http.post<PhotoEntity>(`${this.apiServerUrl}/photos/`, photo);
  }

  public updatePhoto(photo: PhotoEntity): Observable<PhotoEntity> {
    return this.http.post<PhotoEntity>(`${this.apiServerUrl}/photos/update`, photo);
  }

  public deletePhoto(photoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/photos/delete/${photoId}`);
  }

}
