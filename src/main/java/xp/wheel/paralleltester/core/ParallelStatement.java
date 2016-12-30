package xp.wheel.paralleltester.core;

/**
 * @author xp
 */
public abstract class ParallelStatement {

    public abstract void evaluate(String tag, int threadNum) throws Throwable;
}
