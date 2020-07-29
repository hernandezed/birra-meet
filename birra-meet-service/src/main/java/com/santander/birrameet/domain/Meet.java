package com.santander.birrameet.domain;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Document
@Getter
public class Meet {
    @Id
    private ObjectId id;
    private String title;
    private ObjectId creator;
    private Set<Assistant> participants;
    private LocalDateTime date;
    private Location location;
    private Double temperature;

    public Meet(ObjectId id, String title, ObjectId creator, Set<Assistant> participants, LocalDateTime date, Location location) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.participants = Optional.ofNullable(participants).orElse(new HashSet<>());
        this.date = date;
        this.location = location;
    }

    public void addParticipant(ObjectId participantId) {
        participants.add(new Assistant(participantId, false));
    }

    public void removeParticipant(ObjectId participantId) {
        participants.remove(new Assistant(participantId, false));
    }

    public void withCreator(ObjectId creatorId) {
        creator = creatorId;
    }

    public void withTemperature(Double temperature) {
        if (LocalDate.now().equals(date.toLocalDate())) {
            this.temperature = temperature;
        }
    }

}
