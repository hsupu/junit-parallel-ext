/**
 *
 */
package cc.xpcas.junit.ext.parallel.core;

import java.util.*;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class RunAfterCases extends Statement {

    private final Statement statement;
    private final List<FrameworkMethod> fixtures;
    private final Object target;

    public RunAfterCases(Statement statement, List<FrameworkMethod> fixtures, Object target) {
        this.statement = statement;
        this.fixtures = fixtures;
        this.target = target;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<>();
        errors.clear();
        try {
            statement.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            for (FrameworkMethod fixture : fixtures) {
                try {
                    fixture.invokeExplosively(target);
                } catch (Throwable e) {
                    errors.add(e);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}