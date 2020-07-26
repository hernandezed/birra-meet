package com.santander.birrameet.domain;

import com.santander.birrameet.security.model.User;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Document
@Getter
public class Meet {
    @Id
    private ObjectId id;
    private String title;
    @DBRef
    private User creator;
    @DBRef
    private Set<User> participants;
    private LocalDateTime date;
    private Location location;

    public Meet(ObjectId id, String title, User creator, Set<User> participants, LocalDateTime date, Location location) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.participants = Optional.ofNullable(participants).orElse(new HashSet<>());
        this.date = date;
        this.location = location;
    }

    public void addParticipant(User participantId) {
        participants.add(participantId);
    }

    public void removeParticipant(User participantId) {
        participants.remove(participantId);
    }
}
