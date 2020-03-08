package org.example.actor.introduction.hello_world;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.slf4j.Logger;

public class HelloWorldBot extends AbstractBehavior<Greeted> {

    public static Behavior<Greeted> create(int max) {
        return Behaviors.setup(context -> new HelloWorldBot(context, max));
    }

    private final Logger logger;
    private final int max;
    private int greetingCounter;

    private HelloWorldBot(ActorContext<Greeted> context, int max) {
        super(context);
        this.max = max;
        logger = getContext().getLog();
    }

    @Override
    public Receive<Greeted> createReceive() {
        return newReceiveBuilder()
                .onMessage(Greeted.class, this::onGreeted)
                .build();
    }

    private Behavior<Greeted> onGreeted(Greeted message) {
        logger.info("Greeting {} for {}", greetingCounter, message.getWhom());

        greetingCounter++;

        if (greetingCounter == max) {
            return Behaviors.stopped();
        }
        else {
            message.getFrom().tell(Greet.of(message.getWhom(), getContext().getSelf()));
            return this;
        }
    }
}
