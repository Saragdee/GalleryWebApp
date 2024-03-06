package com.example.gallery.service;

import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagEntity convertToEntity(TagDto dto) {
        return tagRepository.findByName(dto.getName())
                .orElseGet(() -> new TagEntity(dto.getName()));
    }

    public Set<TagEntity> getTags(Collection<TagDto> crap) {
        return crap.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());

    }
}
