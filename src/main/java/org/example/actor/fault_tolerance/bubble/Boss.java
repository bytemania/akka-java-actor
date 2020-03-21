package org.example.actor.fault_tolerance.bubble;

import akka.actor.DeathPactException;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Boss extends AbstractBehavior<Command> {

    static Behavior<Command> create() {
        return Behaviors.supervise(Behaviors.setup(Boss::new))
                .onFailure(DeathPactException.class, SupervisorStrategy.restart());
    }

    private final ActorRef<Command> middleManagement;

    private Boss(ActorContext<Command> context) {
        super(context);
        context.getLog().info("Boss starting up");
        middleManagement = context.spawn(MiddleManagement.create(), "middle-management");
        context.watch(middleManagement);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.class, this::onCommand)
                .build();
    }

    private Behavior<Command> onCommand(Command message){
        middleManagement.tell(message);
        return this;
    }
}
