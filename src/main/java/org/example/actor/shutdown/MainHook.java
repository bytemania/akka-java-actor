package org.example.actor.shutdown;

import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MainHook {
    public static void main(String[] args) {
        final ActorSystem<String> system = ActorSystem.create(SomeGuardianActor.create(), "guardian");
        CoordinatedShutdown.get(system).addJvmShutdownHook(() -> System.out.println("custom JVM shutdown hook..."));
    }
}
