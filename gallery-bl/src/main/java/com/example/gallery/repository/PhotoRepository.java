package com.example.gallery.repository;

import com.example.gallery.entities.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long>, JpaSpecificationExecutor<PhotoEntity> {
    // Custom query methods
    Set<PhotoEntity> findByTags_Name(String tagName);

    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p")
    List<Object[]> findAllImages();
}
