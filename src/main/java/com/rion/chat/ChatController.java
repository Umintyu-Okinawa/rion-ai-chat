package com.rion.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GptReplyService gptReplyService;
    private final ChatMessageRepository repo;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          GptReplyService gptReplyService,
                          ChatMessageRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.gptReplyService = gptReplyService;
        this.repo = repo;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/room.general")
    public ChatMessage broadcast(@Payload ChatMessage msg) {

        if (msg == null) msg = new ChatMessage();
        if (msg.getSender() == null)  msg.setSender("anon");
        if (msg.getContent() == null) msg.setContent("");

        // ユーザー発言を保存（Entity変換はこれまで通り）
        repo.save(toEntity(msg));

        // GPT の返答を作成 -> 保存 -> 同じトピックに配信
        String replyText = gptReplyService.ask(msg.getContent());

        ChatMessage ai = new ChatMessage();
        ai.setSender("gpt");
        ai.setContent(replyText);
        repo.save(toEntity(ai));

        messagingTemplate.convertAndSend("/topic/room.general", ai);

        // ユーザーのメッセージは @SendTo で返る
        return msg;
    }

    // 既存の toEntity(...) を利用
    private ChatMessageEntity toEntity(ChatMessage m) {
        ChatMessageEntity e = new ChatMessageEntity();
        e.setSender(m.getSender());
        e.setContent(m.getContent());
        e.setTimestamp(java.time.LocalDateTime.now());
        return e;
    }
}
