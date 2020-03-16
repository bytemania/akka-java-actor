package org.example.actor.patterns.future_to_self;

import akka.Done;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletionStage;

class CustomerRepository extends AbstractBehavior<Command> {

    private static final int MAX_OPERATIONS_IN_PROGRESS = 10;

    @AllArgsConstructor(staticName = "of")
    private static class WrappedUpdateResult implements Command {
        private final OperationResult result;
        private final ActorRef<OperationResult> replyTo;
    }

    public static Behavior<Command> create(CustomerDataAccess dataAccess) {
        return Behaviors.setup(context -> new CustomerRepository(context, dataAccess));
    }

    private final CustomerDataAccess dataAccess;
    private int operationsInProgress;

    private CustomerRepository(ActorContext<Command> context, CustomerDataAccess dataAccess) {
        super(context);
        this.dataAccess = dataAccess;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Update.class, this::onUpdate)
                .onMessage(WrappedUpdateResult.class, this::onUpdateResult)
                .build();
    }

    private Behavior<Command> onUpdate(Update command) {
        if (operationsInProgress == MAX_OPERATIONS_IN_PROGRESS) {
            command.getReplyTo().tell(UpdateFailure.of(command.getCustomer().getId(),
                    "Max " + MAX_OPERATIONS_IN_PROGRESS + " concurrent operations supported."));
        } else {
            operationsInProgress++;
            CompletionStage<Done> futureResult = dataAccess.update(command.getCustomer());
            getContext().pipeToSelf(futureResult, (ok, exc) -> exc == null
                ? WrappedUpdateResult.of(UpdateSuccess.of(command.getCustomer().getId()), command.getReplyTo())
                : WrappedUpdateResult.of(UpdateFailure.of(command.getCustomer().getId(), exc.getMessage()), command.getReplyTo()));
        }
        return this;
    }

    private Behavior<Command> onUpdateResult(WrappedUpdateResult wrapped) {
        operationsInProgress--;
        wrapped.replyTo.tell(wrapped.result);
        return this;
    }
}
