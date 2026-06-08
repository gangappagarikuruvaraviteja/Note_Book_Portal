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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

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
    
    @Value("${app.ai.provider:openai}")
    private String aiProvider;

    @Value("${app.ai.grok.api-key:}")
    private String grokApiKey;

    @Value("${app.ai.grok.model:grok-mini}")
    private String grokModel;

    @Value("${app.ai.grok.base-url:}")
    private String grokBaseUrl;

    @Value("${app.ai.grok.timeout-ms:12000}")
    private long grokTimeoutMs;

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
            String cachedContent = cached.get().getContent();
            if (!isCacheableAiContent(cachedContent)) {
                aiResultRepository.delete(cached.get());
            } else {
                return cachedContent;
            }
        }
        String content = generateWithAi(type, text);
        AiResult result = new AiResult();
        result.setNotebook(notebook);
        result.setType(type);
        result.setContent(content);
        if (isCacheableAiContent(content)) {
            aiResultRepository.save(result);
        }
        return content;
    }

    private String generateWithAi(String type, String text) {
        String activeProvider = resolveActiveProvider();
        if (activeProvider == null) {
            return "AI not configured. Set GROK_API_KEY or OPENAI_API_KEY, or set AI_PROVIDER=stub for local testing.";
        }
        if (text == null || text.isBlank()) {
            return "No description available to analyze.";
        }
        try {
            String prompt = buildPrompt(type, text.trim());
            int maxTokens = type.equals("SUMMARY") ? 900 : 220;
            String response;
            if (activeProvider.equalsIgnoreCase("openai")) {
                response = callOpenAi(prompt, maxTokens);
            } else if (activeProvider.equalsIgnoreCase("grok")) {
                response = callGrok(prompt, type, maxTokens);
            } else { // stub
                response = devStubResponse(type);
            }
            return response.isBlank() ? "AI returned an empty response." : response;
        } catch (Exception ex) {
            log.warn("AI generation failed for type {}. Falling back to local content. Reason: {}", type, ex.getMessage());
            return localFallback(type, text);
        }
    }

    private String buildPrompt(String type, String text) {
        String header = "You are an academic assistant for a notebook portal.";
        if (type.equals("SUMMARY")) {
            return header
                + " Write a detailed, step-by-step explanation of the notebook content."
                + " Use clear headings, short paragraphs, and bullet points where helpful."
                + " Include: overview, important concepts, step-by-step explanation, examples if present, and a short conclusion."
                + " Do not repeat the raw metadata (title/subject/semester/branch)."
                + " Make the answer neat, clear, and useful for studying."
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
        if (log.isDebugEnabled()) {
            String bodyPreview = response.body() == null ? "" : response.body();
            if (bodyPreview.length() > 2000) bodyPreview = bodyPreview.substring(0, 2000) + "...";
            log.debug("OpenAI request to {} returned status {} and body: {}", openAiBaseUrl + "/chat/completions", response.statusCode(), bodyPreview);
        }
        if (response.statusCode() >= 400) {
            throw new IOException("OpenAI request failed with status " + response.statusCode());
        }
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode content = root.path("choices").path(0).path("message").path("content");
        return content.isMissingNode() ? "" : content.asText().trim();
    }

    private String callGenericProvider(String prompt, int maxTokens, String baseUrl, String apiKey, long timeoutMs)
        throws IOException, InterruptedException {
        Map<String, Object> payload = Map.of(
            "model", grokModel,
            "prompt", prompt,
            "max_tokens", maxTokens
        );
        String body = objectMapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl))
            .timeout(Duration.ofMillis(timeoutMs))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (log.isDebugEnabled()) {
            String bodyPreview = response.body() == null ? "" : response.body();
            if (bodyPreview.length() > 2000) bodyPreview = bodyPreview.substring(0, 2000) + "...";
            log.debug("AI provider request to {} returned status {} and body: {}", baseUrl, response.statusCode(), bodyPreview);
        }
        if (response.statusCode() >= 400) {
            throw new IOException("AI provider request failed with status " + response.statusCode());
        }
        JsonNode root = objectMapper.readTree(response.body());
        // try common locations for text
        JsonNode content = root.path("choices");
        if (content.isArray() && content.size() > 0) {
            JsonNode first = content.get(0);
            JsonNode msg = first.path("message").path("content");
            if (!msg.isMissingNode()) return msg.asText().trim();
            JsonNode text = first.path("text");
            if (!text.isMissingNode()) return text.asText().trim();
        }
        JsonNode out = root.path("output");
        if (!out.isMissingNode()) return out.asText().trim();
        JsonNode result = root.path("result");
        if (!result.isMissingNode()) return result.asText().trim();
        JsonNode text = root.path("text");
        if (!text.isMissingNode()) return text.asText().trim();
        return "";
    }

    private String callGrok(String prompt, String type, int maxTokens) throws IOException, InterruptedException {
        String targetUrl = (grokBaseUrl == null || grokBaseUrl.isBlank())
            ? "https://api.x.ai/v1/chat/completions"
            : grokBaseUrl;
        String model = resolveGrokModel();

        Map<String, Object> payload = Map.of(
            "model", model,
            "temperature", 0.3,
            "max_completion_tokens", maxTokens,
            "messages", List.of(
                Map.of("role", "system", "content", "You are concise and helpful."),
                Map.of("role", "user", "content", buildPrompt(type, prompt))
            )
        );

        String body = objectMapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(targetUrl))
            .timeout(Duration.ofMillis(grokTimeoutMs))
            .header("Authorization", "Bearer " + grokApiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (log.isDebugEnabled()) {
            String bodyPreview = response.body() == null ? "" : response.body();
            if (bodyPreview.length() > 2000) bodyPreview = bodyPreview.substring(0, 2000) + "...";
            log.debug("Grok request to {} returned status {} and body: {}", targetUrl, response.statusCode(), bodyPreview);
        }
        if (response.statusCode() >= 400) {
            throw new IOException("Grok request failed with status " + response.statusCode());
        }
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode content = root.path("choices").path(0).path("message").path("content");
        return content.isMissingNode() ? "" : content.asText().trim();
    }

    private String resolveGrokModel() {
        if (grokModel == null || grokModel.isBlank() || grokModel.equalsIgnoreCase("latest")) {
            return "grok-4.3";
        }
        return grokModel.trim();
    }

    private String resolveActiveProvider() {
        String provider = aiProvider == null ? "" : aiProvider.trim().toLowerCase();
        boolean hasOpenAiKey = openAiApiKey != null && !openAiApiKey.isBlank();
        boolean hasGrokKey = grokApiKey != null && !grokApiKey.isBlank();

        if ("stub".equals(provider)) {
            return "stub";
        }
        if ("openai".equals(provider)) {
            return hasOpenAiKey ? "openai" : (hasGrokKey ? "grok" : null);
        }
        if ("grok".equals(provider)) {
            return hasGrokKey ? "grok" : (hasOpenAiKey ? "openai" : null);
        }
        if (hasGrokKey) {
            return "grok";
        }
        if (hasOpenAiKey) {
            return "openai";
        }
        return aiEnabled ? "stub" : null;
    }

    private String devStubResponse(String type) {
        if (type.equals("SUMMARY")) {
            return "(Dev stub) This is a concise summary generated in development mode.";
        }
        if (type.equals("QUESTIONS")) {
            return "(Dev stub) 1. What is the core idea?\n2. Explain the key steps.\n3. Describe implications.";
        }
        return "tag1, tag2, tag3";
    }

    private String localFallback(String type, String text) {
        String contentOnly = extractNotebookContent(text);
        if (contentOnly.isBlank()) {
            return type.equals("TAGS") ? "general, notes, study" : "No description available to analyze.";
        }
        if (type.equals("SUMMARY")) {
            return summarizeLocally(contentOnly);
        }
        if (type.equals("QUESTIONS")) {
            return buildLocalQuestions(contentOnly);
        }
        return buildLocalTags(contentOnly);
    }

    private String summarizeLocally(String text) {
        String normalized = text.replace('\r', ' ').replace('\n', ' ').replaceAll("\\s+", " ").trim();
        if (normalized.isBlank()) {
            return "No description available to analyze.";
        }
        String[] sentences = normalized.split("(?<=[.!?])\\s+");
        StringBuilder summary = new StringBuilder();
        summary.append("Overview:\n");
        summary.append(buildParagraphFromSentences(sentences, 0, 4));
        summary.append("\n\nStep-by-step explanation:\n");
        summary.append(buildNumberedSteps(sentences));
        summary.append("\n\nQuick recap:\n");
        summary.append(buildParagraphFromSentences(sentences, 4, 7));
        return summary.toString().trim();
    }

    private String buildParagraphFromSentences(String[] sentences, int startIndex, int maxSentences) {
        StringBuilder paragraph = new StringBuilder();
        int added = 0;
        for (int i = startIndex; i < sentences.length && added < maxSentences; i++) {
            String sentence = sentences[i].trim();
            if (sentence.isEmpty() || looksLikeMetadata(sentence)) {
                continue;
            }
            if (paragraph.length() > 0) {
                paragraph.append(' ');
            }
            paragraph.append(sentence);
            added++;
        }
        if (paragraph.length() == 0) {
            String fallback = String.join(" ", sentences).trim();
            return fallback.length() > 320 ? fallback.substring(0, 320).trim() + "..." : fallback;
        }
        return paragraph.toString();
    }

    private String buildNumberedSteps(String[] sentences) {
        StringBuilder steps = new StringBuilder();
        int stepNumber = 1;
        for (int i = 0; i < sentences.length && stepNumber <= 5; i++) {
            String sentence = sentences[i].trim();
            if (sentence.isEmpty() || looksLikeMetadata(sentence)) {
                continue;
            }
            steps.append(stepNumber).append(". ").append(sentence).append('\n');
            stepNumber++;
        }
        if (steps.length() == 0) {
            steps.append("1. Review the main idea.\n2. Identify key terms.\n3. Practice the concepts.");
        }
        return steps.toString().trim();
    }

    private String extractNotebookContent(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        int notesIndex = text.indexOf("Notes:");
        if (notesIndex >= 0) {
            return text.substring(notesIndex + "Notes:".length()).trim();
        }
        return text.trim();
    }

    private boolean looksLikeMetadata(String sentence) {
        String normalized = sentence.toLowerCase();
        return normalized.startsWith("title:")
            || normalized.startsWith("subject:")
            || normalized.startsWith("semester:")
            || normalized.startsWith("branch:")
            || normalized.startsWith("description:")
            || normalized.startsWith("notes:");
    }

    private String buildLocalQuestions(String text) {
        String firstLine = extractFirstMeaningfulLine(text);
        String topic = firstLine.isBlank() ? "the notebook content" : firstLine;
        return String.join("\n",
            "1. What is the main idea of " + topic + "?",
            "2. What are the key concepts or steps described?",
            "3. How would you explain this topic in your own words?"
        );
    }

    private String buildLocalTags(String text) {
        String normalized = text.replace('\r', ' ').replace('\n', ' ').replaceAll("\\s+", " ").trim();
        if (normalized.isBlank()) {
            return "general, notes, study";
        }
        String[] words = normalized.toLowerCase().split("[^a-z0-9]+|");
        return "general, study, notes";
    }

    private String extractFirstMeaningfulLine(String text) {
        if (text == null) {
            return "";
        }
        for (String line : text.split("\\R")) {
            String trimmed = line.trim();
            if (!trimmed.isBlank()) {
                return trimmed.length() > 80 ? trimmed.substring(0, 80).trim() : trimmed;
            }
        }
        return "";
    }

    private boolean isCacheableAiContent(String content) {
        if (content == null || content.isBlank()) {
            return false;
        }
        String normalized = content.trim().toLowerCase();
        return !(normalized.startsWith("ai disabled")
            || normalized.startsWith("ai not configured")
            || normalized.startsWith("ai request failed")
            || normalized.startsWith("ai enabled but")
            || normalized.startsWith("unknown ai provider"));
    }
}
