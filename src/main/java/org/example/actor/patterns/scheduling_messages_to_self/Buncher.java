package org.example.actor.patterns.scheduling_messages_to_self;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class Buncher {
    private static final Object TIMER_KEY = new Object();

    public static Behavior<Command> create(ActorRef<Batch> target, Duration after, int maxSize) {
        return Behaviors.withTimers(timers -> new Buncher(timers, target, after, maxSize).idle());
    }

    private final TimerScheduler<Command> timers;
    private final ActorRef<Batch> target;
    private final Duration after;
    private final int maxSize;

    private Buncher(TimerScheduler<Command> timers, ActorRef<Batch> target, Duration after, int maxSize) {
        this.timers = timers;
        this.target = target;
        this.after = after;
        this.maxSize = maxSize;
    }

    private Behavior<Command> idle() {
        return Behaviors.receive(Command.class)
                .onMessage(Command.class, this::onIdleCommand)
                .build();
    }

    private Behavior<Command> onIdleCommand(Command message) {
        timers.startSingleTimer(TIMER_KEY, Timeout.INSTANCE, after);
        return Behaviors.setup(context -> {
            context.getLog().info("On Idle command -- Calling Active Behavior");
            return new Active(context, message);
        });
    }

    private class Active extends AbstractBehavior<Command> {

        private final List<Command> buffer = new ArrayList<>();

        private Active(ActorContext<Command> context,Command firstCommand) {
            super(context);
            buffer.add(firstCommand);
        }

        @Override
        public Receive<Command> createReceive() {
            return newReceiveBuilder()
                    .onMessage(Timeout.class, message -> onTimeout())
                    .onMessage(Command.class, this::onCommand)
                    .build();
        }

        private Behavior<Command> onTimeout() {
            target.tell(Batch.of(buffer));
            return idle();
        }

        private Behavior<Command> onCommand(Command message) {
            getContext().getLog().info("Received new Command {}", message);
            buffer.add(message);
            if (buffer.size() == maxSize) {
                getContext().getLog().info("maxSize reached ending this batch");
                timers.cancel(TIMER_KEY);
                target.tell(Batch.of(buffer));
                return idle();
            } else {
                getContext().getLog().info("keeping the active state");
                return this;
            }
        }
    }

}
