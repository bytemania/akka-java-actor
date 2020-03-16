package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<ReadyToLeaveHome> wantToLeaveHome = context.spawn(WantToLeaveHome.create(), "WantToLeaveHome");
            ActorRef<Command> home = context.spawn(Home.create(), "Home");
            home.tell(LeaveHome.of("Antonio", wantToLeaveHome));

            return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "MainPerSessionChildActor");
    }

}
