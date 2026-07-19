package com.notificationservice.template;

import com.notificationservice.model.enums.TemplateType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailTemplateBuilder {

    public String build(TemplateType templateType, Map<String, Object> payload) {
        return switch (templateType) {
            case WELCOME -> buildWelcomeTemplate(payload);
            case ORDER_STATUS_CHANGED -> buildOrderStatusTemplate(payload);
        };
    }

    private String buildWelcomeTemplate(Map<String, Object> payload) {
        String name = (String) payload.get("name");
        return """
                <h1>Welcome, %s!</h1>
                <p>Than you for joining us.</p>
                """.formatted(name);
    }

    private String buildOrderStatusTemplate(Map<String, Object> payload) {
        String username = (String) payload.get("userName");
        String restaurantName = (String) payload.get("restaurantName");
        String status = (String) payload.get("status");
        String total = (String) payload.get("total");
        return """
                <h1>Hello, %s!</h1>
                <p>Your order at %s has been updated to <strong>%s</strong>.</p>
                <p>Total: $%s</p>
                """.formatted(username, restaurantName, status, total);
    }
}
