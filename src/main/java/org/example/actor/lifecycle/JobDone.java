package org.example.actor.lifecycle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class JobDone implements Command {
    private final String name;
}
