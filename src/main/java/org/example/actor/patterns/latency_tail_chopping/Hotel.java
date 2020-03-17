package org.example.actor.patterns.latency_tail_chopping;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.math.BigDecimal;

class Hotel extends AbstractBehavior<RequestPrice> {

    public static Behavior<RequestPrice> create() {
        return Behaviors.setup(Hotel::new);
    }

    private static final int NO_PAUSE_AT_REQUEST_NR = 2;
    private static final long PAUSE = 5000;
    private int requestNumber = 0;

    private Hotel(ActorContext<RequestPrice> context) {
        super(context);
    }

    @Override
    public Receive<RequestPrice> createReceive() {
        return newReceiveBuilder()
                .onMessage(RequestPrice.class, this::onRequestPrice)
                .build();
    }

    private Behavior<RequestPrice> onRequestPrice(RequestPrice requestPrice) throws InterruptedException {
        getContext().getLog().info("Requested price number {} from {}", requestNumber, requestPrice.getReplyTo());
        if (requestNumber < NO_PAUSE_AT_REQUEST_NR) {
            requestNumber++;
            getContext().getLog().info("Paused: {}", requestNumber);
            Thread.sleep(PAUSE);
        }
        requestPrice.getReplyTo().tell(ResponsePrice.of(requestNumber, new BigDecimal("100.34")));
        return this;
    }
}
