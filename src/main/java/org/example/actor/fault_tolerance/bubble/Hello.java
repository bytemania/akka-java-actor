package org.example.actor.fault_tolerance.bubble;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class Hello implements Command {
    private final String text;
    private final ActorRef<String> replyTo;
}
