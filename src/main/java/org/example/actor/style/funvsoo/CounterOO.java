package org.example.actor.style.funvsoo;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class CounterOO extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(CounterOO::new);
    }

    private int n;

    private CounterOO(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(Increment.INSTANCE, this::onIncrement)
                .onMessage(GetValue.class, this::onGetValue)
                .build();
    }

    private Behavior<Command> onIncrement() {
        n++;
        getContext().getLog().debug("Increment counter to [{}]", n);
        return this;
    }

    private Behavior<Command> onGetValue(GetValue command) {
        command.getReplyTo().tell(Value.of(n));
        return this;
    }
}
