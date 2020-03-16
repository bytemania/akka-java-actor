package org.example.actor.patterns.ignore_reply;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Requester extends AbstractBehavior<Response> {

    private enum AskCookies implements Response {
        COOKIES
    }

    public static Behavior<Response> create(ActorRef<Request> cookieFabric) {
        return Behaviors.setup(ctx -> new Requester(ctx, cookieFabric));
    }

    private ActorRef<Request> cookieFabric;

    private Requester(ActorContext<Response> context, ActorRef<Request> cookieFabric) {
        super(context);
        this.cookieFabric = cookieFabric;
        getContext().getSelf().tell(AskCookies.COOKIES);
    }

    @Override
    public Receive<Response> createReceive() {
        return newReceiveBuilder()
                .onMessage(AskCookies.class, this::askCookies)
                .build();
    }

    private Behavior<Response> askCookies(AskCookies askCookies) {
        getContext().getLog().info("Received ask cookies {}", askCookies.name());
        cookieFabric.tell(Request.of("Give me cookies", getContext().getSystem().ignoreRef()));
        return this;
    }
}
