package org.redcode.bookanddriveservice.notifications.sms;

public interface SmsService {

    void send(String to, String message);
}
