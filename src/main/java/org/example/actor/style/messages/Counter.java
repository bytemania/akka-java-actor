package org.example.actor.style.messages;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

class Counter extends AbstractBehavior<CounterProtocol.Message> {

    private interface PrivateCommand extends CounterProtocol.Message {}

    private enum Tick implements PrivateCommand {
        INSTANCE
    }

    public static Behavior<CounterProtocol.Command> create(String name, Duration tickInterval) {
        return Behaviors.setup((ActorContext<CounterProtocol.Message> context) -> Behaviors.withTimers(timers -> {
            timers.startTimerWithFixedDelay(Tick.INSTANCE, tickInterval);
            return new Counter(name, context);
        })).narrow();
    }

    private final String name;
    private int count;

    private Counter(String name, ActorContext<CounterProtocol.Message> context) {
        super(context);
        this.name = name;
    }

    @Override
    public Receive<CounterProtocol.Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(CounterProtocol.Increment.class, notUsed -> onIncrement())
                .onMessageEquals(Tick.INSTANCE, this::onTick)
                .onMessage(CounterProtocol.GetValue.class, this::onGetValue)
                .build();
    }

    private Behavior<CounterProtocol.Message> onIncrement() {
        count++;
        getContext().getLog().debug("[{}] Increment counter to [{}]", name, count);
        return this;
    }

    private Behavior<CounterProtocol.Message> onTick() {
        count++;
        getContext().getLog().debug("[{}] Incremented counter by background tick to [{}]", name, count);
        return this;
    }

    private Behavior<CounterProtocol.Message> onGetValue(CounterProtocol.GetValue command) {
        command.getReplyTo().tell(CounterProtocol.Value.of(count));
        return this;
    }

}
