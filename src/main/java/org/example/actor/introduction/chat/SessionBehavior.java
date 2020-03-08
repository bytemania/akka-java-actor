package org.example.actor.introduction.chat;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class SessionBehavior extends AbstractBehavior<SessionCommand> {
    public static Behavior<SessionCommand> create(ActorRef<RoomCommand> room, String screenName, ActorRef<SessionEvent> client) {
        return Behaviors.setup(context -> new SessionBehavior(context, room, screenName, client));
    }

    private final ActorRef<RoomCommand> room;
    private final String screenName;
    private final ActorRef<SessionEvent> client;

    private SessionBehavior(ActorContext<SessionCommand> context, ActorRef<RoomCommand> room, String screenName, ActorRef<SessionEvent> client) {
        super(context);
        this.room = room;
        this.screenName = screenName;
        this.client = client;
    }

    @Override
    public Receive<SessionCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(PostMessage.class, this::onPostMessage)
                .build();
    }

    private Behavior<SessionCommand> onPostMessage(PostMessage post) {
        room.tell(PublishSessionMessage.of(screenName, post.getMessage()));
        return Behaviors.same();
    }

    private Behavior<SessionCommand> onNotifyClient(NotifyClient notification) {
        client.tell(notification.getMessage());
        return Behaviors.same();
    }

}
