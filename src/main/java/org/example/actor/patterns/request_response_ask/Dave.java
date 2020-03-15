package org.example.actor.patterns.request_response_ask;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

class Dave extends AbstractBehavior<DaveCommand> {

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    private static class AdaptedResponse implements DaveCommand {
        private final String message;
    }

    public static Behavior<DaveCommand> create(ActorRef<HalCommand> hal) {
        return Behaviors.setup(context -> new Dave(context, hal));
    }

    private Dave(ActorContext<DaveCommand> context, ActorRef<HalCommand> hal) {
        super(context);

        final Duration timeout = Duration.ofSeconds(3);

        context.ask(
                HalResponse.class,
                hal,
                timeout,
                OpenThePodBayDoorsPlease::of,
                (response, throwable) ->
                        response != null ? AdaptedResponse.of(response.getMessage()) : AdaptedResponse.of("Request failed"));

        //the state (must be immutable!)
        final int requestId = 1;
        context.ask(HalResponse.class,
                hal,
                timeout,
                OpenThePodBayDoorsPlease::of,
                (response, throwable) ->
                        response != null ?
                                AdaptedResponse.of(requestId + ": " + response.getMessage())
                                : AdaptedResponse.of(requestId + ": Request failed"));
    }

    @Override
    public Receive<DaveCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(AdaptedResponse.class, this::onAdaptedResponse)
                .build();
    }

    private Behavior<DaveCommand> onAdaptedResponse(AdaptedResponse response) {
        getContext().getLog().info("Got response from HAL: {}", response.getMessage());
        return this;
    }
}
