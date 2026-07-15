package com.notificationservice.dto;

import com.notificationservice.model.enums.NotificationType;
import com.notificationservice.model.enums.TemplateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    @NotBlank(message = "Recipient is required.")
    private String recipient;

    private NotificationType type;

    @NotNull(message = "Template is required.")
    private TemplateType template;

    @NotNull(message = "Payload is required.")
    private Map<String, Object> payload;
}
