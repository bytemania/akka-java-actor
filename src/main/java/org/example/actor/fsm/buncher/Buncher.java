package org.example.actor.fsm.buncher;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

abstract class Buncher {

    static Behavior<Event> create() {
        return uninitialized();
    }

    private static Behavior<Event> uninitialized() {
        return Behaviors.receive(Event.class)
                .onMessage(SetTarget.class, message -> idle(Todo.of(message.getRef(), Collections.emptyList())))
                .build();
    }

    private static Behavior<Event> idle(Todo data) {
        return Behaviors.receive(Event.class)
                .onMessage(Queue.class, message -> active(data.addElement(message)))
                .build();
    }

    private static Behavior<Event> active(Todo data) {
        return Behaviors.withTimers(timers -> {
            timers.startSingleTimer("Timeout", Timeout.INSTANCE, Duration.ofSeconds(1));
            return Behaviors.receive(Event.class)
                    .onMessage(Queue.class, message -> active(data.addElement(message)))
                    .onMessage(Flush.class, message -> activeOnFlushTimeout(data))
                    .onMessage(Timeout.class, message -> activeOnFlushTimeout(data))
                    .build();
        });
    }

    private static Behavior<Event> activeOnFlushTimeout(Todo data) {
        data.getTarget().tell(Batch.of(data.getQueue()));
        return idle(data.copy(new ArrayList<>()));
    }

}
