package org.example.actor.testing.async.echo;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class Ping {
    private final String message;
    private final ActorRef<Pong> replyTo;
}
