package org.example.actor.stash;

import akka.Done;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.StashBuffer;

class DataAccess {

    public static Behavior<Command> create(String id, DB db) {
        return Behaviors.withStash(
                100,
                stash -> Behaviors.setup(
                        ctx -> {
                            ctx.pipeToSelf(db.load(id),
                                    (value, cause) -> {
                                        if (cause == null) return InitialState.of(value);
                                        else return DBError.of(new RuntimeException(cause));
                                    });
                            return new DataAccess(ctx, stash, id, db).start();
                        }));
    }

    private final ActorContext<Command> context;
    private final StashBuffer<Command> buffer;
    private final String id;
    private final DB db;

    private DataAccess(ActorContext<Command> context, StashBuffer<Command> buffer, String id, DB db) {
        this.context = context;
        this.buffer = buffer;
        this.id = id;
        this.db = db;
    }

    private Behavior<Command> start() {
        return Behaviors.receive(Command.class)
                .onMessage(InitialState.class, this::onInitialState)
                .onMessage(DBError.class, this::onDBError)
                .onMessage(Command.class, this::stashOtherCommand)
                .build();
    }

    private Behavior<Command> onInitialState(InitialState message) {
        return buffer.unstashAll(active(message.getValue()));
    }

    private Behavior<Command> active(String state) {
        return Behaviors.receive(Command.class)
                .onMessage(Get.class, message -> onGet(state, message))
                .onMessage(Save.class, this::onSave)
                .build();
    }

    private Behavior<Command> onGet(String state, Get message) {
        message.getReplyTo().tell(state);
        return Behaviors.same();
    }

    private Behavior<Command> onSave(Save message) {
        context.pipeToSelf(db.save(id, message.getPayload()),
                (value, cause) -> cause == null ? SaveSuccess.INSTANCE : DBError.of(new RuntimeException(cause)));
        return saving(message.getPayload(), message.getReplyTo());
    }

    private Behavior<Command> saving(String state, ActorRef<Done> replyTo) {
        return Behaviors.receive(Command.class)
                .onMessage(SaveSuccess.class, message -> onSaveSuccess(state, replyTo))
                .onMessage(DBError.class, this::onDBError)
                .onMessage(Command.class, this::stashOtherCommand)
                .build();
    }

    private Behavior<Command> onDBError(DBError message) {
        throw message.getCause();
    }

    private Behavior<Command> stashOtherCommand(Command message) {
        buffer.stash(message);
        return Behaviors.same();
    }

    private Behavior<Command> onSaveSuccess(String state, ActorRef<Done> replyTo) {
        replyTo.tell(Done.getInstance());
        return buffer.unstashAll(active(state));
    }
}
