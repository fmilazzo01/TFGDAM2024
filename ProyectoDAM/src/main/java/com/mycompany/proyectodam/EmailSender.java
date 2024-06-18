package com.mycompany.proyectodam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

/**
 * Clase con metodos para el envio de correos
 * 
 */

public class EmailSender {

    private static final String SENDER_EMAIL = "thegymapp@outlook.com"; // EMAIL DESDE EL QUE SE ENVIAN LOS MENSAJES
    private static final String SENDER_PASSWORD = "dret3ar$@2q331sadas@4245256"; // CONTRASENA DEL EMAIL 

    //metodo para enviar un correo
    public static void sendEmail(String recipientEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email enviado correctamente a " + recipientEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }

     //metodo para enviar un correo con un archivo adjunto
    public static void sendEmailWithAttachment(String recipientEmail, String subject, String body, String filePath) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL)); //remitente
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); //destinatario
            message.setSubject(subject);

            // Crear la parte del cuerpo del mensaje
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body); //texto del mensaje

            // Crear un mensaje multipart para adjuntar el archivo
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Parte dos= el archivo adjunto
            messageBodyPart = new MimeBodyPart();
            InputStream inputStream = EmailSender.class.getResourceAsStream("/pdfs/" + filePath); // Leer el archivo adjunto como un InputStream
            DataSource source = new ByteArrayDataSource(inputStream, "application/pdf"); // Crear una fuente de datos para el archivo adjunto
            messageBodyPart.setDataHandler(new DataHandler(source));// establecer el datahandler para el archivo adjunto
            messageBodyPart.setFileName(filePath);// establecer el nombre del archivo adjunto
            multipart.addBodyPart(messageBodyPart); // anaadir la parte del archivo adjunto al multipart

            // Enviar todas las partes del mensaje
            message.setContent(multipart);

            Transport.send(message); // Enviar el mensaje

            System.out.println("Email enviado correctamente a " + recipientEmail);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Error al enviar el email con archivo adjunto", e);
        }
    }


    
    

 
}
