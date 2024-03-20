package com.example.gallery.repository;

import com.example.gallery.entities.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long>, JpaSpecificationExecutor<PhotoEntity> {
    Set<PhotoEntity> findByTags_Name(String tagName);

    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p")
    List<Object[]> findAllImages();
    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p")
    Page<Object[]> pageFindAllImages(Pageable pageable);

    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p JOIN p.tags t WHERE t.name IN :normalizedTags")
    Page<Object[]> findPhotoThumbnailsByTags(List<String> normalizedTags, Pageable pageable);

    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%',:description, '%'))")
    Page<Object[]> findByDescription(String description, Pageable pageable);


}
