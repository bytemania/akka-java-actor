package org.example.actor.style.funvsoo;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterFunc {

    private static Behavior<Command> counter(final ActorContext<Command> context, final int n) {
        return Behaviors.receive(Command.class)
                .onMessageEquals(Increment.INSTANCE, () -> onIncrement(context, n))
                .onMessage(GetValue.class, command -> onGetValue(n, command))
                .build();
    }

    private static Behavior<Command> onIncrement(ActorContext<Command> context, int n) {
        int newValue = n + 1;
        context.getLog().debug("Increment counter to [{}]", newValue);
        return counter(context, newValue);
    }

    private static Behavior<Command> onGetValue(int n, GetValue command) {
        command.getReplyTo().tell(Value.of(n));
        return Behaviors.same();
    }
}
