package org.example.actor.fsm.hakkers;

import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DinningHackers {

    public static void main(String[] args) {
        ActorSystem.create(DinningHackers.create(), "DinningHakkers");
    }

    private static Behavior<NotUsed> create() {
        return Behaviors.setup(context -> new DinningHackers(context).behavior());
    }

    private final ActorContext<NotUsed> context;

    private DinningHackers(ActorContext<NotUsed> context) {
        this.context = context;
    }

    private Behavior<NotUsed> behavior() {
        List<ActorRef<ChopstickCommand>> chopsticks = IntStream.range(0, 5)
                .mapToObj(i -> context.spawn(Chopstick.create(), "Chopstick" + i))
                .collect(Collectors.toList());

        List<String> names = Arrays.asList("Ghosh", "Boner", "Klang", "Krasser", "Manie");
        IntStream.range(0, names.size()).mapToObj(i -> {
            Behavior<HakkerCommand> hakker = Hakker.create(names.get(i), chopsticks.get(i), chopsticks.get((i + 1) % 5));
        return context.spawn(hakker, names.get(i));
        }).forEach(hakker -> hakker.tell(Think.INSTANCE));

        return Behaviors.empty();
    }

}
