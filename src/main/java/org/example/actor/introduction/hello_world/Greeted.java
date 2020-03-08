package org.example.actor.introduction.hello_world;

import akka.actor.typed.ActorRef;
import lombok.*;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public final class Greeted {
    private final String whom;
    private final ActorRef<Greet> from;
}
