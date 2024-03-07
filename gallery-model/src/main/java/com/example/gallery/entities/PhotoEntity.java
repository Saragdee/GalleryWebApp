package com.example.gallery.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "photos")
@NoArgsConstructor
@AllArgsConstructor
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "image")
    private byte[] image;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Column(name = "description")
    private String description;
    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    //@Column(name = "tags")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "photo_tags", joinColumns = @JoinColumn(name = "photo_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))

    // Cascade? -- kai ivyksta veiksmas su photo, pvz delete, tegul ivyksta ir associated entity
    // Fetchtype?
    // EntityGraph?
    // @Fetch SUBSELECT?
    private Set<TagEntity> tags = new HashSet<>(); //
}

