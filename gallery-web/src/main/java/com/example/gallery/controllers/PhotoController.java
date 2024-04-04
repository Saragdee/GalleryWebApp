package com.example.gallery.controllers;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.service.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/photos")
// TODO: @ExceptionHandler, @ControllerAdvice, @ResponseStatus
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhoto(@PathVariable Long id) {
        try { // TODO: @ExceptionHandler/@ControllerAdvice
            PhotoDto photo = photoService.getPhotoById(id);
            return ResponseEntity.ok(photo);
        } catch (EntityNotFoundException e) { // FIXME exception handler
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping
    public ResponseEntity<Page<ImageInfoDto>> getAllPhotos(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImageInfoDto> photos = photoService.getAllPhotoImages(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(photos);
    }

    @PostMapping
    public ResponseEntity<PhotoDto> createPhoto(@RequestBody PhotoDto photoDto) {
        photoService.uploadPhoto(photoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            photoService.removePhotoById(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting photo: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ImageInfoDto>> searchPhotos(@RequestParam(required = false) String description,
                                                           @RequestParam(required = false) String tags,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImageInfoDto> photos = photoService.searchPhotos(description, tags, pageable);
        return ResponseEntity.ok(photos);
    }
}
