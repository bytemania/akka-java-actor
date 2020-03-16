package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class LeaveHome implements Command {
    private final String who;
    private final ActorRef<ReadyToLeaveHome> respondTo;
}
