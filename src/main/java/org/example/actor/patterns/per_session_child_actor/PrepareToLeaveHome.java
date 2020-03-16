package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Optional;

class PrepareToLeaveHome extends AbstractBehavior<Item> {

    static Behavior<Item> create(
            String whoIsLeaving,
            ActorRef<ReadyToLeaveHome> replyTo,
            ActorRef<GetKeys> keyCabinet,
            ActorRef<GetWallet> drawer) {
        return Behaviors.setup(context -> new PrepareToLeaveHome(context, whoIsLeaving, replyTo, keyCabinet, drawer));
    }

    private final String whoIsLeaving;
    private final ActorRef<ReadyToLeaveHome> replyTo;
    private Wallet wallet;
    private Keys keys;

    private PrepareToLeaveHome(
            ActorContext<Item> context,
            String whoIsLeaving,
            ActorRef<ReadyToLeaveHome> replyTo,
            ActorRef<GetKeys> keyCabinet,
            ActorRef<GetWallet> drawer) {
        super(context);
        this.whoIsLeaving = whoIsLeaving;
        this.replyTo = replyTo;
        wallet = null;
        keys = null;

        drawer.tell(GetWallet.of(whoIsLeaving, getContext().getSelf()));
        keyCabinet.tell(GetKeys.of(whoIsLeaving, getContext().getSelf()));
    }

    @Override
    public Receive<Item> createReceive() {
        return newReceiveBuilder()
                .onMessage(Wallet.class, this::onWallet)
                .onMessage(Keys.class, this::onKeys)
                .build();
    }

    private Behavior<Item> onWallet(Wallet wallet) {
        this.wallet = wallet;
        return completeOrContinue();
    }

    private Behavior<Item> onKeys(Keys keys) {
        this.keys = keys;
        return completeOrContinue();
    }

    private Behavior<Item> completeOrContinue() {
        if (Optional.ofNullable(wallet).isPresent() && Optional.ofNullable(keys).isPresent()) {
            replyTo.tell(ReadyToLeaveHome.of(whoIsLeaving, keys, wallet));
            return Behaviors.stopped();
        } else {
            return this;
        }
    }
}
