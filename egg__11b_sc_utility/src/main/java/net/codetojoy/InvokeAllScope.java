
// note: I no longer own this domain
package net.codetojoy;

import java.util.*;
import java.util.stream.Stream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import jdk.incubator.concurrent.StructuredTaskScope;

// javadoc here: https://download.java.net/java/early_access/jdk19/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/package-summary.html

// NOTE: this is NOT production-ready, just an experiment
// 
// ref: https://github.com/openjdk/jdk19/blob/master/src/jdk.incubator.concurrent/share/classes/jdk/incubator/concurrent/StructuredTaskScope.java

public class InvokeAllScope<T> extends StructuredTaskScope<T> {
    private final Queue<T> results = new ConcurrentLinkedQueue<>();

    public InvokeAllScope() {
        super(null, Thread.ofVirtual().factory());
    }

    @Override
    protected void handleComplete(Future<T> future) {
        if (future.state() == Future.State.SUCCESS) {
            T result = future.resultNow();
            results.add(result);
        }
    }

    public Stream<T> forkAll(List<Callable<T>> tasks) throws Exception {
        for (var task : tasks) {
            this.fork(() -> task.call());
        }

        this.join();

        return this.results.stream();
    }
}
