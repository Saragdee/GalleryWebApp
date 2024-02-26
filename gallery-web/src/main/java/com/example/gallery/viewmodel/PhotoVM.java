package com.example.gallery.viewmodel;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
@Setter
@NoArgsConstructor(force = true)
public class PhotoVM {
    private String description;
    private LocalDate uploadDate;
    private String tagsAsString; // Comma-separated tagsl
    // Assuming an instance of PhotoService is injected or created here

    private final PhotoService photoService;
    public PhotoVM(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Init
    public void init() {
        // Initialize ViewModel properties if needed
    }

    @Command
    @NotifyChange({"description", "uploadDate", "tagsAsString"})
    public void submit() {
        // Convert tagsAsString to Set<TagDto>
        Set<TagDto> tags = Arrays.stream(tagsAsString.split(","))
                .map(String::trim)
                .map(TagDto::new)
                .collect(Collectors.toSet());

        // Create PhotoDto
        PhotoDto photoDto = new PhotoDto(null, description, LocalDate.now(), tags);

        // Call service to persist photo
        photoService.createPhoto(photoDto);

        // Clear the fields after submission
        description = "";
        uploadDate = null;
        tagsAsString = "";
    }

    // Getters and Setters for the properties
}
