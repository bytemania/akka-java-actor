package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Drawer {

    public static Behavior<GetWallet> create() {
        return Behaviors.receiveMessage(Drawer::onGetWallet);
    }

    private static Behavior<GetWallet> onGetWallet(GetWallet message) {
        message.getReplyTo().tell(Wallet.of());
        return Behaviors.same();
    }

}
