package com.example.gallery.viewmodel;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.DTO.TagDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zkoss.bind.annotation.BindingParam;
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
@VariableResolver(DelegatingVariableResolver.class)
public class ExploreVm {
    @WireVariable
    private PhotoService photoService;
    private Page<ImageInfoDto> imagesPage;
    private PhotoDto selectedImage;
    @Setter
    private String selectedImageTags;

    @Setter
    private String searchByDescription = "";
    @Setter
    private String searchByTags = "";

    private String fullImage;
    private static final int PAGE_SIZE = 12;
    private String filterByDescription = "";
    private String filterByTags = "";

    @Init
    public void init() {
        fetchPage(0);
        selectedImageTags = "";
    }

    @Command
    @NotifyChange("imagesPage")
    private void fetchPage(int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, PAGE_SIZE);
        if (!filterByDescription.isEmpty() || !filterByTags.isEmpty()) {
            imagesPage = photoService.searchPhotos(filterByDescription, filterByTags, pageable);
        } else {
            imagesPage = photoService.getAllPhotoImages(pageable);
        }
        System.out.println(filterByTags);
        System.out.println(filterByDescription);
    }

    @Command
    @NotifyChange({"imagesPage", "searchByDescription", "searchByTags"})
    public void doSearch() {
        if (!searchByDescription.isEmpty() || !searchByTags.isEmpty()) {
            filterByDescription = searchByDescription;
            filterByTags = searchByTags;
        }
        else {
            filterByDescription ="";
            filterByTags="";
        }
        fetchPage(0);

    }

    @Command
    @NotifyChange("imagesPage")
    public void prevPage() {
        if (imagesPage.hasPrevious()) {
            fetchPage(imagesPage.getNumber() - 1);
        }
    }
    @Command
    @NotifyChange("imagesPage")
    public void nextPage() {
        if (imagesPage.hasNext()) {
            fetchPage(imagesPage.getNumber() + 1);
        }
    }

    // ------------------------------------
    @Command
    @NotifyChange({"selectedImage", "selectedImageTags"})
    public void doEditImage(@BindingParam("id") Long id) {
        selectedImage = photoService.getPhotoById(id);
        if (selectedImage != null) {
            selectedImageTags = convertSetToString(selectedImage.getTags());
        } else {
            // TODO
            System.out.println("Error: selectedImage is null. Photo with the given id does not exist.");
        }
    }

    public static String convertSetToString(Set<TagDto> tags) {
        if (tags.isEmpty()) {
            return "";
        }
        return tags.stream().map(TagDto::getName).collect(Collectors.joining(", "));
    }
    // ------------------------------------

    @Command
    public void saveImage() {
        selectedImage.setTags(convertStringToSet(selectedImageTags));
        photoService.uploadPhoto(selectedImage);
    }

    @Command
    @NotifyChange("imagesPage")
    public void doRemoveImage(@BindingParam("id") Long id) {
        photoService.removePhotoById(id);
    }

    @Command
    @NotifyChange("fullImage")
    public void getFullImage(@BindingParam("id") Long id) {
        System.out.println(id);
        fullImage = photoService.convertThumbnailToBase64(photoService.getPhotoById(id).getImage());
    }

    //TODO: can be moved to utils
    private Set<TagDto> convertStringToSet(String tags) {
        if (tags.isEmpty()) {
            return new HashSet<>();
        }

        return Arrays.stream(tags.split(",")).map(String::trim).filter(tag -> !tag.isEmpty()).map(tag -> new TagDto(null, tag)).collect(Collectors.toSet());
    }
}
