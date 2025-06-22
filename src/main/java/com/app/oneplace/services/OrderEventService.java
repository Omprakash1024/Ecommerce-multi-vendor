package com.app.oneplace.services;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Order;
import com.app.oneplace.repo.SellerRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.app.oneplace.model.Seller;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final JavaMailSender javaMailSender;
    private final SellerRepository sellerRepository;

    @Async("taskExecutor")
    public void sendOrderConfirmation(AppUser user, Set<Order> orders) throws MessagingException {

        String email = user.getEmail();

        String orderIds = orders.stream()
                .map(order -> "#" + order.getId())
                .reduce("", (acc, id) -> acc.isEmpty() ? id : acc + ", " + id);

        String subject = "Order Confirmation - Orders: " + orderIds;
        String body = "Your orders have been successfully placed on OnePlace!\n\n"
                + "Order IDs: " + orderIds + "\n"
                + "Track them via your OnePlace app.";
        try {
            sendEmailHandler(email, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("exit email send ");
        log.info("📧 Sending order confirmation to user: {} for {} orders", user.getUsername(), orders.size());

    }

    @Async("taskExecutor")
    public void notifySeller(Long sellerId, Set<Order> orders) throws Exception, MessagingException {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new Exception("Seller not found with the seller id : " + sellerId));
        String email = seller.getEmail();

        StringBuilder bodyBuilder = new StringBuilder("New Orders on OnePlace:\n\n");

        orders.forEach(order -> {
            bodyBuilder.append("Order ID: #").append(order.getId()).append("\n")
                    .append("User: ").append(order.getUser().getUsername()).append("\n")
                    .append("Address: ").append(order.getShippingAddress().getAddress()).append(", ")
                    .append(order.getShippingAddress().getCity()).append(", ")
                    .append(order.getShippingAddress().getState()).append(" - ")
                    .append(order.getShippingAddress().getPinCode()).append("\n")
                    .append("Mobile: ").append(order.getShippingAddress().getMobile()).append("\n\n");
        });

        String subject = "New Order(s) Notification - " + orders.size() + " New Order(s)";

        sendEmailHandler(email, subject, bodyBuilder.toString());

        log.info("📧 Notified seller {} for {} order(s)", seller.getId(), orders.size());
    }

    // Add more async operations as needed — refunds, analytics etc.

    public void sendEmailHandler(String userEmail, String subject, String text) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setTo(userEmail);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new MailSendException("Failed to send Email");
        }
    }
}
