package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class MessageService {
    public static String generateMessage() {
        List<String> messages = Arrays.asList("Hello World!", "Gradle 4tw!");
        return messages.get(new Random().nextInt(messages.size()));
    }
}