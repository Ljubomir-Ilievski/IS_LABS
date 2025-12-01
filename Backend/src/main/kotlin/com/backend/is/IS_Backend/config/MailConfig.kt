/*
package com.backend.`is`.IS_Backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl


@Configuration
class MailConfig {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = System.getenv("MAIL_HOST");
        mailSender.port = test;
        println("PRINTING THE MAIL HOST")
        println(System.getenv("MAIL_HOST"))

        mailSender.username = "";
        mailSender.password = "";


        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}
*/
