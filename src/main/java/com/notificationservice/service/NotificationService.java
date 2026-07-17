package com.notificationservice.service;

import com.notificationservice.dto.NotificationRequestDTO;
import com.notificationservice.dto.NotificationResponseDTO;
import com.notificationservice.exception.NotificationNotFound;
import com.notificationservice.model.Notification;
import com.notificationservice.model.enums.NotificationStatus;
import com.notificationservice.model.enums.NotificationType;
import com.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public NotificationService(NotificationRepository notificationRepository,
                               ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }

    public NotificationResponseDTO send(NotificationRequestDTO dto) {
        String payloadJson = objectMapper.writeValueAsString(dto.getPayload());

        Notification notification = new Notification();
        notification.setRecipient(dto.getRecipient());
        notification.setType(dto.getType() != null ? dto.getType() : NotificationType.EMAIL);
        notification.setTemplate(dto.getTemplate());
        notification.setPayload(payloadJson);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setAttempts(0);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return buildResponse(saved);
    }

    @Transactional(readOnly = true)
    public NotificationResponseDTO findNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFound("Notification not found."));

        return buildResponse(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> findAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    private NotificationResponseDTO buildResponse(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getRecipient(),
                notification.getType(),
                notification.getTemplate(),
                notification.getStatus(),
                notification.getAttempts(),
                notification.getErrorMessage(),
                notification.getCreatedAt()
        );
    }
}
