package com.cab302ai_teacher.util;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class APIHandler {

    private static final String OPENAI_API_KEY = loadAPIKey();  // Load API Key from properties file
    private static final OpenAIClientAsync client;

    static {
        // Manually set the API key using the correct configuration or environment variable
        client = new OpenAIOkHttpClientAsync.Builder()
                .apiKey(OPENAI_API_KEY)
                .build();
    }
    // Load API Key from properties file
    private static String loadAPIKey() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/com/cab302ai_teacher/config.properties")) {
            properties.load(inputStream);
            String apiKey = properties.getProperty("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("API key is missing in config.properties file.");
            }
            return apiKey;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load API key from config.properties", e);
        }
    }

    // Makes the API call to OpenAI and returns the response as a String asynchronously
    public static CompletableFuture<Object> getBotResponse(String userMessage) {
        // Build the chat completion parameters
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(userMessage)  // Add the user's message
                .model(ChatModel.GPT_4_1)  // Specify the model
                .build();

        // Make the asynchronous API call
        CompletableFuture<ChatCompletion> chatCompletion = client.chat().completions().create(params);

        // Process the response and return the result
        return chatCompletion.thenApply(chatResult -> {
            // Check for valid response and return the bot's reply
            if (chatResult.choices() != null && !chatResult.choices().isEmpty()) {
                return chatResult.choices().get(0).message().content();
            } else {
                return "Sorry, I couldn't generate a response at the moment.";
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return "Error occurred: " + ex.getMessage();
        });
    }
}
