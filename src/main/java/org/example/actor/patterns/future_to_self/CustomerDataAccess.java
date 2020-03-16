package org.example.actor.patterns.future_to_self;

import akka.Done;

import java.util.concurrent.CompletionStage;

interface CustomerDataAccess {
    CompletionStage<Done> update(Customer customer);
}
