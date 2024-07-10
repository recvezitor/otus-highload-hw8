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
public class FriendRequestCreate {
    private UUID userId;
    private UUID friendId;

}
