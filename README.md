## Parallel Runner

A JUnit extension to enable parallel tests.

Welcome star.

### Dependency

- Java 8
- JUnit 4

Maven (not available yet):

```xml
<dependency>
    <groupId>xp.wheel</groupId>
    <artifactId>parallel-tester</artifactId>
    <version>0.1</version>
</dependency>
```

### How to Use

`@RunWith(ParallelRunner.class)`

Add this annotation to the test class to run normal tests and to enable parallel tests.

### Annotations

`@Threaded([tag = <string>], number = <int>)` *Repeatable*

Mark a method as a parallel test case, and the number of threads is set by parameters.

*Optional* Because the annotation is repeatable, you can distinct different test case by specifying tags.

`@BeforeThread("threadedMethodName")`

Run before **every thread** start.

`@AfterThread("threadedMethodName")`

Run after **every thread** finished.

`@BeforeCase("threadedMethodName")`

Run before target test case start. After @Before.

`@AfterCase("threadedMethodName")`

Run after target test case finished. Before @After.

### Known issues

We can't set different description for repeated test case because JUnit check it's description name to confirm whether it should be run.

### Sample

```java
package xp.wheel.paralleltester.test;

import java.util.*;
import java.util.concurrent.*;

import org.junit.runner.RunWith;

import xp.wheel.paralleltester.*;

/**
 * @author xp
 */
@RunWith(ParallelRunner.class)
public class Sample {

    private ConcurrentHashMap<String, Integer> map;

    private Random random = new Random();

    @BeforeCase("test")
    public void setUp() {
        map = new ConcurrentHashMap<>();
    }

    @BeforeThread("test")
    public void beforeThread(String tag, int threadNum) {
        map.putIfAbsent(tag, -1);
    }

    @Threaded(tag = "a", number = 100)
    @Threaded(tag = "b", number = 10)
    public void test(String tag, int threadNum) throws InterruptedException {
        map.computeIfPresent(tag, (k, v) -> v + 1);
        Thread.sleep(random.nextInt(100));
    }

    @AfterThread("test")
    public void afterThread(String tag, int threadNum) {
        int value = map.get(tag);
        System.out.println(tag + "[" + threadNum + "] " + value);
    }

    @AfterCase("test")
    public void tearDown() {
        System.out.println("count: " + map.size());
        map = null;
    }

}
```