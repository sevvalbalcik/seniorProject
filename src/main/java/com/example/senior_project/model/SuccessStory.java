package com.example.senior_project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "success_stories", schema = "sc_seniorproject")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SuccessStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String story;

    @ElementCollection
    @CollectionTable(
        name = "success_story_images",
        schema = "sc_seniorproject",
        joinColumns = @JoinColumn(name = "success_story_id")
    )
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private boolean isApproved;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoryComment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "story_supporters",
        schema = "sc_seniorproject",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> supporters = new ArrayList<>();

    private Integer supportCount;

    @Column(nullable = false)
    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        supportCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 