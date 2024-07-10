package com.dimas.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;


@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdate {

    private UUID id;
    private String text;

}
