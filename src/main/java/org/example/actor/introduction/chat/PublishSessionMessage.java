package org.example.actor.introduction.chat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class PublishSessionMessage implements RoomCommand {
    private final String screenName;
    private final String message;
}
