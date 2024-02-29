package com.example.gallery.viewmodel;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PhotoVM {
    @WireVariable
    private PhotoService photoService;
    private PhotoDto photoDto;
    private String description;
//    private LocalDate uploadDate;
    private String tagsAsString;

    @Init
    public void init() {
    photoDto = new PhotoDto(null, "",  null, null);
    tagsAsString = "";

    }

    @Command
    @NotifyChange({"description", "uploadDate", "tagsAsString"})
    public void submit()
    {
        photoDto.setTags(convertStringToSet(tagsAsString));
        photoService.createPhoto(photoDto);
    }

    private Set<TagDto> convertStringToSet(String tags) {
        if (tags.isEmpty()) {
            return new HashSet<>();
        }

        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .map(tag -> new TagDto(null, tag))
                .collect(Collectors.toSet());
    }
}



