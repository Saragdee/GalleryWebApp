package com.example.gallery.service;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.PhotoEntity;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.PhotoRepository;
import com.example.gallery.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    public PhotoDto convertToDto(PhotoEntity photo) {
        if (photo == null) {
            return null;
        }
        PhotoDto dto = new PhotoDto();
        dto.setId(photo.getId());
        dto.setImage(photo.getImage());
        dto.setThumbnail(photo.getThumbnail());
        dto.setDescription(photo.getDescription());
        dto.setUploadDate(photo.getUploadDate());
        // TODO:N+1 SELECT problem
        Set<TagDto> tagDtos = photo.getTags().stream().map(tagEntity -> new TagDto(tagEntity.getId(), tagEntity.getName())).collect(Collectors.toSet());
        dto.setTags(tagDtos);
        return dto;
    }

    private PhotoEntity convertToEntity(PhotoDto dto) {
        PhotoEntity photoEntity;
        if (dto.getId() != null) {
            photoEntity = photoRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("Something very awful passed to convertToEntity"));
        } else {
            photoEntity = new PhotoEntity();
        }
        photoEntity.setImage(dto.getImage());
        photoEntity.setThumbnail(dto.getThumbnail());
        photoEntity.setDescription(dto.getDescription());
        photoEntity.setUploadDate(LocalDate.now());

        if (dto.getTags() != null) {
            Set<TagEntity> tags = dto.getTags().stream().map(tagDto -> {
                // Check if the tag already exists in the database
                return tagRepository.findByName(tagDto.getName()).orElseGet(() -> tagRepository.save(new TagEntity(tagDto.getName())));
            }).collect(Collectors.toSet());
            photoEntity.setTags(tags);
        } else {
            photoEntity.setTags(new HashSet<>());
        }
        return photoEntity;
    }

    public PhotoDto uploadPhoto(PhotoDto photoDto) {
        if (photoDto.getImage() == null) {
            throw new IllegalArgumentException("You did not upload a Image");
        }
        PhotoEntity photoEntity = convertToEntity(photoDto);
        PhotoEntity savedPhotoEntity = photoRepository.save(photoEntity);
        return convertToDto(savedPhotoEntity);
    }

    public PhotoDto getPhotoById(Long id) {
        PhotoEntity photoEntity = photoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Photo not found for ID: " + id));
        return convertToDto(photoEntity);
    }

    public void removePhotoById(Long id) {
        if (!photoRepository.existsById(id)) {
            throw new EntityNotFoundException("Photo not found for ID: " + id);
        }
        photoRepository.deleteById(id);
    }

    public List<ImageInfoDto> getAllPhotoImages() {
        return photoRepository.findAllImages().stream().map(ImageInfoDto::of).collect(Collectors.toList());
    }

    public byte[] createThumbnail(byte[] image, int size) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage thumbnailImage = Scalr.resize(originalImage, size);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage, "png", outputStream);
        return outputStream.toByteArray();
    }

    public String convertThumbnailToBase64(byte[] thumbnailByteArray) {
        return "data:image/png;base64," + new String(Base64.getEncoder().encode(thumbnailByteArray));
    }

    public Page<ImageInfoDto> searchPhotosByDescription(String description, Pageable pageable) {
        Page<Object[]> photoThumbnails = photoRepository.findByDescription(description.trim(), pageable);
        return new PageImpl<>(flattenImageInfoMap(mapPhotoThumbnails(photoThumbnails.getContent())), pageable, photoThumbnails.getTotalElements());
    }

    public Page<ImageInfoDto> searchPhotosByTags(String tagsString, Pageable pageable) {
        List<String> normalizedTags = normalizeTags(tagsString);
        Page<Object[]> photoThumbnails = photoRepository.findPhotoThumbnailsByTags(normalizedTags, pageable);
        return new PageImpl<>(flattenImageInfoMap(mapPhotoThumbnails(photoThumbnails.getContent())), pageable, photoThumbnails.getTotalElements());
    }

    private Map<Long, Set<ImageInfoDto>> mapPhotoThumbnails(List<Object[]> photoThumbnails) {
        Map<Long, Set<ImageInfoDto>> photoThumbnailsMap = new HashMap<>();
        for (Object[] obj : photoThumbnails) {
            Long photoId = (Long) obj[0];
            String thumbnail = (String) obj[1];
            Set<ImageInfoDto> thumbnailSet = photoThumbnailsMap.computeIfAbsent(photoId, k -> new HashSet<>());
            thumbnailSet.add(ImageInfoDto.builder().id(photoId).thumbnail(thumbnail).build());
        }

        return photoThumbnailsMap;
    }

    private List<ImageInfoDto> flattenImageInfoMap(Map<Long, Set<ImageInfoDto>> photoThumbnailsMap) {
        List<ImageInfoDto> result = new ArrayList<>();
        for (Map.Entry<Long, Set<ImageInfoDto>> entry : photoThumbnailsMap.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }


    // can be moved to utils
    private List<String> normalizeTags(String tagsString) {
        return Arrays.stream(tagsString.split(",")).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
    }

    private Page<ImageInfoDto> convertToDtoPage(Page<Object[]> images) {
        return images.map(obj -> ImageInfoDto.builder().id((Long) obj[0]).thumbnail((String) obj[1]).build());
    }

    public Page<ImageInfoDto> getAllPhotoImages(Pageable pageable) {
        return convertToDtoPage(photoRepository.pageFindAllImages(pageable));
    }
}

