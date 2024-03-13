package com.example.gallery.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TagDto {
    private Long id;
    private String name;

    public TagDto(String name) {
        this.name = name;
    }
}
