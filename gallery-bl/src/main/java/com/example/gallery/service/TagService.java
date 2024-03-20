package com.example.gallery.service;

import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagEntity convertToEntity(TagDto dto) {
        return tagRepository.findByName(dto.getName()).orElseGet(() -> new TagEntity(dto.getName()));
    }

}
