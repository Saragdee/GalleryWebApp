package com.example.gallery.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder // TODO: superbuilder
public class PhotoDto {
    private Long id;
    private byte[] image;
    private String thumbnail;
    private String description;
    private LocalDate uploadDate;
    private Set<TagDto> tags;
}
