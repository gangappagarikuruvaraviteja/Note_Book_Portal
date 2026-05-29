package com.notebook.portal.dto.admin;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminStatsResponse {
    private long totalUsers;
    private long totalUploads;
    private long totalDownloads;
    private String mostDownloadedTitle;
    private long mostDownloadedCount;
    private Map<String, Long> popularSubjects;
}
