package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
final class Busy extends Answer {

    static Busy of(ActorRef<ChopstickCommand> chopstick) {
        return new Busy(chopstick);
    }

    private Busy(ActorRef<ChopstickCommand> chopstick) {
        super(chopstick);
    }

    @Override
    boolean isTaken() {
        return false;
    }

    @Override
    boolean isBusy() {
        return true;
    }
}
