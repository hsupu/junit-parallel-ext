/**
 *
 */
package cc.xpcas.junit.ext.parallel.core;

import java.util.*;

import org.junit.runners.model.FrameworkMethod;

public class RunBeforeThreads extends ParallelStatement {

    private final ParallelStatement statement;
    private final List<FrameworkMethod> fixtures;
    private final Object target;

    public RunBeforeThreads(ParallelStatement statement, List<FrameworkMethod> fixtures, Object target) {
        this.statement = statement;
        this.fixtures = fixtures;
        this.target = target;
    }

    @Override
    public void evaluate(String tag, int threadNum) throws Throwable {
        for (FrameworkMethod fixture : fixtures) {
            fixture.invokeExplosively(target, tag, threadNum);
        }
        statement.evaluate(tag, threadNum);
    }
}