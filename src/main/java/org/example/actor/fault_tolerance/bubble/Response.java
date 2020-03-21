package org.example.actor.fault_tolerance.bubble;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {

    static Behavior<String> create() {
        return Behaviors.receive(String.class).onMessage(String.class, Response::onMessage).build();
    }

    private static Behavior<String> onMessage(String message) {
        System.out.println("MESSAGE RECEIVED:" + message);
        return Behaviors.same();
    }
}
