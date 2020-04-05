package org.example.actor.style.parameter;

import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.TimerScheduler;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class Setup {
    private final String name;
    private final ActorContext<Command> context;
    private final TimerScheduler<Command> timers;
}
