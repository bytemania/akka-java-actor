package org.example.actor.fault_tolerance.functional;

import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Counter {

    public static Behavior<Command> create() {
        return Behaviors.supervise(counter(1)).onFailure(SupervisorStrategy.restart());
    }

    private static Behavior<Command> counter(int currentValue) {
        System.out.println("COUNTER CREATED WITH VALUE " + currentValue);
        return Behaviors.receive(Command.class)
                .onMessage(Increase.class, o -> onIncrease(currentValue))
                .onMessage(Get.class, command -> onGet(currentValue, command))
                .onMessage(Error.class, e -> onError())
                .build();
    }

    private static Behavior<Command> onIncrease(int currentValue) {
        return counter(currentValue + 1);
    }

    private static Behavior<Command> onGet(int currentValue, Get command) {
        command.getReplyTo().tell(Got.of(currentValue));
        return Behaviors.same();
    }

    private static Behavior<Command> onError() {
        throw new RuntimeException("REAL BAD ERROR");
    }

}
