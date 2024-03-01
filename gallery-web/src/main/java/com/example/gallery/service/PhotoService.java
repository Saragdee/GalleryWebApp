package com.example.gallery.service;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.PhotoEntity;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.gallery.repository.PhotoRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

// TODO: Figure out if @Transactional should be used on the whole class or not
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    public PhotoDto convertToDto(PhotoEntity photo) {
        if(photo == null) {return null;}

        PhotoDto dto = new PhotoDto();
        dto.setId(photo.getId());
        dto.setImage(photo.getImage());
        dto.setDescription(photo.getDescription());
        dto.setUploadDate(photo.getUploadDate());
        // TODO:N+1 SELECT problem
        Set<TagDto> tagDtos = photo.getTags().stream()
                .map(tagEntity -> new TagDto(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toSet());
        dto.setTags(tagDtos);

        return dto;
    }
    private PhotoEntity convertToEntity(PhotoDto dto) {
        PhotoEntity photo = new PhotoEntity();
        photo.setImage(dto.getImage());
        photo.setDescription(dto.getDescription());
        photo.setUploadDate(LocalDate.now());

        if (dto.getTags() != null) {
            Set<TagEntity> tags = dto.getTags().stream()
                    .map(tagDto -> {
                        // Check if the tag already exists in the database
                        return tagRepository.findByName(tagDto.getName())
                                .orElseGet(() -> tagRepository.save(new TagEntity(tagDto.getName())));
                    })
                    .collect(Collectors.toSet());
            photo.setTags(tags);
        } else {
            photo.setTags(new HashSet<>());
        }
        return photo;
    }
    public PhotoDto createPhoto(PhotoDto photoDto) {
        PhotoEntity photoEntity = convertToEntity(photoDto);
        PhotoEntity savedPhotoEntity = photoRepository.save(photoEntity);
        return convertToDto(savedPhotoEntity);
    }
    // TODO: @Transactional(readOnly = true)
    public PhotoDto getPhotoById(Long id) {
        PhotoEntity photoEntity = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found for ID: " + id));
        return convertToDto(photoEntity);
    }

    public void removePhotoById(Long id){
        if(!photoRepository.existsById(id)){
            throw new EntityNotFoundException("Photo not found for ID: " + id);
        }
        photoRepository.deleteById(id);
    }
}
