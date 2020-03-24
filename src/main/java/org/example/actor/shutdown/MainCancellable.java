package org.example.actor.shutdown;

import akka.Done;
import akka.actor.Cancellable;
import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MainCancellable {

    private static CompletionStage<Done> cleanup() {
        System.out.println("FREE YOUR RESOURCES HERE");
        return CompletableFuture.completedStage(Done.getInstance());
    }

    public static void main(String[] args) {
        final ActorSystem<String> system = ActorSystem.create(SomeGuardianActor.create(), "guardian");
        Cancellable cancellable = CoordinatedShutdown.get(system)
                    .addCancellableTask(CoordinatedShutdown.PhaseBeforeServiceUnbind(),
                "someTaskCleanUp", MainCancellable::cleanup);

        //LATER
        cancellable.cancel();

        CoordinatedShutdown.get(system).runAll(CoordinatedShutdown.unknownReason());
    }
}
