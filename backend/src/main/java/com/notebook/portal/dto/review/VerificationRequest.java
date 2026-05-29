package com.notebook.portal.dto.review;

import com.notebook.portal.entity.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {

    @NotNull
    private VerificationStatus status;

    private String comment;
}
