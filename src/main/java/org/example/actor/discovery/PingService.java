package org.example.actor.discovery;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

public class PingService {

    static final ServiceKey<Ping> pingServiceKey = ServiceKey.create(Ping.class, "pingService");

    public static Behavior<Ping> create() {
        return Behaviors.setup(context -> {
            context.getSystem().receptionist().tell(Receptionist.register(pingServiceKey, context.getSelf()));
            return new PingService(context).behavior();
        });
    }

    private final ActorContext<Ping> context;

    private PingService(ActorContext<Ping> context) {
        this.context = context;
    }

    private Behavior<Ping> behavior() {
        return Behaviors.receive(Ping.class)
                .onMessage(Ping.class, this::onPing)
                .build();
    }

    private Behavior<Ping> onPing(Ping msg) {
        context.getLog().info("Pinged by {}", msg.getReplyTo());
        msg.getReplyTo().tell(Pong.of());
        return Behaviors.same();
    }

}
