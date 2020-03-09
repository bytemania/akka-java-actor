package org.example.actor.lifecycle;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MasterControlProgramWatchingActor extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(MasterControlProgramWatchingActor::new);
    }

    private MasterControlProgramWatchingActor(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(SpawnJob.class, this::onSpawnJob)
                .onSignal(Terminated.class, this::onTerminated)
                .build();
    }

    private Behavior<Command> onSpawnJob(SpawnJob message) {
        getContext().getSystem().log().info("Spawning job {}!", message.getName());
        ActorRef<Command> job = getContext().spawn(Job.create(message.getName()), message.getName());
        getContext().watch(job);
        getContext().stop(job);
        return this;
    }

    private Behavior<Command> onTerminated(Terminated terminated) {
        getContext().getSystem().log().info("Job stopped: {}", terminated.getRef().path().name());
        return this;
    }

    public static void main(String[] args) {
        final ActorSystem<Command> system = ActorSystem.create(MasterControlProgramWatchingActor.create(), "B6700");
        system.tell(SpawnJob.of("a"));
        system.tell(SpawnJob.of("b"));

    }

}
