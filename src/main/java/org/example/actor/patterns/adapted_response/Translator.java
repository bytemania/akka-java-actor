package org.example.actor.patterns.adapted_response;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Translator extends AbstractBehavior<Command> {

    public static Behavior<Command> create(ActorRef<Request> backend) {
        return Behaviors.setup(ctx -> new Translator(ctx, backend));
    }

    private final ActorRef<Request> backend;
    private final ActorRef<Response> backendResponseAdapter;

    private int taskIdCounter = 0;
    private Map<Integer, ActorRef<URI>> inProgress = new HashMap<>();

    private Translator(ActorContext<Command> context, ActorRef<Request> backend) {
        super(context);
        this.backend = backend;
        this.backendResponseAdapter = context.messageAdapter(Response.class, WrappedBackendResponse::of);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Translate.class, this::onTranslate)
                .onMessage(WrappedBackendResponse.class, this::onWrappedBackendResponse)
                .build();
    }

    private Behavior<Command> onTranslate(Translate cmd) {
        taskIdCounter += 1;
        inProgress.put(taskIdCounter, cmd.getReplyTo());
        backend.tell(StartTranslationJob.of(taskIdCounter, cmd.getSite(), backendResponseAdapter));
        return this;
    }

    private Behavior<Command> onWrappedBackendResponse(WrappedBackendResponse wrapped) {
        Response response = wrapped.getResponse();
        if (response instanceof JobStarted) {
            JobStarted rsp = (JobStarted) response;
            getContext().getLog().info("Started {} msg {}", rsp.getTaskId(), rsp);
        } else if (response instanceof JobProgress) {
            JobProgress rsp = (JobProgress) response;
            getContext().getLog().info("Progress {} msg {}", rsp.getTaskId(), rsp);
        } else if (response instanceof JobCompleted) {
            JobCompleted rsp = (JobCompleted) response;
            getContext().getLog().info("Completed {} msg {}", rsp.getTaskId(), rsp);
            ActorRef<URI> requester = inProgress.get(rsp.getTaskId());
            requester.tell(rsp.getResut());
            inProgress.remove(rsp.getTaskId());
        } else {
            return Behaviors.unhandled();
        }

        return this;
    }

}
