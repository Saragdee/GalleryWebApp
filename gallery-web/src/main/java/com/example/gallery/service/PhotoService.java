package com.example.gallery.service;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.PhotoEntity;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.PhotoRepository;
import com.example.gallery.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

// TODO: Figure out if @Transactional should be used on the whole class or not
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
        PhotoEntity photoEntity = convertToEntity(photoDto);
        PhotoEntity savedPhotoEntity = photoRepository.save(photoEntity);
        return convertToDto(savedPhotoEntity);
    }

    // TODO: @Transactional(readOnly = true)
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
        return "data:image/png;base64," + Base64.encodeBase64String(thumbnailByteArray);
    }

    public List<ImageInfoDto> searchPhotosByDescription(String description) {
        Specification<PhotoEntity> spec = (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
        return photoRepository.findAll(spec).stream().map(ImageInfoDto::of).collect(Collectors.toList());
    }

    public List<ImageInfoDto> searchPhotosByTags(String tagsString) {
        Specification<PhotoEntity> spec = (root, query, criteriaBuilder) -> {
            if (tagsString == null || tagsString.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            query.distinct(true);

            String[] tags = tagsString.split(",");
            List<Predicate> predicates = new ArrayList<>();
            for (String tag : tags) {
                String trimmedTag = tag.trim().toLowerCase();
                if (!trimmedTag.isEmpty()) {
                    Join<PhotoEntity, TagEntity> tagsJoin = root.join("tags", JoinType.LEFT);
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tagsJoin.get("name")), "%" + trimmedTag + "%"));

                }
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        return photoRepository.findAll(spec).stream().map(ImageInfoDto::of).collect(Collectors.toList());
    }

    private Page<ImageInfoDto> convertToDtoPage(Page<Object[]> images) {
        return images.map(ImageInfoDto::of);
    }

    public Page<ImageInfoDto> getAllPhotoImages(Pageable pageable) {
        return convertToDtoPage(photoRepository.pagefindAllImages(pageable));
    }

}
