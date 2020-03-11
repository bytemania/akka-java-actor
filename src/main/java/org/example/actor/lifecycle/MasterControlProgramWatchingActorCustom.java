package org.example.actor.lifecycle;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MasterControlProgramWatchingActorCustom extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(MasterControlProgramWatchingActorCustom::new);
    }

    private MasterControlProgramWatchingActorCustom(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(SpawnJobCustom.class, this::onSpawnJob)
                .onMessage(JobTerminated.class, this::onJobTerminated)
                .build();
    }

    private Behavior<Command> onSpawnJob(SpawnJobCustom message) {
        getContext().getSystem().log().info("Spawning job {}!", message.getName());
        ActorRef<Command> job = getContext().spawn(Job.create(message.getName()), message.getName());
        getContext().watchWith(job, JobTerminated.of(message.getName(), message.getReplyToWhenDone()));
        return this;
    }

    private Behavior<Command> onJobTerminated(JobTerminated terminated) {
        getContext().getSystem().log().info("Job stopped: {}", terminated.getName());
        terminated.getReplyToWhenDone().tell(JobDone.of(terminated.getName()));
        return this;
    }
}
