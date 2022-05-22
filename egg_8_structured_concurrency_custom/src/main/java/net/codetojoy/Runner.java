
// note: I no longer own this domain
package net.codetojoy;

import java.util.*;
import java.util.stream.IntStream;

// javadoc here: https://download.java.net/java/early_access/loom/docs/api/

public class Runner {
    private Random random = new Random();
    private static final int MAX_DELAY_IN_SECONDS = 6;

    String taskFoo(int i) {
        String result = "";
        try {
            long delayInMillis = (random.nextInt(MAX_DELAY_IN_SECONDS) + 1) * 1000L;
            var name = "TaskFoo-" + i;
            var value = "foo-" + i;

            result = new Worker().doWork(delayInMillis, name, value);
        } catch (Exception ex) {
            System.err.println("TRACER foo caught ex: " + ex);
        }
        return result;
    }

    List<String> run() throws Exception {
        int numTasksForSuccess = 5;
        try (var scope = new CustomStructuredTaskScope<String>(numTasksForSuccess)) {
            int numTasks = 20;
            IntStream.range(0, numTasks).forEach(i -> scope.fork(() -> taskFoo(i))); 

            scope.join();

            return scope.results();
        }
    }

    public static void main(String... args) {
        var runner = new Runner(); 

        try {
            var results = runner.run();
            for (var result : results) {
                System.out.println("TRACER result: " + result.toString());
            }
        } catch (Exception ex) {
            System.err.println("TRACER caught exception: " + ex.getMessage());
        }
    }
}
