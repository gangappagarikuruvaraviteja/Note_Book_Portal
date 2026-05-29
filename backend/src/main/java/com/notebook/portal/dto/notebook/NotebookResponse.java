package com.notebook.portal.dto.notebook;

import java.time.Instant;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotebookResponse {
    private Long id;
    private String title;
    private String subject;
    private String semester;
    private String branch;
    private String description;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private boolean verified;
    private double ratingAvg;
    private int ratingCount;
    private long downloadsCount;
    private Set<String> tags;
    private Set<String> categories;
    private String uploadedByName;
    private Instant createdAt;
}
