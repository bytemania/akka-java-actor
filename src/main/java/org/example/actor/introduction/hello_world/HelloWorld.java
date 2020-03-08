package org.example.actor.introduction.hello_world;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.slf4j.Logger;

public class HelloWorld extends AbstractBehavior<Greet> {

    public static Behavior<Greet> create() {
        return Behaviors.setup(HelloWorld::new);
    }

    private final Logger logger;

    private HelloWorld(ActorContext<Greet> context) {
        super(context);
        logger = getContext().getLog();
    }

    @Override
    public Receive<Greet> createReceive() {
        return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
    }

    private Behavior<Greet> onGreet(Greet command) {
        logger.info("Hello {}!", command.getWhom());
        command.getReplyTo().tell(Greeted.of(command.getWhom(), getContext().getSelf()));
        return this;
    }
}
