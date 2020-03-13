package org.example.actor.patterns.adapted_response;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;

import java.net.URI;

public class Frontend {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<Request> backend = context.spawn(Backend.create(), "Backend");
            ActorRef<Command> frontend = context.spawn(Translator.create(backend), "Frontend");
            ActorRef<URI> requester = context.spawn(Requester.create(), "Requester");

            URI uri = new URI("https://akka.io/docs");
            URI uri2 = new URI("https://example.org");
            frontend.tell(Translate.of(uri, requester));
            frontend.tell(Translate.of(uri2, requester));

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, sig -> Behaviors.stopped())
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Frontend.create(), "FrontendDemo");
    }

}
