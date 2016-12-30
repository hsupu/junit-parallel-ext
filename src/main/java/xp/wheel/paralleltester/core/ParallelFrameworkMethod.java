package xp.wheel.paralleltester.core;

import java.lang.reflect.*;

import org.junit.runners.model.FrameworkMethod;

/**
 * @author xp
 */
public class ParallelFrameworkMethod extends FrameworkMethod {

    private String tag;
    private int count;

    public ParallelFrameworkMethod(Method method, String tag, int count) {
        super(method);
        this.tag = tag;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public int getCount(){
        return count;
    }

}
