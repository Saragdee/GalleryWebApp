package com.example.gallery.service;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.entities.PhotoEntity;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.PhotoRepository;
import com.example.gallery.repository.TagRepository;
import com.example.gallery.specification.SpecHelper;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TagService tagService;

    public PhotoEntity convertToEntity(PhotoDto dto) {
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

        photoEntity.setTags(mapTagsToEntities(dto));
        return photoEntity;
    }

    private Set<TagEntity> mapTagsToEntities(PhotoDto photoDto) {
        return photoDto.getTags().stream()
                .map(tagService::createOrUpdateTag)
                .collect(Collectors.toSet());
    }

    public void uploadPhoto(PhotoDto photoDto) {
        if (photoDto.getImage() == null) {
            throw new IllegalArgumentException("You did not upload a Image");
        }
        PhotoEntity photoEntity = convertToEntity(photoDto);
        photoRepository.save(photoEntity);
    }

    @Transactional // without Transactional, get LazyInitializationException. For now this seems like simplest solution.
    public PhotoDto getPhotoById(Long id) {
        return PhotoDto.of(photoRepository.findByIdAndFetchTagsEagerly(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Image with id " + id + " does not exist")));
    }

    public void removePhotoById(Long id) {
        if (!photoRepository.existsById(id)) {
            throw new EntityNotFoundException("Photo not found for ID: " + id);
        }
        photoRepository.deleteById(id);
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

    public Page<ImageInfoDto> searchPhotos(String description, String tagsString, Pageable pageable) {
        Specification<PhotoEntity> descSpec = SpecHelper.hasDescription(description != null ? description.trim() : null);
        List<String> normalizedTags = tagsString != null ? normalizeTags(tagsString) : null;
        Specification<PhotoEntity> tagsSpec = SpecHelper.hasTags(normalizedTags);
        Specification<PhotoEntity> combinedSpec = Specification.where(descSpec).and(tagsSpec);
        return photoRepository.findAll(combinedSpec, pageable).map(ImageInfoDto::of);
    }

    // can be moved to utils
    private List<String> normalizeTags(String tagsString) {
        return Arrays.stream(tagsString.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private Page<ImageInfoDto> convertToDtoPage(Page<Object[]> images) {
        return images.map(obj -> ImageInfoDto.builder().id((Long) obj[0]).thumbnail((String) obj[1]).build());
    }

    public Page<ImageInfoDto> getAllPhotoImages(Pageable pageable) {
        return convertToDtoPage(photoRepository.pageFindAllImages(pageable));
    }
}

