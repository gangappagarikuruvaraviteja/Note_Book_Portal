package com.notebook.portal.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notebooks")
@Getter
@Setter
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String branch;

    @Column(length = 2000)
    private String description;

    @Lob
    private String contentText;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private double ratingAvg;

    @Column(nullable = false)
    private int ratingCount;

    @Column(nullable = false)
    private long downloadsCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notebook_tags", joinColumns = @JoinColumn(name = "notebook_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "notebook_categories",
        joinColumns = @JoinColumn(name = "notebook_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.verified = false;
        this.ratingAvg = 0.0;
        this.ratingCount = 0;
        this.downloadsCount = 0;
    }
}
