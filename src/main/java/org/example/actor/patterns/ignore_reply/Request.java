package org.example.actor.patterns.ignore_reply;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class Request {
    private final String message;
    private final ActorRef<ResponseCookie> replyTo;
}
