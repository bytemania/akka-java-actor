package org.example.actor.lifecycle;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class Job extends AbstractBehavior<Command> {

    static Behavior<Command> create(String name) {
        return Behaviors.setup(context -> new Job(context, name));
    }

    private final String name;

    private Job(ActorContext<Command> context, String name) {
        super(context);
        this.name = name;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onSignal(PostStop.class, postStop -> onPostStop())
                .build();
    }

    private Behavior<Command> onPostStop () {
        getContext().getSystem().log().info("Worker {} stopped", name);
        return this;
    }

}
