package com.kgm.nextnest.service;

import com.kgm.nextnest.dto.MessageRequest;
import com.kgm.nextnest.response.ConversationResponse;
import com.kgm.nextnest.response.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse sendMessage(MessageRequest request, String senderEmail);

    List<MessageResponse> getMessages(Long inquiryId, String email);

    void markAsRead(Long messageId, String email);

    List<ConversationResponse> getMyConversations(String email);
}