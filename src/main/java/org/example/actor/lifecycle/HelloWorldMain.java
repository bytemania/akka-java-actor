package org.example.actor.lifecycle;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.example.actor.introduction.hello_world.Greet;
import org.example.actor.introduction.hello_world.HelloWorld;
import org.example.actor.introduction.hello_world.SayHello;

class HelloWorldMain extends AbstractBehavior<SayHello> {
    static Behavior<SayHello> create() {
        return Behaviors.setup(HelloWorldMain::new);
    }

    final ActorRef<Greet> greeter;

    private HelloWorldMain(ActorContext<SayHello> context) {
        super(context);
        greeter = context.spawn(HelloWorld.create(), "greeter");
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder().build();
    }

    public static void main(String[] args) {
        final ActorSystem<SayHello> system = ActorSystem.create(HelloWorldMain.create(), "hello");
        system.tell(SayHello.of("World"));
        system.tell(SayHello.of("Akka"));
    }
}
