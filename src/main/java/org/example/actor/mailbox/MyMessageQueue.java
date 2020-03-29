package org.example.actor.mailbox;

import akka.actor.ActorRef;
import akka.dispatch.Envelope;
import akka.dispatch.MessageQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class MyMessageQueue implements MessageQueue, MyUnboundedMessageQueueSemantics {

    private final Queue<Envelope> queue;

    MyMessageQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void enqueue(ActorRef receiver, Envelope handle) {
        queue.offer(handle);
    }

    @Override
    public Envelope dequeue() {
        return queue.poll();
    }

    @Override
    public int numberOfMessages() {
        return queue.size();
    }

    @Override
    public boolean hasMessages() {
        return !queue.isEmpty();
    }

    @Override
    public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
        queue.forEach(handle -> deadLetters.enqueue(owner, handle));
    }
}
