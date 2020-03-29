package org.example.actor.dispatcher.blocking;

import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

class SeparateDispatcherFutureActor extends AbstractBehavior<Integer> {

    public static Behavior<Integer> create() {
        return Behaviors.setup(SeparateDispatcherFutureActor::new);
    }

    private final Executor ec;

    private SeparateDispatcherFutureActor(ActorContext<Integer> context) {
        super(context);
        ec = context.getSystem().dispatchers().lookup(DispatcherSelector.fromConfig("blocking-io-dispatcher"));
    }

    @Override
    public Receive<Integer> createReceive() {
        return newReceiveBuilder()
                .onMessage(Integer.class, this::onInteger)
                .build();
    }

    private Behavior<Integer> onInteger(Integer i) {
        triggerFutureBlockingOperation(i, ec, getContext());
        return Behaviors.same();
    }

    private static void triggerFutureBlockingOperation(Integer i, Executor ec, ActorContext<Integer> context) {
        context.getLog().info("Calling blocking Future on separate dispatcher: {}", i);
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
                context.getLog().info("Blocking future finished: {}", i);
                return i;
            } catch (InterruptedException e) {
                return -1;
            }
        }, ec);
    }
}
