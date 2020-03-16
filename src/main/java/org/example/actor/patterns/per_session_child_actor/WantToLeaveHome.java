package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class WantToLeaveHome extends AbstractBehavior<ReadyToLeaveHome> {

    public static Behavior<ReadyToLeaveHome> create() {
        return Behaviors.setup(WantToLeaveHome::new);
    }

    private WantToLeaveHome(ActorContext<ReadyToLeaveHome> context) {
        super(context);
    }

    @Override
    public Receive<ReadyToLeaveHome> createReceive() {
        return newReceiveBuilder()
                .onMessage(ReadyToLeaveHome.class, this::onReadyToLeaveHome)
                .build();
    }

    private Behavior<ReadyToLeaveHome> onReadyToLeaveHome(ReadyToLeaveHome message) {
        getContext().getLog().info("Ready to leave home {}", message.getWho());
        return this;
    }
}
