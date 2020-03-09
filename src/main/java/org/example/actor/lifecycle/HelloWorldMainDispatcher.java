package org.example.actor.lifecycle;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.actor.introduction.hello_world.Greet;
import org.example.actor.introduction.hello_world.HelloWorld;
import org.example.actor.introduction.hello_world.SayHello;

class HelloWorldMainDispatcher extends AbstractBehavior<SayHello> {

    static Behavior<SayHello> create() {
        return Behaviors.setup(HelloWorldMainDispatcher::new);
    }

    private final ActorRef<Greet> greeter;

    private HelloWorldMainDispatcher(ActorContext<SayHello> context) {
        super(context);

        final String dispatcherPath = "akka.actor.default-blocking-io-dispatcher";
        Props greeterProps = DispatcherSelector.fromConfig(dispatcherPath);
        greeter = getContext().spawn(HelloWorld.create(), "greeter", greeterProps);
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder().build();
    }
}
