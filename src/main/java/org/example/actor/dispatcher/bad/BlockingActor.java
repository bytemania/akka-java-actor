package org.example.actor.dispatcher.bad;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class BlockingActor extends AbstractBehavior<Integer> {

    public static Behavior<Integer> create() {
        return Behaviors.setup(BlockingActor::new);
    }

    private BlockingActor(ActorContext<Integer> context) {
        super(context);
    }

    @Override
    public Receive<Integer> createReceive() {
        return newReceiveBuilder()
                .onMessage(Integer.class, this::onInteger)
                .build();
    }

    private Behavior<Integer> onInteger(Integer i) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            getContext().getLog().warn("Threat Interrupted : {}", e.getMessage());
        }
        getContext().getLog().info("Blocking operation finished: {}", i);
        return Behaviors.same();
    }

}
