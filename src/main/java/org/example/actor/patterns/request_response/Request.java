package org.example.actor.patterns.request_response;


import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class Request implements CommandRequest {
    private final String query;
    private final ActorRef<CommandResponse> replyTo;
}
