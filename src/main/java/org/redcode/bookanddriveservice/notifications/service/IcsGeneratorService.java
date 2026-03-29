package org.redcode.bookanddriveservice.notifications.service;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.springframework.stereotype.Service;

@Service
public class IcsGeneratorService {

    private static final DateTimeFormatter ICS_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public String generateBase64(Lesson lesson, Instructor instructor) {
        String ics = buildIcs(lesson, instructor);
        return Base64.getEncoder().encodeToString(ics.getBytes());
    }

    private String buildIcs(Lesson lesson, Instructor instructor) {
        String instructorFullName = instructor.getName() + " " + instructor.getSurname();

        return "BEGIN:VCALENDAR\r\n" +
               "VERSION:2.0\r\n" +
               "PRODID:-//DriveDesk//EN\r\n" +
               "BEGIN:VEVENT\r\n" +
               "UID:" + UUID.randomUUID() + "\r\n" +
               "DTSTART:" + lesson.getStartTime().format(ICS_DATE_FORMAT) + "\r\n" +
               "DTEND:" + lesson.getEndTime().format(ICS_DATE_FORMAT) + "\r\n" +
               "SUMMARY:Driving Lesson - " + instructorFullName + "\r\n" +
               "DESCRIPTION:Instructor: " + instructorFullName + "\r\n" +
               "END:VEVENT\r\n" +
               "END:VCALENDAR\r\n";
    }
}
