/**
 *
 */
package cc.xpcas.junit.ext.parallel.core;

import java.util.*;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class RunBeforeCases extends Statement {

    private final Statement statement;
    private final List<FrameworkMethod> fixtures;
    private final Object target;

    public RunBeforeCases(Statement statement, List<FrameworkMethod> fixtures, Object target) {
        this.statement = statement;
        this.fixtures = fixtures;
        this.target = target;
    }

    @Override
    public void evaluate() throws Throwable {
        for (FrameworkMethod fixture : fixtures) {
            fixture.invokeExplosively(target);
        }
        statement.evaluate();
    }
}