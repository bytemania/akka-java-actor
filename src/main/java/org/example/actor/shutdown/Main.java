package org.example.actor.shutdown;

import akka.Done;
import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Main {

    private static CompletionStage<Done> cleanup() {
        System.out.println("FREE YOUR RESOURCES HERE");
        return CompletableFuture.completedStage(Done.getInstance());
    }

    public static void main(String[] args) {
        final ActorSystem<String> system = ActorSystem.create(SomeGuardianActor.create(), "guardian");
        CoordinatedShutdown.get(system)
                .addTask(
                        CoordinatedShutdown.PhaseBeforeServiceUnbind(),
                        "someTaskName",
                        Main::cleanup);

        Runtime.getRuntime().addShutdownHook(new Thread(system::terminate));

        CoordinatedShutdown.get(system).runAll(CoordinatedShutdown.unknownReason());
    }
}
