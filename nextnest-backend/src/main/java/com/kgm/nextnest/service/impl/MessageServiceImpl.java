package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.dto.MessageRequest;
import com.kgm.nextnest.model.Inquiry;
import com.kgm.nextnest.model.Message;
import com.kgm.nextnest.model.RoleType;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.repository.InquiryRepository;
import com.kgm.nextnest.repository.MessageRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.ConversationResponse;
import com.kgm.nextnest.response.MessageResponse;
import com.kgm.nextnest.service.EmailService;
import com.kgm.nextnest.service.MessageService;
import com.kgm.nextnest.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl
        implements MessageService {

    private final MessageRepository messageRepository;

    private final InquiryRepository inquiryRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final NotificationService notificationService;

    @Override
    public MessageResponse sendMessage(
            MessageRequest request,
            String senderEmail) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry inquiry = inquiryRepository.findById(request.getInquiryId())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        Message message = Message.builder()
                .inquiry(inquiry)
                .sender(sender)
                .content(request.getContent())
                .isRead(false)
                .build();

        Message savedMessage =
                messageRepository.save(message);

        User receiver =
                sender.getId().equals(
                        inquiry.getCustomer().getId()
                )
                        ? inquiry.getApartment().getOwner()
                        : inquiry.getCustomer();

        notificationService.createNotification(

                receiver,

                "New Message",

                sender.getFullName()
                        + " sent you a message regarding "
                        + inquiry.getApartment().getTitle(),

                "/messages/" + inquiry.getId()
        );

        LocalDateTime now =
                LocalDateTime.now();

        boolean shouldSendEmail =

                inquiry.getLastEmailNotificationSentAt() == null

                        ||

                        inquiry.getLastEmailNotificationSentAt()
                                .isBefore(
                                        now.minusHours(1)
                                );

        if (shouldSendEmail) {

            try {

                emailService.sendNewMessageNotification(

                        receiver.getEmail(),

                        sender.getFullName(),

                        inquiry.getApartment().getTitle(),

                        request.getContent()
                );

                inquiry.setLastEmailNotificationSentAt(
                        now
                );

                inquiryRepository.save(
                        inquiry
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        return mapToResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> getMessages(Long inquiryId, String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                        .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        validateAccess(inquiry, user);

        return messageRepository.findByInquiryIdAndArchivedFalseOrderBySentAtAsc(inquiryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void markAsRead(Long messageId, String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = messageRepository.findById(messageId)
                        .orElseThrow(() -> new RuntimeException("Message not found"));

        Inquiry inquiry = message.getInquiry();

        System.out.println(
                "Logged User ID: " +
                        user.getId()
        );

        System.out.println(
                "Inquiry Customer ID: " +
                        inquiry.getCustomer().getId()
        );

        System.out.println(
                "Apartment Owner ID: " +
                        inquiry.getApartment()
                                .getOwner()
                                .getId()
        );

        System.out.println(
                "Role: " +
                        user.getRole()
        );

        validateAccess(inquiry, user);

        message.setIsRead(true);

        messageRepository.save(message);
    }

    @Override
    public List<ConversationResponse> getMyConversations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Inquiry> inquiries;

        if (user.getRole() == RoleType.CUSTOMER) {

            inquiries = inquiryRepository.findByCustomer_Id(user.getId());

        } else if (user.getRole() == RoleType.OWNER) {

            inquiries = inquiryRepository.findByApartmentOwnerId(user.getId());

        } else {

            inquiries = inquiryRepository.findAll();
        }

        return inquiries.stream()
                .map(inquiry -> {

                    Optional<Message> lastMessage = messageRepository.findFirstByInquiryIdOrderBySentAtDesc(inquiry.getId());

                    String otherUserName;

                    if (user.getRole() == RoleType.CUSTOMER) {

                        otherUserName = inquiry.getApartment().getOwner().getFullName();

                    } else {

                        otherUserName = inquiry.getCustomer().getFullName();
                    }

                    return ConversationResponse.builder()
                            .inquiryId(inquiry.getId())
                            .apartmentId(inquiry.getApartment().getId())
                            .apartmentTitle(inquiry.getApartment().getTitle())
                            .otherUserName(otherUserName)
                            .lastMessage(lastMessage
                                    .map(Message::getContent)
                                            .orElse("")
                            )
                            .lastMessageTime(lastMessage
                                            .map(Message::getSentAt)
                                            .orElse(null)
                            )

                            .unreadCount(messageRepository.countByInquiryIdAndIsReadFalse(inquiry.getId()))
                            .build();

                })
                .sorted(
                        (a, b) -> {

                            if (
                                    a.getLastMessageTime() == null
                            ) return 1;

                            if (
                                    b.getLastMessageTime() == null
                            ) return -1;

                            return b.getLastMessageTime()
                                    .compareTo(
                                            a.getLastMessageTime()
                                    );
                        }
                )

                .toList();
    }

    private MessageResponse mapToResponse(Message message) {

        return MessageResponse.builder()

                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFullName())
                .content(message.getContent())
                .read(message.getIsRead())
                .sentAt(message.getSentAt())
                .build();
    }

    private void validateAccess(Inquiry inquiry, User user) {

        boolean isCustomer = inquiry.getCustomer().getId().equals(user.getId());

        boolean isOwner = inquiry.getApartment().getOwner().getId().equals(user.getId());

        boolean isAdmin = user.getRole() == RoleType.ADMIN;

        if (!isCustomer && !isOwner && !isAdmin) {

            throw new RuntimeException("Access denied");
        }
    }
}