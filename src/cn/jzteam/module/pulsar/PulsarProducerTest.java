package cn.jzteam.module.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

public class PulsarProducerTest {

    private static final String SERVER_URL = "puslar://pulsar-options.dev-okex.svc.cluster.local:6650";

    public static void main(String[] args) throws Exception {
        // 构造Pulsar Client
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVER_URL)
                .enableTcpNoDelay(true)
                .build();
        Producer<String> producer = client.newProducer(Schema.STRING)
                .producerName("producer")
                .topic("persistent://users_okex_dev/users/register")
                .create();


        producer.newMessage().key("123").value("hello").send();
        System.out.println("main线程发布消息结束");
    }
}
