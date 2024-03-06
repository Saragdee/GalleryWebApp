package com.example.gallery.viewmodel;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PhotoVm {
    @WireVariable
    private PhotoService photoService;
    private PhotoDto photoDto;
    private String description;
    //private LocalDate uploadDate;
    private String tagsAsString;

    @Init
    public void init() {
        photoDto = new PhotoDto(null, null, null, null, null, null);
        tagsAsString = "";
    }

    @Command
    public void submit() throws IOException {
        photoDto.setTags(convertStringToSet(tagsAsString));
        photoService.uploadPhoto(photoDto);
    }

    @Command
    public void uploadImage(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event) {
        Media media = event.getMedia();
        if (media != null) {
            if (media.isBinary()) {
                byte[] originalImageData = media.getByteData();
                try {
                    byte[] thumbnail = photoService.createThumbnail(originalImageData, 100);  // Assuming 100 is the desired size
                    String base64Thumbnail = photoService.convertThumbnailToBase64(thumbnail);
                    photoDto.setImage(originalImageData);
                    photoDto.setThumbnail(base64Thumbnail);
                } catch (IOException e) {
                    System.out.println("Image upload kaput");
                }
            } else {
                System.out.println("The media of image upload was non-binary or there is an error");
            }
        } else {
            System.out.println("No file uploaded or error in uploading file");
        }
    }

    // TODO: Move to a utils package
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



