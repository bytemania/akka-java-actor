package org.example.actor.fault_tolerance.bubble;

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

            ActorRef<String> response = context.spawn(Response.create(), "response");
            ActorRef<Command> boss = context.spawn(Boss.create(), "boss");
            boss.tell(Hello.of("Hello World", response.narrow()));
            boss.tell(Fail.of("FAIL"));
            boss.tell(Hello.of("Hello Number 2", response.narrow()));

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, signal -> {
                        System.out.println("Terminated signal receive");
                        return Behaviors.stopped();
                    }).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "BubbleUpSystem");
    }
}
