package org.example.actor.lifecycle;


import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class SpawnJobCustom implements Command {
    private final String name;
    private final ActorRef<JobDone> replyToWhenDone;
}
