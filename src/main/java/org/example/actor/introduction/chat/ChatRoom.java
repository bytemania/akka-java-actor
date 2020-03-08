package org.example.actor.introduction.chat;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class ChatRoom {

    public static Behavior<RoomCommand> create() {
        return Behaviors.setup(ChatRoomBehavior::new);
    }

}
