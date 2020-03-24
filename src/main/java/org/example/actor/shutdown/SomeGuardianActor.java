package org.example.actor.shutdown;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class SomeGuardianActor extends AbstractBehavior<String> {

    public static Behavior<String> create() {
        return Behaviors.setup(SomeGuardianActor::new);
    }

    private SomeGuardianActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("stop", Behaviors::stopped)
                .build();
    }
}
