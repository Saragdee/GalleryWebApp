package com.example.gallery.DTO;


import com.example.gallery.entities.PhotoEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageInfoDto {
    private long id;
    private String thumbnail;

    public static ImageInfoDto of(Object[] image) {
        return ImageInfoDto.builder()
                .id((Long) image[0])
                .thumbnail((String) image[1])
                .build();
    }

    public static ImageInfoDto of(PhotoEntity image) {
        return ImageInfoDto.builder()
                .id(image.getId())
                .thumbnail(image.getThumbnail())
                .build();
    }
}

