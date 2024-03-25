package com.example.gallery.DTO;

import com.example.gallery.entities.PhotoEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDto {
    private Long id;
    private byte[] image;
    private String thumbnail;
    private String description;
    private LocalDate uploadDate;
    private Set<TagDto> tags;

    public static PhotoDto of(PhotoEntity entity) {
        return PhotoDto.builder()
                .id(entity.getId())
                .image(entity.getImage())
                .thumbnail((entity.getThumbnail()))
                .description(entity.getDescription())
                .uploadDate(entity.getUploadDate())
                .tags(mapTagsToDto(entity))
                .build();
    }

    private static Set<TagDto> mapTagsToDto(PhotoEntity entity) {
        return entity.getTags().stream()
                .map(TagDto::of)
                .collect(Collectors.toSet());
    }
}