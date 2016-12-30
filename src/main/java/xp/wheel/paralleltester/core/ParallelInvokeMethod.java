package xp.wheel.paralleltester.core;

import java.util.*;
import java.util.concurrent.*;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class ParallelInvokeMethod extends Statement {

    private final ParallelFrameworkMethod method;
    private final ParallelStatement statement;
    private final Object target;

    public ParallelInvokeMethod(ParallelFrameworkMethod method, ParallelStatement statement, Object target) {
        this.method = method;
        this.statement = statement;
        this.target = target;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new CopyOnWriteArrayList<>();
        errors.clear();
        String tag = method.getTag();
        int count = method.getCount();
        ExecutorService executor = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; ++i) {
            final int threadNum = i;
            executor.execute(() -> {
                try {
                    statement.evaluate(tag, threadNum);
                } catch (Throwable e) {
                    errors.add(e);
                }
            });
        }
        executor.shutdown();
        if (!executor.awaitTermination(0, TimeUnit.SECONDS)) {
            while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                if (!errors.isEmpty()) break;
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
