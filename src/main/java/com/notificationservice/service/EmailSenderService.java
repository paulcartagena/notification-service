package com.notificationservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.model.Notification;
import com.notificationservice.model.enums.NotificationStatus;
import com.notificationservice.model.enums.TemplateType;
import com.notificationservice.repository.NotificationRepository;
import com.notificationservice.template.EmailTemplateBuilder;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailSenderService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final EmailTemplateBuilder emailTemplateBuilder;

    public EmailSenderService(NotificationRepository notificationRepository,
                              ObjectMapper objectMapper,
                              EmailTemplateBuilder emailTemplateBuilder) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
        this.emailTemplateBuilder = emailTemplateBuilder;
    }

    @Async
    public void processNotification(Notification notification) {
        try {
            // 1.
            Map<String, Object> payload = objectMapper.readValue(
                    notification.getPayload(),
                    new TypeReference<Map<String, Object>>() {}
            );

            // 2. Build HTLM
            String html = emailTemplateBuilder.build(notification.getTemplate(), payload);

            // 3. Send via SendGrid
            SendGrid sg = new SendGrid(apiKey);

            Email from = new Email(fromEmail);
            Email to = new Email(notification.getRecipient());
            Content content = new Content("text/html", html);
            Mail mail = new Mail(from, getSubject(notification.getTemplate()), to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid error: " + response.getBody());
            }

            // 4. Update status
            notification.setStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } catch (Exception e) {
            // Update status to FAILED
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notification.setAttempts(notification.getAttempts() + 1);
            notificationRepository.save(notification);
        }
    }

    private String getSubject(TemplateType templateType) {
        return switch (templateType) {
            case WELCOME -> "Welcome to our platform!";
            case ORDER_STATUS_CHANGED -> "Your order status has been updated";
        };
    }
}
