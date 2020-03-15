package org.example.actor.patterns.request_response_ask;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class Hal extends AbstractBehavior<HalCommand> {

    public static Behavior<HalCommand> create() {
        return Behaviors.setup(Hal::new);
    }

    private Hal(ActorContext<HalCommand> context) {
        super(context);
    }

    @Override
    public Receive<HalCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(OpenThePodBayDoorsPlease.class, this::onOpenThePodBayDoorsPlease)
                .build();
    }

    private Behavior<HalCommand> onOpenThePodBayDoorsPlease(OpenThePodBayDoorsPlease message) {
        message.getRespondTo().tell(HalResponse.of("I'm sorry, Dave. I'm afraid I can't do that."));
        return this;
    }
}
