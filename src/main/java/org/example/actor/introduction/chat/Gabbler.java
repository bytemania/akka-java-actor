package org.example.actor.introduction.chat;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class Gabbler extends AbstractBehavior<SessionEvent> {

    public static Behavior<SessionEvent> create() {
        return Behaviors.setup(Gabbler::new);
    }

    public Gabbler(ActorContext<SessionEvent> context) {
        super(context);
    }

    @Override
    public Receive<SessionEvent> createReceive() {
        return newReceiveBuilder()
                .onMessage(SessionDenied.class, this::onSessionDenied)
                .onMessage(SessionGranted.class, this::onSessionGranted)
                .onMessage(MessagePosted.class, this::onMessagePosted)
                .build();
    }

    private Behavior<SessionEvent> onSessionDenied(SessionDenied message) {
        getContext().getLog().info("cannot start chat room session: {}", message.getReason());
        return Behaviors.stopped();
    }

    private Behavior<SessionEvent> onSessionGranted(SessionGranted message) {
        message.getHandle().tell(PostMessage.of("Hello World!"));
        return Behaviors.same();
    }

    private Behavior<SessionEvent> onMessagePosted(MessagePosted message) {
        getContext().getLog().info("message has been posted '{}': {}", message.getScreenName(), message.getMessage());
        return Behaviors.stopped();
    }
}
