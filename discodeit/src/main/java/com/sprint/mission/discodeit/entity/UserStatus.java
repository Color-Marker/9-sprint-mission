package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    boolean isOnline;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.isOnline = true;
    }

    public boolean stillOnline(){
        if(updatedAt.isAfter(Instant.now().minusSeconds(300))){
            isOnline = true;
        }
        else{
            isOnline = false;
        }
        return isOnline;
    }

    public void update() {
        this.isOnline = true;
    }
}
