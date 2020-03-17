package org.example.actor.patterns.aggregator;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.math.BigDecimal;

class HotelPrice extends AbstractBehavior<RequestPrice> {

    public static Behavior<RequestPrice> create() {
        return Behaviors.setup(HotelPrice::new);
    }

    private HotelPrice(ActorContext<RequestPrice> context) {
        super(context);
    }

    @Override
    public Receive<RequestPrice> createReceive() {
        return newReceiveBuilder().onMessage(RequestPrice.class, this::onRequestPrice).build();
    }

    private Behavior<RequestPrice> onRequestPrice(RequestPrice message) {
        getContext().getLog().info("Received request price from {}", message);
        message.getReplyTo().tell(Price.of("HotelPrice", new BigDecimal("100.5")));
        return this;
    }
}
