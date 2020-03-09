package org.example.actor.lifecycle;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MasterControlProgram extends AbstractBehavior<Command> {
    public static Behavior<Command> create() {
        return Behaviors.setup(MasterControlProgram::new);
    }

    private MasterControlProgram(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(SpawnJob.class, this::onSpawnJob)
                .onMessage(GracefulShutdown.class, message -> onGracefulShutdown())
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onSpawnJob(SpawnJob message) {
        getContext().getSystem().log().info("Spawning job {}!", message.getName());
        getContext().spawn(Job.create(message.getName()), message.getName());
        return this;
    }

    private Behavior<Command> onGracefulShutdown() {
        getContext().getSystem().log().info("Initiating graceful shutdown...");
        return Behaviors.stopped(() -> getContext().getSystem().log().info("Cleanup!"));
    }

    private Behavior<Command> onPostStop() {
        getContext().getSystem().log().info("Master Control Program stopped");
        return this;
    }

    public static void main(String[] args) {
        final ActorSystem<Command> system = ActorSystem.create(MasterControlProgram.create(), "B6700");
        system.tell(SpawnJob.of("a"));
        system.tell(SpawnJob.of("b"));

        system.tell(GracefulShutdown.INSTANCE);
    }

}
