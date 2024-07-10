package com.dimas.dialog.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DialogMessage {

    @EqualsAndHashCode.Include
    private UUID id;
    private String dialogId;//auto generated as hashInvariant(used1+user2), is used for defining shard key, will help to avoid searching in different shards
    private UUID fromUser;
    private UUID toUser;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
