# MIT6.824-Java

MIT6.824 Java 版
--- 

MIT 的 6.824 课程是一门非常经典的分布式系统课程。当前版本的课程是由 Go 完成，在这之前是 C++。

同样是服务端编程语言，Java 更加流行，会的人也更多。如此看来，如果能有一个 Java 版的课程作业可供学习挑战，收益人应该还会更多。加上 Java
有更加丰富的并发编程库，这样也许能看到更多有意思的代码。

实现的功能分为三个模块

- MapReduce
- Raft
- RaftKV

和原版一样，由于作业是在模拟分布式环境，因此不同的物理节点在作业中是用不同的 Java 进程来表示。节点之间的通信是基于 Netty 实现的 RPC。项目是一个标准的 maven 工程。其中
common 里面是一些公用的 bean 和 rpc 实现。mapreduce，raft 和 raftkv 是三个不同的模块，之间没有依赖关系。每个模块只需要通过测试代码即可。

注：该项目在开发中。所有的进度和相关资料在我的 notion [分享页](https://razertory.notion.site/MIT6-824-Java-b88001d31b854031815bf73d8b5ef14d)中可以看到。

目前完成了

- RPC 框架，在 common 包里
- MapReduce 以及测试


