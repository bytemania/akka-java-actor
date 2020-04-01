package org.example.actor.testing.async.producer;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

class Producer {

    private final Scheduler scheduler;
    private final ActorRef<Message> publisher;

    Producer(Scheduler scheduler, ActorRef<Message> publisher) {
        this.scheduler = scheduler;
        this.publisher = publisher;
    }

    void produce(int messages) {
        IntStream.range(0, messages).forEach(this::publish);
    }

    private CompletionStage<Integer> publish(int i) {
        return AskPattern.ask(publisher, ref -> Message.of(i, ref), Duration.ofSeconds(3), scheduler);
    }
}
