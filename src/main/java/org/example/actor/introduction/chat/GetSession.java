package org.example.actor.introduction.chat;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class GetSession implements RoomCommand {
    private final String screenName;
    private final ActorRef<SessionEvent> replyTo;

}
