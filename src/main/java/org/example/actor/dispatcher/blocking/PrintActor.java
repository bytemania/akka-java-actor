package org.example.actor.dispatcher.blocking;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class PrintActor extends AbstractBehavior<Integer> {

    public static Behavior<Integer> create() {
        return Behaviors.setup(PrintActor::new);
    }

    private PrintActor(ActorContext<Integer> context) {
        super(context);
    }

    @Override
    public Receive<Integer> createReceive() {
        return newReceiveBuilder()
                .onMessage(Integer.class, this::onInteger)
                .build();
    }

    private Behavior<Integer> onInteger(Integer i) {
        getContext().getLog().info("PrintActor: {}", i);
        return Behaviors.same();
    }
}
