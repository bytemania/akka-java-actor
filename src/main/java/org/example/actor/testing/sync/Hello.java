package org.example.actor.testing.sync;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class Hello extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(Hello::new);
    }

    private Hello(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CreateAChild.class, this::onCreateAChild)
                .onMessage(CreateAnAnonymousChild.class, this::onCreateAnonymousChild)
                .onMessage(SayHelloToChild.class, this::onSayHelloToChild)
                .onMessage(SayHelloToAnonymousChild.class, this::onSayHelloToAnonymousChild)
                .onMessage(SayHello.class, this::onSayHello)
                .onMessage(LogAndSayHello.class, this::onLogAndSayHello)
                .build();
    }

    private Behavior<Command> onCreateAChild(CreateAChild message) {
        getContext().spawn(Child.create(), message.getChildName());
        return Behaviors.same();
    }

    private Behavior<Command> onCreateAnonymousChild(CreateAnAnonymousChild message) {
        getContext().spawnAnonymous(Child.create());
        return Behaviors.same();
    }

    private Behavior<Command> onSayHelloToChild(SayHelloToChild message) {
        ActorRef<String> child = getContext().spawn(Child.create(), message.getChildName());
        child.tell("hello");
        return Behaviors.same();
    }

    private Behavior<Command> onSayHelloToAnonymousChild(SayHelloToAnonymousChild message) {
        ActorRef<String> child = getContext().spawnAnonymous(Child.create());
        child.tell("hello stranger");
        return Behaviors.same();
    }

    private Behavior<Command> onSayHello(SayHello message) {
        message.getWho().tell("hello");
        return Behaviors.same();
    }

    private Behavior<Command> onLogAndSayHello(LogAndSayHello message) {
        getContext().getLog().info("Saying hello to {}", message.getWho().path().name());
        message.getWho().tell("hello");
        return Behaviors.same();
    }
}
