package com.notificationservice.controller;

import com.notificationservice.dto.NotificationRequestDTO;
import com.notificationservice.dto.NotificationResponseDTO;
import com.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationResponseDTO send(@Valid @RequestBody NotificationRequestDTO dto) {
        return notificationService.send(dto);
    }

    @GetMapping("/{notificationId}")
    public NotificationResponseDTO getNotificationById(@PathVariable Long notificationId) {
        return  notificationService.findNotificationById(notificationId);
    }

    @GetMapping
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationService.findAllNotifications();
    }
}
