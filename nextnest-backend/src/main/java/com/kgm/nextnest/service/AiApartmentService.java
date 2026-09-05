package com.kgm.nextnest.service;

import com.kgm.nextnest.dto.AiSearchRequest;
import com.kgm.nextnest.dto.ApartmentRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiApartmentService {

    private final ChatClient chatClient;

    public AiApartmentService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateDescription(ApartmentRequest request) {


        // For AI generated description
        String type = request.getPropertyType() != null ? request.getPropertyType().name().toLowerCase() : "property";
        int beds = request.getBedrooms() != null ? request.getBedrooms() : 0;
        int baths = request.getBathrooms() != null ? request.getBathrooms() : 0;
        String furnishing = request.getFurnishing() != null ? request.getFurnishing().name().toLowerCase() : "unfurnished";

        String prompt = String.format(
                "Write a highly engaging and professional real estate listing description for a %s. " +
                        "It has %d bedrooms, %d bathrooms, and is currently %s. " +
                        "Make the tone inviting and keep it under 3 paragraphs.",
                type, beds, baths, furnishing
        );

        return chatClient.prompt()
                .user(prompt)
                .options(GoogleGenAiChatOptions.builder()
                        .model("gemini-3.6-flash")
                        .build())
                .call()
                .content();
    }

    // For Searchin or Filtering using AI
    public AiSearchRequest parseSearchQuery(String query) {
        try {
            BeanOutputConverter<AiSearchRequest> converter = new BeanOutputConverter<>(AiSearchRequest.class);
            String format = converter.getFormat();

            String prompt = String.format(
                    "Extract real estate search parameters from the following user query: '%s'.\n" +
                            "Return the extracted data in the exact JSON format specified here: %s\n" +
                            "If a parameter is not mentioned, leave it null.",
                    query, format
            );

            String response = chatClient.prompt()
                    .user(prompt)
                    .options(GoogleGenAiChatOptions.builder()
                            .model("gemini-3.6-flash")
                            .build())
                    .call()
                    .content();

            return converter.convert(response);

        } catch (Exception e) {
            // Log the error so you can see it in the terminal
            System.err.println("AI Search Failed (Likely High Demand or Rate Limit): " + e.getMessage());

            // Return an empty request so the frontend doesn't get a 500 error!
            return new AiSearchRequest(null, null, null, null, null, null);
        }
    }
}