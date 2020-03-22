package org.example.actor.router;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Routers;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "of")
class GroupRouter {

    private static Behavior<Void> create() {
        return Behaviors.setup(context -> {
            ActorRef<Command> worker = context.spawn(Worker.create(), "worker");

            ServiceKey<Command> serviceKey = ServiceKey.create(Command.class, "log-worker");
            context.getSystem().receptionist().tell(Receptionist.register(serviceKey, worker));

            akka.actor.typed.javadsl.GroupRouter<Command> group = Routers.group(serviceKey);
            ActorRef<Command> router = context.spawn(group, "worker-group");

            for (int i = 0; i < 10; i ++) {
                router.tell(DoLog.of("msg " + i));
            }

            return Behaviors.receive(Void.class).onSignal(Terminated.class, signal -> Behaviors.stopped()).build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(GroupRouter.create(), "GroupRouterSystem");
    }

}
