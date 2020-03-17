package org.example.actor.patterns.aggregator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<ClientInfo> client = context.spawn(Client.create(), "client");
            ActorRef<RequestQuote> hotelQuote = context.spawn(HotelQuote.create(), "hotelQuote");
            ActorRef<RequestPrice> hotelPrice = context.spawn(HotelPrice.create(), "hotelPrice");
            context.spawn(HotelCustomer.create(hotelQuote, hotelPrice, client), "hotelCustomer");
            return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "AggregatorSystem");
    }

}
