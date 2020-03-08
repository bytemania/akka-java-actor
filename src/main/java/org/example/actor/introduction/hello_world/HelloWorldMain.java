package org.example.actor.introduction.hello_world;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldMain extends AbstractBehavior<SayHello> {

    public static Behavior<SayHello> create() {
        return Behaviors.setup(HelloWorldMain::new);
    }

    private final ActorRef<Greet> greeter;

    public HelloWorldMain(ActorContext<SayHello> context) {
        super(context);
        greeter = context.spawn(HelloWorld.create(), "greeter");
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder()
                .onMessage(SayHello.class, this::onStart)
                .build();
    }

    private Behavior<SayHello> onStart(SayHello command) {
        ActorRef<Greeted> replyTo = getContext().spawn(HelloWorldBot.create(5), command.getName());
        greeter.tell(Greet.of(command.getName(), replyTo));
        return this;
    }
}
