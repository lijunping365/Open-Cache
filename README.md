# Open-Cache

企业级开源分布式缓存项目

架构设计

1. 通过 Dashboard 管理监控我们的缓存

Dashboard 通过 MQ 广播同步缓存消息给应用的各个节点

2. 应用缓存指标数据上报给 Dashboard 使用 消息中间件

内存缓存同步使用哪个 MQ 指标数据就使用哪个 MQ

3. Dashboard 主动查询缓存节点的缓存指标数据，查询缓存节点上有哪些 cacheName，以及操作缓存节点上的缓存数据

这些都使用 Open-Light-Rpc

## 多个应用使用统一的可视化程序进行管理

假设我们有用户系统，有订单系统... 这些系统都是集群模式，我们需要使用一个统一的可视化程序进行管理这些系统的缓存

1. 可以通过该程序手动清除某个系统的某个缓存
2. 可以查看各个系统的缓存的命中率... 参数
3. ...


同一时刻，应用中各个实例缓存情况做对比形成图表，

1. 包括应用内所有节点的全部 cacheName 的对比图

2. 也包括应用内所有节点的某个 cacheName 的对比图

指标分当前和历史，缓存指标都存在单节点和多节点的问题

1. 单个节点：可以查看某个节点的某个 cacheName 情况，也可以查看某个节点的多个 cacheName 情况

2. 多个节点：可以对比多个节点某个 cacheName 情况，多个节点多个 cacheName 情况


1. 原则：应用 -> cacheName -> 节点

2. 缓存操作可以使用负载均衡，只调用一个节点就行，因为调用一个之后它会通知其他节点同步缓存。

3. cacheName 可能之前存在，但是现在不存在了这种情况。