package org.example.actor.patterns.adapted_response;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.net.URI;
import java.net.URISyntaxException;

public class Backend extends AbstractBehavior<Request> {

    public static Behavior<Request> create() {
        return Behaviors.setup(Backend::new);
    }

    private Backend(ActorContext<Request> context) {
        super(context);
    }

    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(StartTranslationJob.class, this::onStartTranslationJob)
                .build();
    }

    private Behavior<Request> onStartTranslationJob(StartTranslationJob job) {
        job.getReplyTo().tell(JobStarted.of(job.getTaskId()));
        job.getReplyTo().tell(JobProgress.of(job.getTaskId(), 0.25));
        job.getReplyTo().tell(JobProgress.of(job.getTaskId(), 0.50));
        job.getReplyTo().tell(JobProgress.of(job.getTaskId(), 0.75));

        URI uri;
        try {
            uri = new URI("https://akka.io/docs/sv/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            uri = null;
        }

        job.getReplyTo().tell(JobCompleted.of(job.getTaskId(), uri));

        return Behaviors.same();
    }
}
