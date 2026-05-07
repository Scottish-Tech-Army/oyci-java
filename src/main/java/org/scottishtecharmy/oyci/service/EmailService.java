package org.scottishtecharmy.oyci.service;

import org.scottishtecharmy.oyci.entities.EventAssignment;
import org.scottishtecharmy.oyci.entities.EventInstance;
import org.scottishtecharmy.oyci.entities.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@oyci.org}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an assignment notification email asynchronously.
     * Called after an EventAssignment is persisted so the HTTP response
     * is returned immediately without waiting for the mail server.
     */
    @Async
    public void sendAssignmentNotification(EventAssignment assignment) {
        Staff staff = assignment.getStaff();
        EventInstance instance = assignment.getEventInstance();

        if (staff == null || staff.getEmail() == null || instance == null) {
            log.warn("Skipping email — missing staff/email/event-instance on assignment id={}",
                    assignment.getId());
            return;
        }

        String to = staff.getEmail();
        String subject = "You have been assigned to: " + instance.getEventType().getName();
        String body = buildEmailBody(staff, instance);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Assignment email sent to {} for event instance id={}", to, instance.getId());
        } catch (MailException ex) {
            log.error("Failed to send assignment email to {} for event instance id={}: {}",
                    to, instance.getId(), ex.getMessage());
        }
    }

    private String buildEmailBody(Staff staff, EventInstance instance) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hi ").append(staff.getName()).append(",\n\n");
        sb.append("You have been assigned to the following event:\n\n");
        sb.append("────────────────────────────────────\n");
        sb.append("  Event       : ").append(instance.getEventType().getName()).append("\n");

        if (instance.getEventType().getDescription() != null) {
            sb.append("  Description : ").append(instance.getEventType().getDescription()).append("\n");
        }

        sb.append("  Location    : ").append(instance.getLocation().getName());
        if (instance.getLocation().getAddress() != null) {
            sb.append(" (").append(instance.getLocation().getAddress()).append(")");
        }
        sb.append("\n");

        sb.append("  Date        : ").append(instance.getStartTime().format(DATE_FMT)).append("\n");
        sb.append("  Start Time  : ").append(instance.getStartTime().format(TIME_FMT)).append("\n");
        sb.append("  End Time    : ").append(instance.getEndTime().format(TIME_FMT)).append("\n");

        if (instance.getShiftStartTime() != null && instance.getShiftEndTime() != null) {
            sb.append("  Shift       : ").append(instance.getShiftStartTime().format(TIME_FMT))
                    .append(" – ").append(instance.getShiftEndTime().format(TIME_FMT)).append("\n");
        }

        if (instance.getEventType().getDefDurMins() != null) {
            sb.append("  Duration    : ").append(instance.getEventType().getDefDurMins()).append(" minutes\n");
        }

        sb.append("────────────────────────────────────\n\n");
        sb.append("Please make sure you are available at the scheduled time.\n\n");
        sb.append("Regards,\n");
        sb.append("OYCI Event Management System");

        return sb.toString();
    }
}



