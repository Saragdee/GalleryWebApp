package com.example.gallery.repository;

import com.example.gallery.entities.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long>, JpaSpecificationExecutor<PhotoEntity> {
    @Query("SELECT p.id, p.thumbnail FROM PhotoEntity p")
    Page<Object[]> pageFindAllImages(Pageable pageable);

    @Query("SELECT p FROM PhotoEntity p JOIN FETCH p.tags WHERE p.id = :id")
    Optional<PhotoEntity> findByIdAndFetchTagsEagerly(@Param("id") Long id);
}
