package org.example.actor.patterns.fire_forget;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
class Printer {
    static Behavior<PrintMe> create() {
        return Behaviors.setup(context -> Behaviors.receive(PrintMe.class)
                .onMessage(PrintMe.class,
                    printMe -> {
                        context.getLog().info(printMe.getMessage());
                        return Behaviors.same();
                    }).build());
    }

    public static void main(String[] args) {
        ActorSystem<PrintMe> system = ActorSystem.create(Printer.create(), "printer-sample-system");
        system.tell(PrintMe.of("message 1"));
    }

}
