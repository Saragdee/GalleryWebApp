package com.example.gallery.specification;

import com.example.gallery.entities.PhotoEntity;
import com.example.gallery.entities.TagEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

public class SpecHelper {

    public static Specification<PhotoEntity> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    public static Specification<PhotoEntity> hasTags(List<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags != null && !tags.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PhotoEntity> photoEntity = subquery.from(PhotoEntity.class);
                Join<PhotoEntity, TagEntity> tagsJoin = photoEntity.join("tags", JoinType.INNER);
                subquery.select(criteriaBuilder.count(photoEntity)).where(criteriaBuilder.and(criteriaBuilder.equal(photoEntity, root), tagsJoin.get("name").in(tags)));
                query.distinct(true);
                return criteriaBuilder.equal(subquery, tags.size());
            }
            return criteriaBuilder.conjunction();
        };
    }
}
