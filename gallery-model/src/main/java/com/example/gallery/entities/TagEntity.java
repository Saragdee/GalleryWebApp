package com.example.gallery.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")

    private Set<PhotoEntity> photos = new HashSet<>();

    public TagEntity(String name) {
        this.name = name;

    }
}
