package org.example.actor.style.messages;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

interface CounterProtocol {

    interface Message {}

    interface Command extends Message {}

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    class Increment implements Command {
        private final int delta;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    class Decrement implements Command {
        private final int delta;
        private final ActorRef<OperationResult> replyTo;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    class GetValue implements Command {
        private final ActorRef<Value> replyTo;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    class Value {
        private final int value;
    }

    interface OperationResult {}

    enum Confirmed implements OperationResult {
        INSTANCE
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode
    @ToString
    class Rejected implements OperationResult {
        private final String reason;
    }

}
