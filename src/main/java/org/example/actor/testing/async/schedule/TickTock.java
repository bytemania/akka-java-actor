package org.example.actor.testing.async.schedule;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class TickTock {

    static Behavior<Tick> create(Duration duration, ActorRef<Tock> replyTo) {
        return Behaviors.withTimers(timer -> {
            timer.startSingleTimer(Tick.INSTANCE, duration);
            return Behaviors.receiveMessage(tick -> {
                 replyTo.tell(Tock.INSTANCE);
                 return Behaviors.same();
            });
        });
    }

}
