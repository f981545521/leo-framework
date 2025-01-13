package cn.acyou.leo.tool.test.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author youfang
 * @version [1.0.0, 2025/1/13 9:36]
 **/
@State(Scope.Thread)
public class HelloBenchmark {

    @Benchmark
    public void testMethod() throws InterruptedException {
        Thread.sleep(10);
    }

    @Test
    public void testBenchmark() throws Exception {
        Options options = new OptionsBuilder()
                .include(HelloBenchmark.class.getSimpleName())
                .forks(1) //进程数
                .threads(1) //线程数
                .warmupIterations(1)
                .measurementIterations(1)
                /**
                 * ◦ Throughput(吞吐量)，单位时间内的操作数。
                 * ◦ AverageTime(平均时间)，每个操作的平均时间。
                 * ◦ SampleTime(采样时间)，采样每一个操作的时间。该模式持续调用基准测试方法，随机采样方法调用所需要的时间，自动调整采样频率。
                 * ◦ SingleShotTime(单个操作的测量时间)，用于测量一次基准测试的调用时间。对于不想持续调用的场景，例如对于冷启动测试或者批处理测试很有用。
                 * 这个模式只会调用一次基准测试方法。 ◦ All，覆盖以上所有测试模式。
                 */
                .mode(Mode.Throughput)
                .build();
        new Runner(options).run();
    }
}