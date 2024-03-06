package com.example.gallery.controllers;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;


@RestController
@RequestMapping("/photos")
// TODO: @ExceptionHandler, @ControllerAdvice, @ResponseStatus
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<PhotoDto> createPhoto(@RequestBody PhotoDto photoDto) throws IOException {
        PhotoDto createdPhoto = photoService.uploadPhoto(photoDto);
        return ResponseEntity.ok(createdPhoto);
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
}
