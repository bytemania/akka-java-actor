package org.example.actor.style.parameter;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.TimerScheduler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CounterParameter {

    public static Behavior<Command> create(String name) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> counter(name, context, timers, 0)));
    }

    private static Behavior<Command> counter(
            final String name,
            final ActorContext<Command> context,
            final TimerScheduler<Command> timers,
            final int n) {
        return Behaviors.receive(Command.class)
                .onMessage(IncrementRepeatedly.class, command -> onIncrementRepeatedly(name, context, timers, n, command))
                .onMessageEquals(Increment.INSTANCE, () -> onIncrement(name, context, timers, n))
                .onMessage(GetValue.class, command -> onGetValue(n, command))
                .build();
    }

    private static Behavior<Command> onIncrementRepeatedly(String name, ActorContext<Command> context,
                                                           TimerScheduler<Command> timers, int n,
                                                           IncrementRepeatedly command) {
        context.getLog().debug("[{}] Starting repeated increments with interval [{}], current count is [{}]",
                name, command.getInterval(), n);
        timers.startTimerWithFixedDelay(Increment.INSTANCE, command.getInterval());
        return Behaviors.same();
    }

    private static Behavior<Command> onIncrement(String name, ActorContext<Command> context,
                                                 TimerScheduler<Command> timers, int n) {
        int newValue = n + 1;
        context.getLog().debug("[{}] Increment counter to [{}]", name, newValue);
        return counter(name, context, timers, newValue);
    }

    private static Behavior<Command> onGetValue(int n, GetValue command) {
        command.getReplyTo().tell(Value.of(n));
        return Behaviors.same();
    }

}
