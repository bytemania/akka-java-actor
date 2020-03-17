package org.example.actor.patterns.aggregator;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class HotelQuote {
    public static Behavior<RequestQuote> create() {
        return Behaviors.setup(context ->
                Behaviors.receive(RequestQuote.class)
                        .onMessage(
                                RequestQuote.class,
                                request -> {
                                    context.getLog().info("Received Request: {}", request);
                                    request.getReplyTo().tell(Quote.of("HotelQuote", new BigDecimal("103.2")));
                                    return Behaviors.same();
                                })
                .build());
    }

}
