package com.notebook.portal.service;

import com.notebook.portal.dto.admin.AdminStatsResponse;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.repository.DownloadRepository;
import com.notebook.portal.repository.NotebookRepository;
import com.notebook.portal.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final NotebookRepository notebookRepository;
    private final DownloadRepository downloadRepository;

    public AdminStatsResponse getStats() {
        long totalUsers = userRepository.count();
        long totalUploads = notebookRepository.count();
        long totalDownloads = downloadRepository.totalDownloads();

        Notebook mostDownloaded = notebookRepository.findAll().stream()
            .max((a, b) -> Long.compare(a.getDownloadsCount(), b.getDownloadsCount()))
            .orElse(null);

        Map<String, Long> popularSubjects = new HashMap<>();
        notebookRepository.findAll().forEach(notebook -> {
            popularSubjects.put(
                notebook.getSubject(),
                popularSubjects.getOrDefault(notebook.getSubject(), 0L) + 1
            );
        });

        return AdminStatsResponse.builder()
            .totalUsers(totalUsers)
            .totalUploads(totalUploads)
            .totalDownloads(totalDownloads)
            .mostDownloadedTitle(mostDownloaded == null ? null : mostDownloaded.getTitle())
            .mostDownloadedCount(mostDownloaded == null ? 0 : mostDownloaded.getDownloadsCount())
            .popularSubjects(popularSubjects)
            .build();
    }
}
