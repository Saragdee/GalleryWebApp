package com.example.gallery.viewmodel;

import com.example.gallery.service.PhotoService;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        images = photoService.getAllPhotoImages().stream()
                .map(imageBytes -> {
                    try {
                        byte[] thumbnail = createThumbnail(imageBytes, 100); // Create a thumbnail from the image bytes
                        return convertThumbnailToBase64(thumbnail); // Convert the thumbnail to a Base64 string
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null; // Consider a more robust error handling strategy
                    }
                })// Filter out any null values resulting from errors
                .collect(Collectors.toList());
    }
    private byte[] createThumbnail(byte[] image, int size) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage thumbnailImage = Scalr.resize(originalImage,size);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage,"png", outputStream);
        return outputStream.toByteArray();
    }
    private String convertThumbnailToBase64(byte[] thumbnailByteArray)
    {
        return "data:image/png;base64," + Base64.encodeBase64String(thumbnailByteArray);
    }

}
