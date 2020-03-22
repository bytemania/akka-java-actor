package org.example.actor.discovery;

import akka.actor.typed.receptionist.Receptionist;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class ListingResponse implements Command {
    private final Receptionist.Listing listing;
}
