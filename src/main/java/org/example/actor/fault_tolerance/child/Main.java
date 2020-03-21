package org.example.actor.fault_tolerance.child;

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

            System.out.println("NO RESTART");

            ActorRef<String> parentWithNoRestartChild = context.spawn(ChildStoppedInParentRestart.parentWithNoRestartChild(), "parentWithNoRestartChild");

            parentWithNoRestartChild.tell("one one");
            parentWithNoRestartChild.tell("FAIL");
            parentWithNoRestartChild.tell("two two");

            Thread.sleep(1000);

            System.out.println("RESTART");

            ActorRef<String> parentChildRestart = context.spawn(ChildStoppedInParentRestart.parent(), "parent");

            parentChildRestart.tell("one one");
            parentChildRestart.tell("FAIL");
            parentChildRestart.tell("two two");

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, signal -> {
                        System.out.println("Terminated signal received");
                        return Behaviors.stopped();
                    }).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "ParentChildSystem");
    }

}
