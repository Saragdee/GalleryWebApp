package com.example.gallery.service;

import com.example.gallery.DTO.TagDto;
import com.example.gallery.entities.TagEntity;
import com.example.gallery.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagEntity convertToEntity(TagDto tagDto) {
        TagEntity tagEntity;
        if (tagDto.getId() != null) {
            tagEntity = tagRepository.findById(tagDto.getId())
                    .orElseThrow(() ->
                            new NoSuchElementException("Tag with name " + tagDto.getName() + " not found!"));
        } else {
            tagEntity = tagRepository.findByName(tagDto.getName())
                    .orElse(new TagEntity());
        }
        tagEntity.setName(tagDto.getName());

        return tagEntity;
    }

    public TagEntity createOrUpdateTag(TagDto tagDto) {
        TagEntity tagEntity = convertToEntity(tagDto);

        return tagRepository.save(tagEntity);
    }
}