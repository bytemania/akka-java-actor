package org.example.actor.style.factory;

import akka.Done;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class CountDown extends AbstractBehavior<Command> {

    public static Behavior<Command> create(int countDownFrom, ActorRef<Done> notifyWhenZero) {
        return Behaviors.setup(context -> new CountDown(context, countDownFrom, notifyWhenZero));
    }

    private final ActorRef<Done> notifyWhenZero;
    private int remaining;

    private CountDown(ActorContext<Command> context, int countDownFrom, ActorRef<Done> notifyWhenZero) {
        super(context);
        this.remaining = countDownFrom;
        this.notifyWhenZero = notifyWhenZero;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(Down.INSTANCE, this::onDown)
                .build();
    }

    private Behavior<Command> onDown() {
        remaining --;
        if (remaining == 0) {
            notifyWhenZero.tell(Done.getInstance());
            return Behaviors.stopped();
        } else {
            return this;
        }
    }

    public static void main(String[] args) {
        ActorSystem.create(Behaviors.setup(context -> {
            ActorRef<Done> doneActorRef = context.spawnAnonymous(Behaviors.receive(Done.class)
                    .onMessageEquals(Done.getInstance(), Behaviors::stopped).build());
            context.spawnAnonymous(CountDown.create(100, doneActorRef));
            return Behaviors.ignore();
        }), "system");
    }
}
