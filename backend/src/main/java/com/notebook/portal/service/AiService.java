package com.notebook.portal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notebook.portal.entity.AiResult;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.repository.AiResultRepository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiResultRepository aiResultRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    @Value("${app.ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${app.ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${app.ai.openai.model:gpt-4o-mini}")
    private String openAiModel;

    @Value("${app.ai.openai.base-url:https://api.openai.com/v1}")
    private String openAiBaseUrl;

    @Value("${app.ai.openai.timeout-ms:12000}")
    private long openAiTimeoutMs;

    public String generateSummary(Notebook notebook, String text) {
        return getOrCreateResult(notebook, "SUMMARY", text);
    }

    public String generateQuestions(Notebook notebook, String text) {
        return getOrCreateResult(notebook, "QUESTIONS", text);
    }

    public String generateTags(Notebook notebook, String text) {
        return getOrCreateResult(notebook, "TAGS", text);
    }

    private String getOrCreateResult(Notebook notebook, String type, String text) {
        Optional<AiResult> cached = aiResultRepository.findTopByNotebookIdAndTypeOrderByCreatedAtDesc(
            notebook.getId(), type
        );
        if (cached.isPresent()) {
            return cached.get().getContent();
        }
        String content = generateWithAi(type, text);
        AiResult result = new AiResult();
        result.setNotebook(notebook);
        result.setType(type);
        result.setContent(content);
        aiResultRepository.save(result);
        return content;
    }

    private String generateWithAi(String type, String text) {
        if (!aiEnabled) {
            return "AI disabled. Set AI_ENABLED=true and configure OPENAI_API_KEY to enable.";
        }
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            return "AI enabled but OPENAI_API_KEY is missing.";
        }
        if (text == null || text.isBlank()) {
            return "No description available to analyze.";
        }
        try {
            String prompt = buildPrompt(type, text.trim());
            int maxTokens = type.equals("SUMMARY") ? 220 : 160;
            String response = callOpenAi(prompt, maxTokens);
            return response.isBlank() ? "AI returned an empty response." : response;
        } catch (Exception ex) {
            return "AI request failed. Check AI configuration and try again.";
        }
    }

    private String buildPrompt(String type, String text) {
        String header = "You are an academic assistant for a notebook portal.";
        if (type.equals("SUMMARY")) {
            return header
                + " Summarize the content in 3-5 concise sentences."
                + " Focus on the core concepts and learning outcomes."
                + "\n\n" + text;
        }
        if (type.equals("QUESTIONS")) {
            return header
                + " Create 5 exam-style questions as bullet points."
                + " Each question should test understanding of key ideas."
                + "\n\n" + text;
        }
        return header
            + " Return 6-10 short comma-separated tags only, no extra text."
            + "\n\n" + text;
    }

    private String callOpenAi(String prompt, int maxTokens) throws IOException, InterruptedException {
        Map<String, Object> payload = Map.of(
            "model", openAiModel,
            "temperature", 0.3,
            "max_tokens", maxTokens,
            "messages", List.of(
                Map.of("role", "system", "content", "You are concise and helpful."),
                Map.of("role", "user", "content", prompt)
            )
        );
        String body = objectMapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(openAiBaseUrl + "/chat/completions"))
            .timeout(Duration.ofMillis(openAiTimeoutMs))
            .header("Authorization", "Bearer " + openAiApiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new IOException("OpenAI request failed with status " + response.statusCode());
        }
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode content = root.path("choices").path(0).path("message").path("content");
        return content.isMissingNode() ? "" : content.asText().trim();
    }
}
