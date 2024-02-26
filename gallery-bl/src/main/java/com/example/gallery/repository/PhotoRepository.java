package com.example.gallery.repository;
import com.example.gallery.entities.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    // Custom query methods
    Set<PhotoEntity> findByTags_Name(String tagName);

}
