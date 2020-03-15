package org.example.actor.patterns.request_response_ask_outside;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class CookieFabric extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(CookieFabric::new);
    }

    private CookieFabric(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(GiveMeCookies.class, this::onGiveMeCookies).build();
    }

    private Behavior<Command> onGiveMeCookies(GiveMeCookies request) {
        if (request.getCount() >= 5) request.getReplyTo().tell(InvalidRequest.of("Too many cookies"));
        else request.getReplyTo().tell(Cookies.of(request.getCount()));

        return this;
    }
}
