package org.example.actor.router;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.PoolRouter;
import akka.actor.typed.javadsl.Routers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Worker {

    static final Behavior<Command> create() {
        return Behaviors.setup(context -> {
           context.getLog().info("Starting worker");
           return Behaviors.receive(Command.class)
                   .onMessage(DoLog.class, doLog -> onDogLog(context, doLog))
                   .build();
        });
    }

    private static Behavior<Command> onDogLog(ActorContext<Command> context, DoLog doLog) {
        context.getLog().info("Got message {}", doLog.getText());
        return Behaviors.same();
    }

    private static Behavior<Void> system() {
        return Behaviors.setup(context -> {
            int poolSize = 4;
            PoolRouter<Command> pool = Routers.pool(poolSize, Behaviors.supervise(Worker.create())
                    .onFailure(SupervisorStrategy.restart()));

            ActorRef<Command> router = context.spawn(pool, "worker-pool");

            for (int i = 0; i < 10; i++) {
                router.tell(DoLog.of("msg " + i));
            }

            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, signal -> Behaviors.stopped())
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(Worker.system(), "PoolRouterClass");
    }

}
