package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class KeyCabinet {

    public static Behavior<GetKeys> create() {
        return Behaviors.receiveMessage(KeyCabinet::onGetKeys);
    }

    private static Behavior<GetKeys> onGetKeys(GetKeys message) {
        message.getReplyTo().tell(Keys.of());
        return Behaviors.same();
    }

}
