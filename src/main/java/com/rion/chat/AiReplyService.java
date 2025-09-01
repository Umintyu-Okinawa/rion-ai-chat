package com.rion.chat;

import org.springframework.stereotype.Service;

@Service
public class AiReplyService {

    public ChatMessage buildReply(ChatMessage user) {
        String userContent =
            (user != null && user.getContent() != null) ? user.getContent() : "";

        ChatMessage ai = new ChatMessage();
        ai.setSender("ai");
        ai.setContent("（テスト返信）" + userContent);
        return ai;
    }
}
