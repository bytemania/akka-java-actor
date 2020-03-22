package org.example.actor.stash;

import akka.Done;

import java.util.concurrent.CompletionStage;

interface DB {
    CompletionStage<Done> save(String id, String value);
    CompletionStage<String> load(String id);
}
