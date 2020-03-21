package org.example.actor.fault_tolerance.bubble;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class MiddleManagement extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(MiddleManagement::new);
    }

    private final ActorRef<Command> child;

    private MiddleManagement(ActorContext<Command> context) {
        super(context);
        context.getLog().info("Middle management starting up");
        child = context.spawn(Worker.create(), "child");
        context.watch(child);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(Command.class, this::onCommand).build();
    }

    private Behavior<Command> onCommand(Command message) {
        child.tell(message);
        return this;
    }
}
