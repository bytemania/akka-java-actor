package org.example.actor.patterns.latency_tail_chopping;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class ResponsePrice implements PriceRequester.Command {
    private final Integer requestId;
    private final BigDecimal price;
}
