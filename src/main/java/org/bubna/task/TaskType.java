package org.bubna.task;

public enum TaskType {

    CLASSIFICATION("classification"),
    SUMMARIZATION("summarization"),
    AUDIO_GENERATION("audio-generation"),
    IMAGE_GENERATION("image-generation");

    public final String queueName;

    TaskType(String queueName) {
        this.queueName = queueName;
    }

}
