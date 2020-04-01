package org.example.actor.testing.async.echo;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Echo {
    static Behavior<Ping> create() {
        return Behaviors.receive(Ping.class)
                .onMessage(Ping.class, ping -> {
                    ping.getReplyTo().tell(Pong.of(ping.getMessage()));
                    return Behaviors.same();
                }).build();
    }
}
