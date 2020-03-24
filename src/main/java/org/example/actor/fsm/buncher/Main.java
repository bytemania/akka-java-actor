package org.example.actor.fsm.buncher;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static Behavior<Batch> createReceiver() {
        return Behaviors.setup(context ->
                Behaviors.receive(Batch.class)
                        .onMessage(Batch.class, b -> {
                            for (var e: b.getList()) {
                                context.getLog().info("message received: {}", e);
                            }
                            context.getLog().info("END BATCH");
                            return Behaviors.same();
                        })
                    .build()
        );
    }

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<Batch> target = context.spawn(Main.createReceiver(), "target");
            ActorRef<Event> buncher = context.spawn(Buncher.create(), "buncher");

            buncher.tell(SetTarget.of(target));

            for (int i = 0; i < 10; i++) {
                if (i > 0 && i % 3 == 0) {
                    buncher.tell(Flush.INSTANCE);
                } else if (i > 0 && i % 5 == 0) {
                    Thread.currentThread().sleep(1000);
                } else {
                    buncher.tell(Queue.of("elem " + i));
                }
            }

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, sig -> Behaviors.stopped())
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "FSMBuncherCreator");
    }
}
