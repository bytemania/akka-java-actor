package org.example.actor.patterns.aggregator;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Client {
    public static Behavior<ClientInfo> create() {
        return Behaviors.setup(context -> Behaviors.receive(ClientInfo.class)
                .onMessage(ClientInfo.class, info -> {
                    context.getLog().info("Received info for client {}", info);
                    return Behaviors.same();
                })
                .build());
    }
}
