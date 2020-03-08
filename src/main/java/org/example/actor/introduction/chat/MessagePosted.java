package org.example.actor.introduction.chat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class MessagePosted implements SessionEvent {
    private final String screenName;
    private final String message;
}
