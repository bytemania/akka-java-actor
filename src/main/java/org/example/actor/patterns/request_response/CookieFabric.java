package org.example.actor.patterns.request_response;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
class CookieFabric {

    public static Behavior<CommandRequest> create() {
        return Behaviors.receive(CommandRequest.class)
                .onMessage(Request.class, CookieFabric::onRequest)
                .build();
    }

    private static Behavior<CommandRequest> onRequest(Request request) {
        request.getReplyTo().tell(Response.of("Here are the cookies for " + request.getQuery()));
        return Behaviors.same();
    }
}
