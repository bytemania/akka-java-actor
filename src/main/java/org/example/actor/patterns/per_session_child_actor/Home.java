package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

class Home {
    private final ActorContext<Command> context;
    private final ActorRef<GetKeys> keyCabinet;
    private final ActorRef<GetWallet> drawer;

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> new Home(context).behavior());
    }

    private Home(ActorContext<Command> context) {
        this.context = context;
        this.keyCabinet = context.spawn(KeyCabinet.create(), "key-cabinet");
        this.drawer = context.spawn(Drawer.create(), "drawer");
    }

    private Behavior<Command> behavior() {
        return Behaviors.receive(Command.class)
                .onMessage(LeaveHome.class, this::onLeaveHome)
                .build();
    }

    private Behavior<Command> onLeaveHome(LeaveHome message) {
        context.spawn(PrepareToLeaveHome.create(message.getWho(), message.getRespondTo(), keyCabinet, drawer),
                "leaving" + message.getWho());
        return Behaviors.same();
    }

}
