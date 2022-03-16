### @KafkaListener()

#### concurrency 参数

通过一个@KafkaListener注解消费3个topic，同时配置concurrency参数为10

那么将会产生10个listener线程，每个线程消费3个topic的一个分区

但如果topic只有2个分区则只有3*2个线程能消费消息，其余的线程将不会收到任何消息

因此 concurrency 应设置为与分区数一致

#### 并行测试1

topic: TOPIC_1, TOPIC_2

@KafkaListener(): 1个

concurrency=1

代码判断收到TOPIC_2的消息时 Thread.sleep(10000)

打印顺序：

TOPIC_2

TOPIC_1

#### 并行测试2

topic: TOPIC_1, TOPIC_2

@KafkaListener(): 1个

concurrency=10

代码判断收到TOPIC_2的消息时 Thread.sleep(10000)

打印顺序：

TOPIC_2

TOPIC_1

因为分区只有一个，虽然有10个消费者线程，但实际上消费的线程只有一个，且同时消费两个topic

#### 并行测试3

topic: TOPIC_1, TOPIC_2

@KafkaListener(): 2个

concurrency=1

代码判断收到TOPIC_2的消息时 Thread.sleep(10000)

打印顺序：

TOPIC_1

TOPIC_2

由两个线程各消费一个topic因此两个消息并行消费

#### 并行测试4

topic: TOPIC_1 两个分区, TOPIC_2

@KafkaListener(): 1个

concurrency=10

代码判断收到分区1的消息时 Thread.sleep(10000)

打印顺序：

TOPIC_1-0

TOPIC_1-1

因为有10个消费者线程

消费者1：TOPIC_1-0, TOPIC_2-0

消费者2：TOPIC_1-1

其余无消费

因此此时由两个消费者分别消费两个分区的消息因此并行。

### 关于Spring Kafka中key的作用

用于指定分区。相同key的消息将会进入同一个分区

将key的byte[]进行运算后与分区数量取模得到目标分区号

当key为null的时候将会轮流进入分区

### 服务启动后增加Topic消费

https://stackoverflow.com/a/48022114

start()