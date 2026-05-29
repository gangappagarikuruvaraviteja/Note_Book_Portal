package com.notebook.portal.dto.notebook;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotebookCreateRequest {

    @NotBlank
    private String title;

    private String subject;

    private String semester;

    private String branch;

    private String description;

    private String tags;

    private String categories;
}
