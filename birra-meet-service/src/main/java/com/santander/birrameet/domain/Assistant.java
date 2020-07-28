package com.santander.birrameet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Assistant {
    private ObjectId userId;
    private boolean assist;

    public void assist() {
        assist = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assistant assistant = (Assistant) o;

        return Objects.equals(userId, assistant.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
