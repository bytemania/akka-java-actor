package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
final class Taken extends Answer {

    public static Taken of(ActorRef<ChopstickCommand> chopstick) {
        return new Taken(chopstick);
    }

    private Taken(ActorRef<ChopstickCommand> chopstick) {
        super(chopstick);
    }

    @Override
    boolean isTaken() {
        return true;
    }

    @Override
    boolean isBusy() {
        return false;
    }
}
