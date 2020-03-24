package org.example.actor.fsm.hakkers;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class AdaptedAnswer implements HakkerCommand {
    private final Answer msg;
}
