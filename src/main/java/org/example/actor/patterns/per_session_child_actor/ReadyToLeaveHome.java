package org.example.actor.patterns.per_session_child_actor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class ReadyToLeaveHome {
    private final String who;
    private final Keys keys;
    private final Wallet wallet;
}
