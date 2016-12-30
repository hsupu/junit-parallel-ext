/**
 *
 */
package xp.wheel.paralleltester.core;

import java.util.*;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;

public class RunAfterThreads extends ParallelStatement {

    private final ParallelStatement statement;
    private final List<FrameworkMethod> fixtures;
    private final Object target;

    public RunAfterThreads(ParallelStatement statement, List<FrameworkMethod> fixtures, Object target) {
        this.statement = statement;
        this.fixtures = fixtures;
        this.target = target;
    }

    @Override
    public void evaluate(String tag, int threadNum) throws Throwable {
        List<Throwable> errors = new ArrayList<>();
        errors.clear();
        try {
            statement.evaluate(tag, threadNum);
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            for (FrameworkMethod fixture : fixtures) {
                try {
                    fixture.invokeExplosively(target, tag, threadNum);
                } catch (Throwable e) {
                    errors.add(e);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}