package com.dimas.dialog.controller.rest.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiDialogMessage {

    private UUID from;
    private UUID to;
    private String text;
    private LocalDateTime publishedAt;

}