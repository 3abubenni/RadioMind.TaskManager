package org.bubna.task.rabbitmq;

import com.rabbitmq.client.Channel;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bubna.task.Task;
import org.bubna.task.TaskType;
import org.bubna.util.JsonUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class TaskRabbitMqProducer {

    final RabbitMQClient rabbitMQClient;

    Channel channel;

    @ConfigProperty(name = "rabbitmq-task-exchange")
    String exchangeName;

    public void onSetup(@Observes StartupEvent ev) {
        log.info("Starting RabbitMQ producer");
        try {
            channel = rabbitMQClient.connect().createChannel();
            channel.exchangeDeclare(
                    exchangeName,
                    "direct",
                    true,
                    false,
                    null
            );
            for (TaskType type : TaskType.values()) {
                channel.queueDeclare(
                        type.queueName,
                        true,
                        false,
                        false,
                        null
                );
                channel.queueBind(type.queueName, exchangeName, type.queueName);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendTask(Task<?, ?> task) {
        TaskRabbitMqMessage<?, ?> rabbitMqMessage = TaskRabbitMqMessage.builder()
                .id(task.getId())
                .outputData(task.getOutputData())
                .inputData(task.getInputData())
                .type(task.getTaskType())
                .status(task.getStatus())
                .build();

        String json = JsonUtil.toJson(rabbitMqMessage);
        try {
            channel.basicPublish(
                    exchangeName,
                    task.getTaskType().queueName,
                    null,
                    json.getBytes(StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            log.error("Error during sending task message to queue {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}