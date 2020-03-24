package org.example.actor.fsm.hakkers;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.time.Duration;

class Hakker {

    static Behavior<HakkerCommand> create(String name, ActorRef<ChopstickCommand> left, ActorRef<ChopstickCommand> right) {
        return Behaviors.setup(ctx -> new Hakker(ctx, name, left, right).waiting());
    }

    private final ActorContext<HakkerCommand> ctx;
    private final String name;
    private final ActorRef<ChopstickCommand> left;
    private final ActorRef<ChopstickCommand> right;
    private final ActorRef<Answer> adapter;

    public Hakker(ActorContext<HakkerCommand> ctx, String name, ActorRef<ChopstickCommand> left, ActorRef<ChopstickCommand> right) {
        this.ctx = ctx;
        this.name = name;
        this.left = left;
        this.right = right;
        this.adapter = ctx.messageAdapter(Answer.class, AdaptedAnswer::of);
    }

    private Behavior<HakkerCommand> waiting() {
        return Behaviors.receive(HakkerCommand.class)
                .onMessage(Think.class, msg -> {
                    ctx.getLog().info("{} starts to think", name);
                    return startThinking(Duration.ofSeconds(5));
                }).build();
    }

    //When a hakker is thinking it can become hungry and try to pick up its chopsticks and eat
    private Behavior<HakkerCommand> thinking() {
        return Behaviors.receive(HakkerCommand.class)
                .onMessageEquals(Eat.INSTANCE, () -> {
                    left.tell(Take.of(adapter));
                    right.tell(Take.of(adapter));
                    return hungry();
                }).build();
    }

    //When a hakker is hungry it tries to pick up its chopsticks and eat. When it picks one up, it goes into wait for
    //the other. If the hakkers first attempt at grabbing a chopstick fails, it starts to wait for the response of the
    //other grab
    private Behavior<HakkerCommand> hungry() {
        return Behaviors.receive(HakkerCommand.class)
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().getChopstick().equals(left), msg -> waitForOtherChopstick(right, left))
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().getChopstick().equals(right), msg -> waitForOtherChopstick(left, right))
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().isBusy(), msg -> firstChopstickDenied())
                .build();
    }

    //When a hakker is waiting for the last chopstick it can either obtain it and start eating, or the other chopstick
    // was busy and the hakker goes back to think about how he should obtain his chopsticks :-)
    private Behavior<HakkerCommand> waitForOtherChopstick(ActorRef<ChopstickCommand> chopstickToWaitFor,
                                                          ActorRef<ChopstickCommand> takenChopstick) {
        return Behaviors.receive(HakkerCommand.class)
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().getChopstick().equals(chopstickToWaitFor), msg -> {
                    ctx.getLog().info("{} has picked up {} and {} starts to eat",
                            name, left.path().name(), right.path().name());
                    return startEating(ctx, Duration.ofSeconds(5));
                })
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().getChopstick().equals(chopstickToWaitFor), msg -> {
                    takenChopstick.tell(Put.of(adapter));
                    return startThinking(Duration.ofMillis(10));
                })
                .build();
    }

    //When a hakker is eating, he can decide to start to think, then he puts down his chopsticks and starts to think
    private Behavior<HakkerCommand> eating() {
        return Behaviors.receive(HakkerCommand.class)
                .onMessageEquals(Think.INSTANCE, () -> {
                    ctx.getLog().info("{} puts down his chopsticks and starts to think", name);
                    left.tell(Put.of(adapter));
                    right.tell(Put.of(adapter));
                    return startThinking(Duration.ofSeconds(5));
                }).build();
    }

    //When the results of the other grab comes back, he needs to put it back if he got the other one.
    //Then go back and think and try to grab the chopsticks again
    private Behavior<HakkerCommand> firstChopstickDenied() {
        return Behaviors.receive(HakkerCommand.class)
                .onMessage(AdaptedAnswer.class, m -> m.getMsg().isTaken(), msg -> {
                    msg.getMsg().getChopstick().tell(Put.of(adapter));
                    return startThinking(Duration.ofMillis(10));
                })
                .build();
    }

    private Behavior<HakkerCommand> startThinking(Duration duration) {
        ctx.scheduleOnce(duration, ctx.getSelf(), Eat.INSTANCE);
        return thinking();
    }

    private Behavior<HakkerCommand> startEating(ActorContext<HakkerCommand> ctx, Duration duration) {
        ctx.scheduleOnce(duration, ctx.getSelf(), Think.INSTANCE);
        return eating();
    }
}
