package cc.xpcas.junit.ext.parallel.core;

public class RunInvokeMethod extends ParallelStatement {

    private final ParallelFrameworkMethod testMethod;
    private Object target;

    public RunInvokeMethod(ParallelFrameworkMethod testMethod, Object target) {
        this.testMethod = testMethod;
        this.target = target;
    }

    @Override
    public void evaluate(String tag, int threadNum) throws Throwable {
        testMethod.invokeExplosively(target, tag, threadNum);
    }
}
