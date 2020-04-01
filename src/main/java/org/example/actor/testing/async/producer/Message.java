package org.example.actor.testing.async.producer;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class Message {
    private final int i;
    private final ActorRef<Integer> replyTo;
}
