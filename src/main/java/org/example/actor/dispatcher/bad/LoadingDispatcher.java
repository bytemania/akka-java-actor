package org.example.actor.dispatcher.bad;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.Behaviors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class LoadingDispatcher {
    private static Behavior<Void> dummyActor() {
        return Behaviors.setup(context -> Behaviors.receive(Void.class).onSignal(Terminated.class, signal -> Behaviors.stopped()).build());
    }

    private static Behavior<Void> create() {
        return Behaviors.setup(context ->
                {
                    context.spawn(LoadingDispatcher.dummyActor(), "DefaultDispatcherActor");
                    context.spawn(LoadingDispatcher.dummyActor(), "ExplicitDefaultDispatcherActor", DispatcherSelector.defaultDispatcher());
                    context.spawn(LoadingDispatcher.dummyActor(), "BlockingDispatcher", DispatcherSelector.blocking());
                    context.spawn(LoadingDispatcher.dummyActor(), "ParentDispatcher", DispatcherSelector.sameAsParent());
                    context.spawn(LoadingDispatcher.dummyActor(), "DispatcherFromConfig", DispatcherSelector.fromConfig("example-dispatcher"));
                    return Behaviors.receive(Void.class).onSignal(Terminated.class, signal -> Behaviors.stopped()).build();
                });
    }

    public static void main(String[] args) {
        ActorSystem<Void> system = ActorSystem.create(LoadingDispatcher.create(), "system");
        system.dispatchers().lookup(DispatcherSelector.fromConfig("example-dispatcher"));
    }

}
