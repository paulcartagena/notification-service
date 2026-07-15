package com.notificationservice.dto;

import com.notificationservice.model.enums.NotificationStatus;
import com.notificationservice.model.enums.NotificationType;
import com.notificationservice.model.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String recipient;
    private NotificationType type;
    private TemplateType template;
    private NotificationStatus status;
    private int attempts;
    private String errorMessage;
    private LocalDateTime createdAt;
}
