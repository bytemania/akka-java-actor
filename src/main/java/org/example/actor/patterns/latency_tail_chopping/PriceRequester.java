package org.example.actor.patterns.latency_tail_chopping;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.function.BiPredicate;

class PriceRequester extends AbstractBehavior<PriceRequester.Command> {

    private static final int MAX_TRIES = 5;

    interface Command {}

    @ToString
    private enum Timeout implements Command {
        INSTANCE
    }

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @ToString
    static class Quote implements Command {
        private final Integer hotel;
        private final BigDecimal price;
    }

    public static Behavior<Command> create(ActorRef<RequestPrice> hotel) {
        return Behaviors.setup(context -> new PriceRequester(context, hotel));
    }

    private PriceRequester(ActorContext<Command> context, ActorRef<RequestPrice> hotel) {
        super(context);
        BiPredicate<Integer, ActorRef<Command>> sendRequest = (tries, actor) -> {
            hotel.tell(RequestPrice.of(actor.narrow()));
            return tries < MAX_TRIES;
        };

        context.spawnAnonymous(TailChopping.create(Command.class, sendRequest, Duration.ofSeconds(4),
                context.getSelf(), Duration.ofSeconds(60), Timeout.INSTANCE));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.class, this::onCommand)
                .build();
    }

    private Behavior<Command> onCommand(Command message) {
        getContext().getLog().info("Command Received {}", message);
        return this;
    }

}
