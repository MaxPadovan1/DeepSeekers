package com.example.teach.model;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;//hello
/**
 * Singleton service class to handle AI generation using the Ollama API.
 * <p>
 * Responsible for sending prompts and retrieving AI-generated content such as lesson plans.
 */
public class AIService {
    /** The AI model name used for generation (must be available on the Ollama server). */
    private static final String MODEL_NAME = "gemma3:1b";
    /** The local Ollama server host URL. */
    private static final String HOST = "http://localhost:11434/";
    /** HTTP status code indicating a successful request. */
    private static final int HTTP_OK = 200;
    /** Logger instance for debugging and error reporting. */
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    /** Singleton instance of AIService. */
    private static AIService instance;
    /** Ollama API instance for interacting with the AI model. */
    private final OllamaAPI ollamaAPI;
    /**
     * Private constructor for singleton pattern.
     * Initializes the Ollama API with the defined host and timeout.
     */
    private AIService() {
        this.ollamaAPI = new OllamaAPI(HOST);
        this.ollamaAPI.setRequestTimeoutSeconds(120);
    }
    /**
     * Returns the singleton instance of AIService.
     *
     * @return the {@link AIService} instance
     */
    public static AIService getInstance() {
        if (instance == null) {
            instance = new AIService();
        }
        return instance;
    }
    /**
     * Sends a prompt to the AI model and returns the generated response.
     *
     * @param prompt the prompt or instruction to send to the AI
     * @return the generated response as a {@link String}, or an error message if failed
     */
    public String getResponse(String prompt) {
        try {
            OllamaResult result = ollamaAPI.generate(MODEL_NAME, prompt, false, new OptionsBuilder().build());

            if (result != null && result.getHttpStatusCode() == 200) {
                return result.getResponse();
            } else {
                return "⚠️ AI returned no valid response.";
            }

        } catch (OllamaBaseException | IOException | InterruptedException e) {
            e.printStackTrace();
            return "❌ Error during AI generation.";
        }

    }
    /**
     * Convenience method for generating a lesson plan from a given prompt.
     *
     * @param prompt the input prompt describing the desired lesson content
     * @return the AI-generated lesson plan as a {@link String}
     */
    public String generateLessonPlan(String prompt) {
        return getResponse(prompt);


    }
}
