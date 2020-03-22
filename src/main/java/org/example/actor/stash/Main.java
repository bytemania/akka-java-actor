package org.example.actor.stash;

import akka.Done;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Main {

    private static Behavior<Done> replyTo() {
        return Behaviors.setup(context -> Behaviors.receive(Done.class)
                .onMessage(Done.class, d -> {
                    System.out.println("Received " + d);
                    return Behaviors.same();
                })
                .build());
    }

    private static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {
                    ActorRef<Done> reply = context.spawn(Main.replyTo(), "reply");
                    ActorRef<Command> data = context.spawn(DataAccess.create("data1", new DB() {
                        @Override
                        public CompletionStage<Done> save(String id, String value) {
                            System.out.println("Saving " + id + " " + value);
                            return CompletableFuture.completedFuture(Done.done());
                        }

                        @Override
                        public CompletionStage<String> load(String id) {
                            System.out.println("Load " + id);
                            return CompletableFuture.completedFuture("VALUE");
                        }
                    }), "dataAccess");

                    data.tell(Save.of("PAYLOAD", reply.narrow()));

                    return Behaviors.receive(Void.class).onSignal(Terminated.class, signal -> Behaviors.stopped()).build();
                }
        );
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "stashSystem");
    }

}
