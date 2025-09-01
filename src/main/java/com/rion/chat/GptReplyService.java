package com.rion.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GptReplyService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.system_prompt:あなたはフレンドリーな日本語アシスタントです。できるだけ簡潔に答えてください。}")
    private String systemPrompt;

    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newHttpClient();

    /** ユーザーのメッセージから GPT の返答テキストを取得 */
    public String ask(String userText) {
        try {
            if (userText == null) userText = "";

            // Chat Completions 形式の JSON を組み立て
            String payload = """
                {
                  "model": "%s",
                  "messages": [
                    {"role":"system","content": %s},
                    {"role":"user","content": %s}
                  ]
                }
                """.formatted(
                    model,
                    jsonQuote(systemPrompt),
                    jsonQuote(userText)
                );

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() / 100 != 2) {
                // 失敗時はログに残して簡易メッセージを返す
                System.err.println("OpenAI error " + res.statusCode() + ": " + res.body());
                return "（ごめんなさい、いま応答できませんでした）";
            }

            JsonNode root = mapper.readTree(res.body());
            String reply = root.path("choices").get(0).path("message").path("content").asText();
            if (reply == null || reply.isBlank()) reply = "（応答が空でした）";
            return reply;

        } catch (Exception e) {
            e.printStackTrace();
            return "（ごめんなさい、エラーが起きました）";
        }
    }

    /** JSON 文字列用にエスケープして二重引用符で囲む */
    private static String jsonQuote(String s) {
        if (s == null) s = "";
        return "\"" + s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r") + "\"";
    }
}
