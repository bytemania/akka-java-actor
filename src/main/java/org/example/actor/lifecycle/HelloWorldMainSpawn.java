package org.example.actor.lifecycle;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.actor.introduction.chat.ChatRoom;
import org.example.actor.introduction.hello_world.Greet;
import org.example.actor.introduction.hello_world.Greeted;
import org.example.actor.introduction.hello_world.HelloWorld;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HelloWorldMainSpawn {

    public static Behavior<SpawnProtocol.Command> create() {
        return Behaviors.setup(context -> {
            //context.spawn(ChatRoom.create(), "chatRoom");
            return SpawnProtocol.create();
        });
    }

    public static void main(String[] args) {
        final ActorSystem<SpawnProtocol.Command> system = ActorSystem.create(HelloWorldMainSpawn.create(), "hello");
        final Duration timeout = Duration.ofSeconds(3);

        CompletionStage<ActorRef<Greet>> greeter =
                AskPattern.ask(system,
                        replyTo -> new SpawnProtocol.Spawn<>(HelloWorld.create(), "greeter", Props.empty(), replyTo),
                        timeout, system.scheduler());

        Behavior<Greeted> greetedBehavior = Behaviors.receive((context, message) -> {
            context.getLog().info("Greeting for {} from {}", message.getWhom(), message.getFrom());
            return Behaviors.stopped();
        });

        CompletionStage<ActorRef<Greeted>> greetedReplyTo = AskPattern.ask(system,
                replyTo -> new SpawnProtocol.Spawn<>(greetedBehavior, "", Props.empty(), replyTo), timeout,
                system.scheduler());

        greeter.whenComplete((greeterRef, exc) -> {
            if (exc == null) {
                greetedReplyTo.whenComplete((greetedReplyToRef, exc2) -> {
                    if (exc2 == null) {
                        greeterRef.tell(Greet.of("Akka", greetedReplyToRef));
                    }
                });
            }
        });
    }
}
