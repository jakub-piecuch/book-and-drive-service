package org.redcode.bookanddriveservice.notifications.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.config.SmsConfig;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwilioSmsService implements SmsService {

    private final SmsConfig smsConfig;

    @PostConstruct
    void init() {
        Twilio.init(smsConfig.getAccountSid(), smsConfig.getAuthToken());
    }

    @Override
    public void send(String to, String message) {
        Message.creator(new PhoneNumber(to), new PhoneNumber(smsConfig.getFromNumber()), message)
            .create();
        log.info("SMS sent to {}", to);
    }
}
