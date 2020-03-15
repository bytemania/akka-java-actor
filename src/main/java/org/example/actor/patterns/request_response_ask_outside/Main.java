package org.example.actor.patterns.request_response_ask_outside;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Main {

    private static void askAndPrint(ActorSystem<Command> cookieFabricSystem) {
        CompletionStage<Reply> result = AskPattern.ask(
                cookieFabricSystem,
                replyTo -> GiveMeCookies.of(3, replyTo),
                Duration.ofSeconds(3),
                cookieFabricSystem.scheduler());

        result.whenComplete(
                (reply, failure) -> {
                    if (reply instanceof Cookies) System.out.println("Yay, " + ((Cookies) reply).getCount() + " cookies!");
                    else if (reply instanceof InvalidRequest) System.out.println("No cookies for me. " + ((InvalidRequest) reply).getReason());
                    else System.out.println("Boo! didn't get cookies in time." + failure);
                }
        );
    }

    private static void askDealError(ActorSystem<Command> cookieFabricSystem) {
        CompletionStage<Reply> result = AskPattern.ask(
                cookieFabricSystem,
                replyTo -> GiveMeCookies.of(3, replyTo),
                Duration.ofSeconds(3),
                cookieFabricSystem.scheduler()
        );

        CompletionStage<Cookies> cookies = result.thenCompose(reply -> {
                if (reply instanceof Cookies) return CompletableFuture.completedFuture((Cookies) reply);
                 else if (reply instanceof InvalidRequest) {
                    CompletableFuture<Cookies> failed = new CompletableFuture<>();
                    failed.completeExceptionally(new IllegalArgumentException(((InvalidRequest) reply).getReason()));
                    return failed;
                } else throw new IllegalStateException("Unexpected reply: " + reply.getClass());
            });

        cookies.whenComplete((cookiesReply, failure) -> {
            if (cookiesReply != null) System.out.println("Yay, " + cookiesReply.getCount() + " cookies");
            else System.out.println("Boo! didn't get cookies in time. " + failure);
        });
    }

    public static void main(String[] args) {
        final ActorSystem<Command> cookieFabricSystem = ActorSystem.create(CookieFabric.create(), "cookieFabric");
        Runtime.getRuntime().addShutdownHook(new Thread(cookieFabricSystem::terminate));

        askAndPrint(cookieFabricSystem);
        askDealError(cookieFabricSystem);
    }

}
