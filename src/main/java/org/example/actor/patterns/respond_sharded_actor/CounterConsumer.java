package org.example.actor.patterns.respond_sharded_actor;

import akka.cluster.sharding.typed.javadsl.EntityTypeKey;

class CounterConsumer {

    public static EntityTypeKey<Command> typeKey = EntityTypeKey.create(Command.class, "example-sharded-response");



}
