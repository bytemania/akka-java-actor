package org.example.actor.patterns.request_response;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class CookieRequester extends AbstractBehavior<CommandResponse> {

    static Behavior<CommandResponse> create() {
        return Behaviors.setup(CookieRequester::new);
    }

    private final ActorRef<CommandRequest> fabric;

    private CookieRequester(ActorContext<CommandResponse> context) {
        super(context);
        fabric = getContext().spawn(CookieFabric.create(),"fabric");
    }

    @Override
    public Receive<CommandResponse> createReceive() {
        return newReceiveBuilder()
                .onMessage(GiveMeCookies.class, msg -> onGiveMeCookies())
                .onMessage(Response.class, this::onResponse)
                .build();
    }

    private Behavior<CommandResponse> onGiveMeCookies() {
        getContext().getLog().info("Give me cookies");
        fabric.tell(Request.of("Give me Cookies", getContext().getSelf()));
        return Behaviors.same();
    }

    private Behavior<CommandResponse> onResponse(Response response) {
        getContext().getLog().info(response.toString());
        return Behaviors.same();
    }

    public static void main(String[] args) {
        final ActorSystem<CommandResponse> system = ActorSystem.create(CookieRequester.create(), "Requester");
        system.tell(GiveMeCookies.INSTANCE);
    }
}
