package xp.wheel.paralleltester;

import java.util.*;
import java.util.stream.*;

import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import xp.wheel.paralleltester.core.*;

/**
 * @author xp
 */
public class ParallelRunner extends BlockJUnit4ClassRunner {

    public ParallelRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    private List<FrameworkMethod> testMethods;

    private List<ParallelFrameworkMethod> computeThreadedTestMethods() {
        List<ParallelFrameworkMethod> parallelMethods = new ArrayList<>();
        List<FrameworkMethod> methods;
        methods = getTestClass().getAnnotatedMethods(ThreadedGroup.class);
        for (FrameworkMethod method : methods) {
            ThreadedGroup group = method.getAnnotation(ThreadedGroup.class);
            for (Threaded anno : group.value()) {
                parallelMethods.add(new ParallelFrameworkMethod(method.getMethod(), anno.tag(), anno.number()));
            }
        }
        methods = getTestClass().getAnnotatedMethods(Threaded.class);
        for (FrameworkMethod method : methods) {
            Threaded anno = method.getAnnotation(Threaded.class);
            parallelMethods.add(new ParallelFrameworkMethod(method.getMethod(), anno.tag(), anno.number()));
        }
        return parallelMethods;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (testMethods != null) {
            return testMethods;
        }
        testMethods = Stream.concat(
                super.computeTestMethods().stream(),
                computeThreadedTestMethods().stream()
        ).collect(Collectors.toList());
        return testMethods;
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        return super.describeChild(method);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement;
        if (method instanceof ParallelFrameworkMethod) {
            ParallelFrameworkMethod pmethod = (ParallelFrameworkMethod) method;
            ParallelStatement pstatement = new RunInvokeMethod(pmethod, test);
            pstatement = withBeforeThreads(pmethod, test, pstatement);
            pstatement = withAfterThreads(pmethod, test, pstatement);
            statement = new ParallelInvokeMethod(pmethod, pstatement, test);
        } else {
            statement = super.methodInvoker(method, test);
        }
        statement = withBeforeCases(method, test, statement);
        statement = withAfterCases(method, test, statement);
        return statement;
    }

    private ParallelStatement withBeforeThreads(ParallelFrameworkMethod method, Object target, ParallelStatement statement) {
        List<FrameworkMethod> fixtures = getTestClass().getAnnotatedMethods(BeforeThread.class);
        String methodName = method.getName();
        List<FrameworkMethod> methods = fixtures.stream()
                .filter(e -> Objects.equals(methodName, e.getAnnotation(BeforeThread.class).value()))
                .collect(Collectors.toList());
        return methods.isEmpty() ? statement : new RunBeforeThreads(statement, methods, target);
    }

    private ParallelStatement withAfterThreads(ParallelFrameworkMethod method, Object target, ParallelStatement statement) {
        List<FrameworkMethod> fixtures = getTestClass().getAnnotatedMethods(AfterThread.class);
        String methodName = method.getName();
        List<FrameworkMethod> results = fixtures.stream()
                .filter(e -> Objects.equals(methodName, e.getAnnotation(AfterThread.class).value()))
                .collect(Collectors.toList());

        return results.isEmpty() ? statement : new RunAfterThreads(statement, results, target);
    }

    private Statement withBeforeCases(FrameworkMethod method, Object target, Statement statement) {
        List<FrameworkMethod> fixtures = getTestClass().getAnnotatedMethods(BeforeCase.class);
        String methodName = method.getName();
        List<FrameworkMethod> methods = fixtures.stream()
                .filter(e -> Objects.equals(methodName, e.getAnnotation(BeforeCase.class).value()))
                .collect(Collectors.toList());
        return methods.isEmpty() ? statement : new RunBeforeCases(statement, methods, target);
    }

    private Statement withAfterCases(FrameworkMethod method, Object target, Statement statement) {
        List<FrameworkMethod> fixtures = getTestClass().getAnnotatedMethods(AfterCase.class);
        String methodName = method.getName();
        List<FrameworkMethod> results = fixtures.stream()
                .filter(e -> Objects.equals(methodName, e.getAnnotation(AfterCase.class).value()))
                .collect(Collectors.toList());

        return results.isEmpty() ? statement : new RunAfterCases(statement, results, target);
    }

}
