package com.example.gallery.viewmodel;

import com.example.gallery.DTO.ImageInfoDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

@Getter
@VariableResolver(DelegatingVariableResolver.class)
public class ExploreVm {
    @WireVariable
    private PhotoService photoService;
    private List<ImageInfoDto> images;
    @Setter
    private String searchDescription;

    @Init
    public void init() {
        images = photoService.getAllPhotoImages();
    }

    @Command
    @NotifyChange("images")
    public void removeImage(@BindingParam("id") Long id) {
        photoService.removePhotoById(id);
        //    images = photoService.getAllPhotoImages();
    }

    @Command
    @NotifyChange("images")
    public void search() {
        images = photoService.searchPhotosByDescription(searchDescription);
    }

}
