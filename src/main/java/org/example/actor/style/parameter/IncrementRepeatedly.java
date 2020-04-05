package org.example.actor.style.parameter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class IncrementRepeatedly implements Command {
    private final Duration interval;
}
