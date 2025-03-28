package com.eduhubpro.eduhubpro.Util.Services;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String sourceEmail;

    @Value("${spring.mailersend.token}")
    private String accessToken;

    public boolean sendEmail(String userName, String destinationEmail, String courseName, String subject, String templateId) {
        // Crea el objeto de correo
        Email email = new Email();

        // Dirección de correo del remitente (verificada en MailerSend)
        email.setFrom("EduHubPro", sourceEmail);

        // Crea el destinatario dinámico a partir de los parámetros
        email.addRecipient(userName, destinationEmail);

        // Establece el ID de la plantilla
        email.setTemplateId(templateId);

        // Agrega variables personalizadas
        email.addPersonalization("name", userName);
        email.addPersonalization("course_name", courseName);

        // Establecer el asunto
        email.setSubject(subject);
        // Crea la instancia de MailerSend
        MailerSend ms = new MailerSend();

        // Establece el token de la API (tu token de autenticación)
        ms.setToken(accessToken);

        try {
            // Enviar el correo
            MailerSendResponse response = ms.emails().send(email);
            logger.info("Correo enviado con ID: {} ", response.messageId);
            return true;
        } catch (MailerSendException e) {
            e.printStackTrace();
            return false;
        }
    }

}
