package org.example.actor.introduction.hello_world;

import akka.actor.typed.ActorSystem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    public static void main(String[] args) {
        final ActorSystem<SayHello> system = ActorSystem.create(HelloWorldMain.create(), "hello");
        system.tell(SayHello.of("World"));
        system.tell(SayHello.of("Akka"));
        Runtime.getRuntime().addShutdownHook(new Thread(system::terminate));
    }
}
