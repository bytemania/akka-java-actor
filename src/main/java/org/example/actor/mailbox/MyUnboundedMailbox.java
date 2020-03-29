package org.example.actor.mailbox;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import scala.Option;

class MyUnboundedMailbox implements MailboxType, ProducesMessageQueue<MyMessageQueue> {

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new MyMessageQueue();
    }
}
