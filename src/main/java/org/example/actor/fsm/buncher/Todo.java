package org.example.actor.fsm.buncher;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class Todo implements Data {
    private final ActorRef<Batch> target;
    private final List<Object> queue;

    Todo addElement(Object element) {
        List<Object> nQueue = new LinkedList<>(queue);
        nQueue.add(element);
        return new Todo(this.target, nQueue);
    }

    Todo copy(List<Object> queue) {
        return new Todo(this.target, queue);
    }

    Todo copy(ActorRef<Batch> target) {
        return new Todo(target, this.queue);
    }
}
