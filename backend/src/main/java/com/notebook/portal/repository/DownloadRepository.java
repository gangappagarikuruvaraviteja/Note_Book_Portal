package com.notebook.portal.repository;

import com.notebook.portal.entity.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DownloadRepository extends JpaRepository<Download, Long> {

    @Query("select count(d) from Download d")
    long totalDownloads();
}
