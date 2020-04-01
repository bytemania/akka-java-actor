package org.example.actor.testing.async.log;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Log {
    static Behavior<Message> createConstantLog() {
        return behavior((context, message) -> onConstantLog(context));
    }

    static Behavior<Message> createError() {
        return behavior((Log::onError));
    }

    private static Behavior<Message> behavior(BiFunction<ActorContext<Message>, Message, Behavior<Message>> onMessage) {
        return Behaviors.setup(context -> Behaviors.receive(Message.class)
                .onMessage(Message.class, message -> onMessage.apply(context, message))
                .build());
    }

    private static Behavior<Message> onConstantLog(ActorContext<Message> context) {
        context.getLog().info("Message received");
        return Behaviors.same();
    }

    private static Behavior<Message> onError(ActorContext<Message> context, Message message) {
        try {
            throw new IllegalArgumentException(message.getMessage());
        } catch (IllegalArgumentException e) {
            context.getLog().error("Error received", e);
        }
        return Behaviors.same();
    }
}
