package com.example.gallery.viewmodel;

import com.example.gallery.DTO.PhotoDto;
import com.example.gallery.service.PhotoService;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@VariableResolver(DelegatingVariableResolver.class)
public class ExploreVM {
    @WireVariable
    private PhotoService photoService;
    private List<String> images;

    @Init
    public void init() {
        images = photoService.getAllPhotos().stream()
                .map(this::encodeImageToBase64)
                .collect(Collectors.toList());
    }

    private String encodeImageToBase64(PhotoDto photo) {
        byte[] imageData = photo.getImage();
        return "data:image/*;base64," + Base64.encodeBase64String(imageData);
    }
}
