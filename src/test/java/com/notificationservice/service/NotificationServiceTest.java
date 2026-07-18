package com.notificationservice.service;

import com.notificationservice.dto.NotificationRequestDTO;
import com.notificationservice.dto.NotificationResponseDTO;
import com.notificationservice.model.Notification;
import com.notificationservice.model.enums.NotificationStatus;
import com.notificationservice.model.enums.NotificationType;
import com.notificationservice.model.enums.TemplateType;
import com.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSendNotificationSuccessfully() {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setRecipient("paul@gmail.com");
        dto.setTemplate(TemplateType.WELCOME);
        dto.setType(NotificationType.EMAIL);
        dto.setPayload(Map.of(
                "name", "Paul",
                "email", "paul@gmail.com"
        ));

        Notification savedNotification = new Notification();
        savedNotification.setId(1L);
        savedNotification.setRecipient("paul@gmail.com");
        savedNotification.setStatus(NotificationStatus.PENDING);

        when(objectMapper.writeValueAsString(dto.getPayload()))
                .thenReturn("{\\\"name\\\":\\\"Paul\\\",\\\"email\\\":\\\"paul@gmail.com\\\"}");
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        NotificationResponseDTO result = notificationService.send(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("paul@gmail.com", result.getRecipient());
        assertEquals(NotificationStatus.PENDING, result.getStatus());
    }

    @Test
    void shouldFindNotificationSuccessfully() {
        Long notificationId = 1L;

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setRecipient("paul@gmail.com");

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        NotificationResponseDTO result = notificationService.findNotificationById(notificationId);

        assertNotNull(result);
        assertEquals(notification.getRecipient(), result.getRecipient());
    }

    @Test
    void shouldFindAllNotifications() {
        Notification notification1 = new Notification();
        notification1.setRecipient("paul@gmail.com");

        Notification notification2 = new Notification();
        notification2.setRecipient("kevin@gmail.com");

        when(notificationRepository.findAll())
                .thenReturn(List.of(notification1, notification2));

        List<NotificationResponseDTO> result = notificationService.findAllNotifications();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

}
