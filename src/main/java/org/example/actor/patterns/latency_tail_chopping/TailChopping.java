package org.example.actor.patterns.latency_tail_chopping;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.util.function.BiPredicate;

public class TailChopping<REPLY> extends AbstractBehavior<TailChopping.Command> {

    interface Command {}

    @ToString
    private enum RequestTimeout implements Command {
        INSTANCE
    }

    @ToString
    private enum FinalTimeout implements Command {
        INSTANCE
    }

    @AllArgsConstructor
    @ToString
    private class WrappedReply implements Command {
        private final REPLY reply;
    }

    public static <R> Behavior<Command> create(
            Class<R> replyClass,
            BiPredicate<Integer, ActorRef<R>> sendRequest,
            Duration nextRequestAfter,
            ActorRef<R> replyTo,
            Duration finalTimeout,
            R timeoutReply) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers ->
                new TailChopping<>(replyClass, context, timers, sendRequest, nextRequestAfter, replyTo,
                        finalTimeout, timeoutReply)));
    }

    private final TimerScheduler<Command> timers;
    private final BiPredicate<Integer, ActorRef<REPLY>> sendRequest;
    private final Duration nextRequestAfter;
    private final ActorRef<REPLY> replyTo;
    private final Duration finalTimeout;
    private final REPLY timeoutReply;
    private final ActorRef<REPLY> replyAdapter;

    private int requestCount;

    private TailChopping(
            Class<REPLY> replyClass,
            ActorContext<Command> context,
            TimerScheduler<Command> timers,
            BiPredicate<Integer, ActorRef<REPLY>> sendRequest,
            Duration nextRequestAfter,
            ActorRef<REPLY> replyTo,
            Duration finalTimeout,
            REPLY timeoutReply) {
        super(context);
        this.timers = timers;
        this.sendRequest = sendRequest;
        this.nextRequestAfter = nextRequestAfter;
        this.replyTo = replyTo;
        this.finalTimeout = finalTimeout;
        this.timeoutReply = timeoutReply;

        replyAdapter = context.messageAdapter(replyClass, WrappedReply::new);

        sendNextRequest();
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(WrappedReply.class, this::onReply)
                .onMessage(RequestTimeout.class, notUsed -> onRequestTimeout())
                .onMessage(FinalTimeout.class, notUsed -> onFinalTimeout())
                .build();
    }

    private Behavior<Command> onReply(WrappedReply wrappedReply) {
        getContext().getLog().info("WrappedReply received {}", wrappedReply);
        REPLY reply = wrappedReply.reply;
        replyTo.tell(reply);
        return Behaviors.stopped();
    }

    private Behavior<Command> onRequestTimeout() {
        getContext().getLog().info("Request timeout");
        sendNextRequest();
        return this;
    }

    private Behavior<Command> onFinalTimeout() {
        getContext().getLog().info("Final timeout");
        replyTo.tell(timeoutReply);
        return Behaviors.stopped();
    }

    private void sendNextRequest() {
        requestCount++;
        if (sendRequest.test(requestCount, replyAdapter)) {
            getContext().getLog().info("Scheduling the next request");
            timers.startSingleTimer(RequestTimeout.INSTANCE, RequestTimeout.INSTANCE, nextRequestAfter);
        } else {
            getContext().getLog().info("Scheduling with the final timeout");
            timers.startSingleTimer(FinalTimeout.INSTANCE, FinalTimeout.INSTANCE, finalTimeout);
        }
    }

}
