package org.example.actor.patterns.aggregator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class HotelCustomer extends AbstractBehavior<HotelCustomer.Command> {

    interface Command {}

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @ToString
    static class Quote {
        private final String hotel;
        private final BigDecimal price;
    }

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @ToString
    static class AggregatedQuotes implements Command {
        private final List<Quote> quotes;
    }

    public static Behavior<Command> create(ActorRef<RequestQuote> hotel1, ActorRef<RequestPrice> hotel2,
                                           ActorRef<ClientInfo> client) {
        return Behaviors.setup(context -> new HotelCustomer(context, hotel1, hotel2, client));
    }

    private ActorRef<ClientInfo> client;

    private HotelCustomer(ActorContext<Command> context,
                          ActorRef<RequestQuote> hotel1,
                          ActorRef<RequestPrice> hotel2,
                          ActorRef<ClientInfo> client) {
        super(context);
        this.client = client;

        Consumer<ActorRef<Object>> sendRequests = replyTo -> {
            hotel1.tell(RequestQuote.of(replyTo.narrow()));
            hotel2.tell(RequestPrice.of(replyTo.narrow()));
        };

        int expectedReplies = 2;
        context.spawnAnonymous(
                Aggregator.create(
                        Object.class,
                        sendRequests,
                        expectedReplies,
                        context.getSelf(),
                        this::aggregateReplies,
                        Duration.ofSeconds(5)));
    }

    private AggregatedQuotes aggregateReplies(List<Object> replies) {
        List<Quote> quotes = replies.stream()
                .map(r -> {
                    if (r instanceof org.example.actor.patterns.aggregator.Quote) {
                        org.example.actor.patterns.aggregator.Quote q = (org.example.actor.patterns.aggregator.Quote) r;
                        return Quote.of(q.getHotel(), q.getPrice());
                    } else if (r instanceof Price) {
                        Price p = (Price) r;
                        return Quote.of(p.getHotel(), p.getPrice());
                    } else {
                        throw new IllegalArgumentException("Unknown reply " + r);
                    }
                })
                .sorted(Comparator.comparing(a -> a.price))
                .collect(Collectors.toList());

        return new AggregatedQuotes(quotes);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(AggregatedQuotes.class, this::onAggregatedQuotes)
                .build();
    }

    private Behavior<Command> onAggregatedQuotes(AggregatedQuotes aggregated) {
        if (aggregated.quotes.isEmpty()) getContext().getLog().info("Best Quote N/A");
        else getContext().getLog().info("Best {}", aggregated.quotes.get(0));
        client.tell(ClientInfo.of(aggregated.quotes.get(0).hotel, aggregated.quotes.get(0).price));
        return this;
    }
}
