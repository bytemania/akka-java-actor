package org.example.actor.dispatcher.bad;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Main {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            for (int i = 0; i < 100; i++) {
                context.spawn(BlockingActor.create(), "BlockingActor-" + i).tell(i);
                context.spawn(PrintActor.create(), "PrintActor-" + i).tell(i);
            }
            return Behaviors.ignore();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "BadActorSystem");
    }

}
