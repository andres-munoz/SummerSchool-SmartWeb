package es.uca.cursoia;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class WebNotifications {

    public static void showSuccessNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(5000);
        notification.addThemeName(NotificationVariant.LUMO_SUCCESS.getVariantName());
        notification.open();
    }

    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(5000);
        notification.addThemeName(NotificationVariant.LUMO_ERROR.getVariantName());
        notification.open();
    }
}
