package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class Take implements ChopstickCommand {
    private final ActorRef<Answer> hakker;
}
