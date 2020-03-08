package org.example.actor.introduction.chat;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomBehavior extends AbstractBehavior<RoomCommand> {
    private final List<ActorRef<SessionCommand>> sessions = new ArrayList<>();

    public ChatRoomBehavior(ActorContext<RoomCommand> context) {
        super(context);
    }

    @Override
    public Receive<RoomCommand> createReceive() {
        ReceiveBuilder<RoomCommand> builder = newReceiveBuilder();
        builder.onMessage(GetSession.class, this::onGetSession);
        builder.onMessage(PublishSessionMessage.class, this::onPublishSessionMessage);
        return builder.build();
    }

    private Behavior<RoomCommand> onGetSession(GetSession getSession) throws UnsupportedEncodingException {
        ActorRef<SessionEvent> client = getSession.getReplyTo();
        ActorRef<SessionCommand> ses = getContext().spawn(
                SessionBehavior.create(getContext().getSelf(), getSession.getScreenName(), client),
                URLEncoder.encode(getSession.getScreenName(), StandardCharsets.UTF_8.name()));
        client.tell(SessionGranted.of(ses.narrow()));
        return this;
    }

    private Behavior<RoomCommand> onPublishSessionMessage(PublishSessionMessage pub) {
        NotifyClient notification = NotifyClient.of((MessagePosted.of(pub.getScreenName(), pub.getMessage())));
        sessions.forEach(s -> s.tell(notification));
        return this;
    }
}
