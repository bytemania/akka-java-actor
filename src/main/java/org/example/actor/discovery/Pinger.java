package org.example.actor.discovery;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

public class Pinger {

    static Behavior<Pong> create(ActorRef<Ping> pingService) {
        return Behaviors.setup(ctx -> {
           pingService.tell(Ping.of(ctx.getSelf()));
           return new Pinger(ctx, pingService).behavior();
        });
    }

    private final ActorContext<Pong> context;
    private final ActorRef<Ping> pingService;

    private Pinger(ActorContext<Pong> context, ActorRef<Ping> pingService) {
        this.context = context;
        this.pingService = pingService;
    }

    private Behavior<Pong> behavior() {
        return Behaviors.receive(Pong.class)
                .onMessage(Pong.class, pong -> onPong())
                .build();
    }

    private Behavior<Pong> onPong() {
        context.getLog().info("{} was ponged!!", context.getSelf());
        return Behaviors.stopped();
    }
}
