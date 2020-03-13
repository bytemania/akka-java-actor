package org.example.actor.patterns.adapted_response;


import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.net.URI;

class Requester extends AbstractBehavior<URI> {

    public static Behavior<URI> create() {
        return Behaviors.setup(Requester::new);
    }

    private Requester(ActorContext<URI> context) {
        super(context);
    }

    @Override
    public Receive<URI> createReceive() {
        return newReceiveBuilder()
                .onMessage(URI.class, this::printMe)
                .build();
    }

    private Behavior<URI> printMe(URI obj) {
        getContext().getLog().info("Requester Received {}", obj);
        return Behaviors.same();
    }
}
