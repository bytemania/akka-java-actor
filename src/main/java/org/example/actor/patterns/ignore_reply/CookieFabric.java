package org.example.actor.patterns.ignore_reply;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class CookieFabric extends AbstractBehavior<Request> {
    public static Behavior<Request> create() {
        return Behaviors.setup(CookieFabric::new);
    }

    private CookieFabric(ActorContext<Request> context) {
        super(context);
    }

    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(Request.class, this::onRequest)
                .build();
    }

    private Behavior<Request> onRequest(Request message) {
        getContext().getLog().info("Received request: {}", message.getMessage());
        message.getReplyTo().tell(ResponseCookie.of("Response"));
        return this;
    }
}
