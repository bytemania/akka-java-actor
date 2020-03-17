package org.example.actor.patterns.aggregator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Aggregator<REPLY, AGGREGATE> extends AbstractBehavior<Aggregator.Command> {

    interface Command {}

    private enum ReceiveTimeout implements Command {
        INSTANCE
    }

    @AllArgsConstructor
    private class WrappedReply implements Command {
        private final REPLY reply;
    }

    public static <R, A> Behavior<Command> create(
            Class<R> replyClass,
            Consumer<ActorRef<R>> sendRequests,
            int expectedReplies,
            ActorRef<A> replyTo,
            Function<List<R>, A> aggregateReplies,
            Duration timeout) {
        return Behaviors.setup(context -> new Aggregator<>(replyClass, context, sendRequests, expectedReplies,
                replyTo, aggregateReplies, timeout));
    }

    private final int expectedReplies;
    private final ActorRef<AGGREGATE> replyTo;
    private final Function<List<REPLY>, AGGREGATE> aggregateReplies;
    private final List<REPLY> replies;

    private Aggregator(
            Class<REPLY> replyClass,
            ActorContext<Command> context,
            Consumer<ActorRef<REPLY>> sendRequests,
            int expectedReplies,
            ActorRef<AGGREGATE> replyTo,
            Function<List<REPLY>, AGGREGATE> aggregateReplies,
            Duration timeout) {
        super(context);
        this.expectedReplies = expectedReplies;
        this.replyTo = replyTo;
        this.aggregateReplies = aggregateReplies;
        context.setReceiveTimeout(timeout, ReceiveTimeout.INSTANCE);
        ActorRef<REPLY> replyAdapter = context.messageAdapter(replyClass, WrappedReply::new);
        sendRequests.accept(replyAdapter);
        replies = new ArrayList<>();
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(WrappedReply.class, this::onReply)
                .onMessage(ReceiveTimeout.class, notUsed -> onReceiveTimeout())
                .build();
    }

    private Behavior<Command> onReply(WrappedReply wrappedReply) {
        REPLY reply = wrappedReply.reply;
        replies.add(reply);
        if (replies.size() == expectedReplies) {
            AGGREGATE result = aggregateReplies.apply(replies);
            replyTo.tell(result);
            return Behaviors.stopped();
        } else {
            return this;
        }
    }

    private Behavior<Command> onReceiveTimeout() {
        AGGREGATE result = aggregateReplies.apply(replies);
        replyTo.tell(result);
        return Behaviors.stopped();
    }
}
