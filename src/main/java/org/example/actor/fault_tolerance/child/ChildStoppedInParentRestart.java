package org.example.actor.fault_tolerance.child;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChildStoppedInParentRestart {

    static Behavior<String> parentWithNoRestartChild() {
        return Behaviors.<String>setup(
                ctx -> {
                    final ActorRef<String> child1 = ctx.spawn(child(0), "child1");
                    final ActorRef<String> child2 = ctx.spawn(child(0), "child2");

                    return Behaviors.<String>supervise(Behaviors.receiveMessage(msg -> {
                        if ("FAIL".equals(msg)) throw new RuntimeException("ERROR");

                        String[] parts = msg.split(" ");
                        child1.tell(parts[0]);
                        child2.tell(parts[1]);
                        return Behaviors.same();
                    })).onFailure(SupervisorStrategy.restart().withStopChildren(false));
                }
        );
    }

    static Behavior<String> parent() {
        return Behaviors.<String>supervise(Behaviors.setup(ctx -> {
            final ActorRef<String> child1 = ctx.spawn(child(0), "child1");
            final ActorRef<String> child2 = ctx.spawn(child(0), "child2");

            return Behaviors.receiveMessage(msg -> {
                if ("FAIL".equals(msg)) throw new RuntimeException("ERROR");

                String[] parts = msg.split(" ");
                child1.tell(parts[0]);
                child2.tell(parts[1]);
                return Behaviors.same();
            });
        })).onFailure(SupervisorStrategy.restart());
    }

    private static Behavior<String> child(long size) {
        return Behaviors.receiveMessage(msg ->{
            System.out.println("THREAD " + Thread.currentThread().getId() +  " CHILD RECEIVED MESSAGE "  + size + msg.length());
            return child(size + msg.length());
        });
    }

}
