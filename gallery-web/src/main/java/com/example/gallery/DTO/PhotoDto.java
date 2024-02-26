package com.example.gallery.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // TODO: superbuilder
public class PhotoDto {
    private Long id;
    private String description;
    private LocalDate uploadDate;
    private Set<TagDto> tags;
}
