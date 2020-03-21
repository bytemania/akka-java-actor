package org.example.actor.fault_tolerance.bubble;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class Worker extends AbstractBehavior<Command> {

    static Behavior<Command> create() {
        return Behaviors.setup(Worker::new);
    }

    public Worker(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Fail.class, this::onFail)
                .onMessage(Hello.class, this::onHello)
                .build();
    }

    private Behavior<Command> onFail(Fail message) {
        throw new RuntimeException(message.getText());
    }

    private Behavior<Command> onHello(Hello message) {
        message.getReplyTo().tell(message.getText());
        return this;
    }
}
