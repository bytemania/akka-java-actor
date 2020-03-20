package org.example.actor.patterns.respond_sharded_actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;

class Counter extends AbstractBehavior<Command> {
    static EntityTypeKey<Command> typeKey = EntityTypeKey.create(Command.class, "example-sharded-counter");

    public static Behavior<Command> create() {
        return Behaviors.setup(Counter::new);
    }

    private final ClusterSharding sharding;
    private int value;

    private Counter(ActorContext<Command> context) {
        super(context);
        this.sharding = ClusterSharding.get(context.getSystem());
        this.value = 0;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Increment.class, msg -> onIncrement())
                .onMessage(GetValue.class, this::onGetValue)
                .build();
    }

    private Behavior<Command> onIncrement() {
        value++;
        return this;
    }

    private Behavior<Command> onGetValue(GetValue msg) {
        EntityRef<Command> entityRef = sharding.entityRefFor(CounterConsumer.typeKey, msg.getReplyToEntityId());
        entityRef.tell(NewCount.of(value));
        return this;
    }

}
