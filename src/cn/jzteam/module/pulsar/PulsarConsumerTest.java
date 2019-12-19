package cn.jzteam.module.pulsar;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

public class PulsarConsumerTest {

    private static final String SERVER_URL = "puslar://pulsar-options.dev-okex.svc.cluster.local:6650";

    public static void main(String[] args) throws Exception {
        // 构造Pulsar Client
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVER_URL)
                .enableTcpNoDelay(true)
                .build();
        // Schema 必须 Consumer 与 Producer 一致
        Consumer consumer = client.newConsumer(Schema.STRING)
                .consumerName("my-consumer")
                .topic("persistent://users_okex_dev/users/register")
                .subscriptionName("my-subscription")
                .ackTimeout(10, TimeUnit.SECONDS)
                .maxTotalReceiverQueueSizeAcrossPartitions(10)
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();
        System.out.println("main线程订阅消息：");


        new Thread(() -> {
            try {
                Consumer consumer1 = client.newConsumer(Schema.STRING)
                        .consumerName("my-consumer1")
                        .topic("persistent://users_okex_dev/users/register")
                        .subscriptionName("my-subscription1")
                        .ackTimeout(10, TimeUnit.SECONDS)
                        .maxTotalReceiverQueueSizeAcrossPartitions(10)
                        .subscriptionType(SubscriptionType.Shared)
                        .subscribe();
                System.out.println("子线程1订阅消息：");
                do {
                    // 接收消息有两种方式：异步和同步
                    // CompletableFuture<Message<String>> message = consumer.receiveAsync();
                    Message message = consumer1.receive();
                    System.out.println("子线程1消费消息 > " + message.getPublishTime() + ": " + message.getKey() + "--" + message.getValue());
                    consumer1.acknowledge(message);
                } while (true);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Consumer consumer2 = client.newConsumer(Schema.STRING)
                        .consumerName("my-consumer1")
                        .topic("persistent://users_okex_dev/users/register")
                        .subscriptionName("my-subscription1")
                        .ackTimeout(10, TimeUnit.SECONDS)
                        .maxTotalReceiverQueueSizeAcrossPartitions(10)
                        .subscriptionType(SubscriptionType.Shared)
                        .subscribe();
                System.out.println("子线程2订阅消息：");
                do {
                    // 接收消息有两种方式：异步和同步
                    // CompletableFuture<Message<String>> message = consumer.receiveAsync();
                    Message message = consumer2.receive();
                    System.out.println("子线程2消费消息 > " + message.getPublishTime() + ": " + message.getKey() + "--" + message.getValue());
                    consumer2.acknowledge(message);
                } while (true);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        try {

            do {
                // 接收消息有两种方式：异步和同步
                // CompletableFuture<Message<String>> message = consumer.receiveAsync();
                Message message = consumer.receive();
                System.out.println("main线程收到消息 > " + message.getPublishTime() + ": " + message.getKey() + "--" + message.getValue());
                consumer.acknowledge(message);
            } while (true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
