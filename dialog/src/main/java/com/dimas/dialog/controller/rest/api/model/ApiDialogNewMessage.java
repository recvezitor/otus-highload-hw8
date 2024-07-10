package com.dimas.dialog.controller.rest.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class ApiDialogNewMessage {

    private @NotBlank String text;

}