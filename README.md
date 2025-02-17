# L2Cache

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ck-jesse/l2cache-core?color=green)](https://search.maven.org/search?q=g:io.github.ck-jesse%20AND%20a:l2cache-core)
[![Crates.io](https://img.shields.io/crates/l/ap)](https://www.apache.org/licenses/LICENSE-2.0.html)

# 一、简介

L2Cache 是一个基于`内存`、 `Redis` 、 `Spring Cache` 实现的满足高并发场景下的分布式二级缓存框架。

# 二、如何使用
- [如何使用L2cache](https://blog.csdn.net/icansoicrazy/article/details/125097730)


# 三、重要数据
- 已在生产环境投产，目前主要应用在商品、优惠券、用户、营销等核心服务。
- 经历过多年双十一、双十二，以及`多次大促活动的流量洗礼`。
- 支撑公司单月`10亿GMV`
- 支撑全链路压测`35W QPS`


# 四、核心功能
- **支持多种缓存类型：** 一级缓存、二级缓存、混合缓存
- **解决痛点问题：** 缓存击穿、缓存穿透等
- **动态缓存配置：** 支持动态调整混合缓存下的缓存类型，支持热key的手动配置
- **缓存一致性保证：** 通过消息通知的方式来保证集群环境下一级缓存的一致性
- **自动热key探测：** 自动识别热key并缓存到一级缓存
- **支持缓存批量操作：** 支持分页的批量获取、批量删除等
- **定义通用缓存层：** 承上启下，简化业务开发，规整业务代码

## **1、同其他开源框架的对比**

| 核心功能       | JetCache（阿里）                        | J2Cache（OSChina）                    | L2Cache                              |
| -------------- | ------------------------------------ | ------------------------------------ | ------------------------------------ |
| 支持的缓存类型 | 一级缓存<br />二级缓存<br />混合缓存 | 一级缓存<br />二级缓存<br />混合缓存 | 一级缓存<br />二级缓存<br />混合缓存 |
| 解决的痛点问题 | 缓存击穿<br />缓存穿透               | 缓存击穿<br />缓存穿透               | 缓存击穿<br />缓存穿透               |
| 缓存一致性保证 | 支持                                 | 支持                                 | 支持                                 |
| 动态缓存配置   | 不支持                               | 不支持                               | 支持                                 |
| 自动热key探测  | 不支持                               | 不支持                               | 支持                                 |
| 缓存批量操作   | 不支持                               | 不支持                               | 支持                                 |
| 通用缓存层     | 不支持                               | 不支持                               | 支持                                 |
> 说明：上面表格的对比，数据正在整理中，后续会再校对一次。

- 从上面表格的对比可发现，`L2Cache` 的核心优势为三个点：`自动热key探测`、`缓存批量操作`、`通用缓存层`。
- 这三点优势是从实际业务开发中沉淀下来的能力，在屏蔽多级缓存基本能力的复杂性的基础上，进一步屏蔽了业务维度的缓存操作的复杂性，让原本需要资深开发者才能开发的功能，现在高级和中级开发者都可以简单、高效、高质的进行开发。
- 实际业务开发中，如果你也遇到开发难度高，难以维护，难以扩展的痛点问题，建议可以试试L2Cache。反正接入成本低，试试又何妨？



## **2、L2Cache 的二级缓存结构**

1、L1：一级缓存，内存缓存，支持 `Caffeine` 和 `Guava Cache`。

2、L2：二级缓存，集中式缓存，支持 `Redis`。

3、混合缓存，指支持同时使用一级缓存和二级缓存。

由于大量的缓存读取会导致 `L2` 的网络成为整个系统的瓶颈，因此 `L1` 的目标是降低对 `L2` 的读取次数。避免使用独立缓存系统所带来的网络IO开销问题。`L2` 可以避免应用重启后导致的 `L1`数据丢失的问题，同时无需担心`L1`会增加太多的内存消耗，因为你可以设置 `L1`中缓存数据的数量。

 **说明：**


> `L2Cache` 满足CAP定理中的AP，也就是满足可用性和分区容错性，至于C(一致性)因为缓存的特性所以无法做到强一致性，只能尽可能的去做到一致性，保证最终的一致。


## **3、关键点：**

支持根据配置`缓存类型`来灵活的组合使用不同的Cache。

1、支持只使用一级缓存`Caffeine` 和 `Guava Cache`。

2、支持只使用二级缓存`Redis`。

3、支持同时使用一二级缓存`Composite`。

 **必知：**

> 若使用缓存，则必然可能出现不一致的情况，也就是说无法保证强一致性。


# 五、实战问题
- [记录一次阿里云Redis版超出最大内存限制异常](https://editor.csdn.net/md/?articleId=108810679)
- [实战-l2cache-Caffeine的OOM异常分析](https://blog.csdn.net/icansoicrazy/article/details/108923992)
- [实战-l2cache高并发场景下出现OOM的分析和优化方案](https://blog.csdn.net/icansoicrazy/article/details/112274052)
- [实战-l2cache中caffeine.getIfPresent()仅仅获取缓存，但触发了数据加载，导致被设置为NullValue的问题分析
  ](https://blog.csdn.net/icansoicrazy/article/details/125096876)
