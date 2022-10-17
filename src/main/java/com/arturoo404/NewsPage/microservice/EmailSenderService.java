package com.arturoo404.NewsPage.microservice;

import javax.mail.MessagingException;

public interface EmailSenderService {

    void sendMail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException;
}
