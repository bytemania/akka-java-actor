package org.example.actor.style.parameter;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterLessParameters {

    public static Behavior<Command> create(String name) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> counter(Setup.of(name, context, timers), 0)));
    }

    private static Behavior<Command> counter(final Setup setup, final int n) {
        return Behaviors.receive(Command.class)
                .onMessage(IncrementRepeatedly.class, command -> onIncrementRepeatedly(setup, n, command))
                .onMessageEquals(Increment.INSTANCE, () -> onIncrement(setup, n))
                .onMessage(GetValue.class, command -> onGetValue(n, command))
                .build();
    }

    private static Behavior<Command> onIncrementRepeatedly(Setup setup, int n, IncrementRepeatedly command) {
        setup.getContext().getLog().debug("[{}] Starting repeated increments with interval [{}], current count is [{}]",
                setup.getName(), command.getInterval(), n);
        setup.getTimers().startTimerWithFixedDelay(Increment.INSTANCE, command.getInterval());
        return Behaviors.same();
    }

    private static Behavior<Command> onIncrement(Setup setup, int n) {
        int newValue = n + 1;
        setup.getContext().getLog().debug("[{}] Incremented counter to [{}]", setup.getName(), newValue);
        return counter(setup, newValue);
    }

    private static Behavior<Command> onGetValue(int n, GetValue command) {
        command.getReplyTo().tell(Value.of(n));
        return Behaviors.same();
    }

}
