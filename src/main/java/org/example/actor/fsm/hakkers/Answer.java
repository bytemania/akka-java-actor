package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
abstract class Answer {
    private final ActorRef<ChopstickCommand> chopstick;

    abstract boolean isTaken();
    abstract boolean isBusy();
}
