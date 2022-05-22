# Caffeine

## 特点

1. 有多种淘汰
2. 并且支持淘汰通知
3. 目前Spring也在推荐使用，Caffeine 因使用 Window TinyLfu 回收策略，提供了一个近乎最佳的命中率。

- 自动把数据加载到本地缓存中，并且可以配置异步；
- 基于数量剔除策略；
- 基于失效时间剔除策略，这个时间是从最后一次操作算起【访问或者写入】；
- 异步刷新；
- Key会被包装成Weak引用；
- Value会被包装成Weak或者Soft引用，从而能被GC掉，而不至于内存泄漏；
- 数据剔除提醒；
- 写入广播机制；
- 缓存访问可以统计；

## Caffeine的内部结构

1. Cache的内部包含着一个ConcurrentHashMap，这也是存放我们所有缓存数据的地方，众所周知，ConcurrentHashMap是一个并发安全的容器，这点很重要，可以说Caffeine其实就是一个被强化过的ConcurrentHashMap。
2. Scheduler，定期清空数据的一个机制，可以不设置，如果不设置则不会主动的清空过期数据。
3. Executor，指定运行异步任务时要使用的线程池。可以不设置，如果不设置则会使用默认的线程池，也就是ForkJoinPool.commonPool()