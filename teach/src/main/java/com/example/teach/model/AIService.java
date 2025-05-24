package com.example.teach.model;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AIService {

    private static final String MODEL_NAME = "gemma3:1b";
    private static final String HOST = "http://localhost:11434/";
    private static final int HTTP_OK = 200;
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private static AIService instance;
    private final OllamaAPI ollamaAPI;

    private AIService() {
        this.ollamaAPI = new OllamaAPI(HOST);
        this.ollamaAPI.setRequestTimeoutSeconds(120);
    }

    public static AIService getInstance() {
        if (instance == null) {
            instance = new AIService();
        }
        return instance;
    }

    public String getResponse(String prompt) {
        System.out.println("[DEBUG] Sending prompt to Ollama: " + prompt);
        try {
            OllamaResult result = ollamaAPI.generate(MODEL_NAME, prompt, false, new OptionsBuilder().build());

            if (result != null) {
                System.out.println("[DEBUG] HTTP status: " + result.getHttpStatusCode());
                System.out.println("[DEBUG] Raw AI response: " + result.getResponse());
            }

            if (result != null && result.getHttpStatusCode() == 200) {
                return result.getResponse();
            } else {
                return "⚠️ AI returned no valid response.";
            }

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            System.out.println("[ERROR] AI generation failed:");
            e.printStackTrace();
            return "❌ Error during AI generation.";
        }
    }
}
