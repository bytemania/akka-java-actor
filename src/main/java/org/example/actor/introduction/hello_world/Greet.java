package org.example.actor.introduction.hello_world;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public final class Greet {
    private final String whom;
    private final ActorRef<Greeted> replyTo;
}
