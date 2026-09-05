package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.MessageRequest;
import com.kgm.nextnest.response.ConversationResponse;
import com.kgm.nextnest.response.MessageResponse;
import com.kgm.nextnest.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Message Resource"
)
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestBody
            MessageRequest request,
            Authentication authentication) {

        String senderEmail = authentication.getName();

        return new ResponseEntity<>(messageService.sendMessage(request, senderEmail),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/inquiry/{inquiryId}")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable
            Long inquiryId,
            Authentication authentication) {

        return ResponseEntity.ok(messageService.getMessages(inquiryId, authentication.getName()));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable
            Long id,
            Authentication authentication) {

        messageService.markAsRead(id, authentication.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(
            Authentication authentication) {

        return ResponseEntity.ok(messageService.getMyConversations(authentication.getName()));
    }
}