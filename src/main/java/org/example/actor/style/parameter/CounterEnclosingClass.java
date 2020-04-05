package org.example.actor.style.parameter;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.TimerScheduler;

public class CounterEnclosingClass {

    public static Behavior<Command> create(String name) {
        return Behaviors.setup(context ->
                Behaviors.withTimers(timers -> new CounterEnclosingClass(name, context, timers).counter(0)));
    }

    private final String name;
    private final ActorContext<Command> context;
    private final TimerScheduler<Command> timers;

    private CounterEnclosingClass(String name, ActorContext<Command> context, TimerScheduler<Command> timers) {
        this.name = name;
        this.context = context;
        this.timers = timers;
    }

    private Behavior<Command> counter(final int n) {
        return Behaviors.receive(Command.class)
                .onMessage(IncrementRepeatedly.class, command -> onIncrementRepeatedly(n, command))
                .onMessageEquals(Increment.INSTANCE, () -> onIncrement(n))
                .onMessage(GetValue.class, command -> onGetValue(n, command))
                .build();
    }

    private Behavior<Command> onIncrementRepeatedly(int n, IncrementRepeatedly command) {
        context.getLog().debug("[{}] Starting repeated increments with internal [{}], current count is [{}]",
                name, command.getInterval(), n);
        return Behaviors.same();
    }

    private Behavior<Command> onIncrement(int n) {
        int newValue = n + 1;
        context.getLog().debug("[{}] Increment counter to [{}]", name, newValue);
        return counter(newValue);
    }

    private Behavior<Command> onGetValue(int n, GetValue command) {
        command.getReplyTo().tell(Value.of(n));
        return Behaviors.same();
    }

}
