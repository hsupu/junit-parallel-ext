package cc.xpcas.junit.ext.parallel.core;

/**
 * @author xp
 */
public abstract class ParallelStatement {

    public abstract void evaluate(String tag, int threadNum) throws Throwable;
}
