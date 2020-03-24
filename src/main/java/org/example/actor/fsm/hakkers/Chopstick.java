package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

class Chopstick {

    public static Behavior<ChopstickCommand> create() {
        return Behaviors.setup(context -> new Chopstick(context).available());
    }

    private final ActorContext<ChopstickCommand> context;

    private Chopstick(ActorContext<ChopstickCommand> context) {
        this.context = context;
    }

    private Behavior<ChopstickCommand> takenBy(ActorRef<Answer> hakker) {
        return Behaviors.receive(ChopstickCommand.class)
                .onMessage(Take.class, msg -> {
                    msg.getHakker().tell(Busy.of(context.getSelf()));
                    return Behaviors.same();
                })
                .onMessage(Put.class, m -> m.getHakker().equals(hakker), msg -> available())
                .build();
    }

    private Behavior<ChopstickCommand> available() {
        return Behaviors.receive(ChopstickCommand.class)
                .onMessage(Take.class, msg -> {
                    msg.getHakker().tell(Taken.of(context.getSelf()));
                    return takenBy(msg.getHakker());
                })
                .build();
    }

}
