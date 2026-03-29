package org.redcode.bookanddriveservice.notifications.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redcode.bookanddriveservice.instructors.domain.Instructor;
import org.redcode.bookanddriveservice.instructors.service.InstructorsService;
import org.redcode.bookanddriveservice.lessons.domain.Lesson;
import org.redcode.bookanddriveservice.trainees.domain.Trainee;
import org.redcode.bookanddriveservice.trainees.service.TraineesService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonNotificationService {

    private static final String SENDER = "onboarding@resend.dev";
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy");
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final Resend resend;
    private final IcsGeneratorService icsGeneratorService;
    private final InstructorsService instructorsService;
    private final TraineesService traineesService;

    public void sendLessonScheduledEmail(Lesson lesson, Trainee trainee) {
        Instructor instructor = instructorsService.findById(lesson.getInstructor().getId());
        Trainee fullTrainee = traineesService.findById(trainee.getId());

        if (instructor == null || fullTrainee == null) {
            log.warn("Cannot send lesson scheduled email: instructor or trainee not found [lessonId={}, traineeId={}]",
                lesson.getId(), trainee.getId());
            return;
        }

        String icsBase64 = icsGeneratorService.generateBase64(lesson, instructor);
        String htmlBody = buildHtmlBody(lesson, instructor, fullTrainee);
        String instructorFullName = instructor.getName() + " " + instructor.getSurname();

        Attachment icsAttachment = Attachment.builder()
            .fileName("lesson.ics")
            .content(icsBase64)
            .build();

        CreateEmailOptions request = CreateEmailOptions.builder()
            .from(SENDER)
            .to(fullTrainee.getEmail())
            .subject("Driving Lesson Scheduled – " + lesson.getStartTime().format(DISPLAY_DATE_FORMAT))
            .html(htmlBody)
            .attachments(List.of(icsAttachment))
            .build();

        try {
            resend.emails().send(request);
            log.info("Lesson scheduled email sent [lessonId={}, traineeId={}, instructor={}]",
                lesson.getId(), fullTrainee.getId(), instructorFullName);
        } catch (ResendException e) {
            log.error("Failed to send lesson scheduled email [lessonId={}, traineeId={}]: {}",
                lesson.getId(), fullTrainee.getId(), e.getMessage());
        }
    }

    private String buildHtmlBody(Lesson lesson, Instructor instructor, Trainee trainee) {
        String instructorFullName = instructor.getName() + " " + instructor.getSurname();
        String date = lesson.getStartTime().format(DISPLAY_DATE_FORMAT);
        String startTime = lesson.getStartTime().format(DISPLAY_TIME_FORMAT);
        String endTime = lesson.getEndTime().format(DISPLAY_TIME_FORMAT);

        return "<html><body>" +
               "<h2>Driving Lesson Scheduled</h2>" +
               "<p>Hi " + trainee.getName() + ",</p>" +
               "<p>Your driving lesson has been scheduled. Here are the details:</p>" +
               "<ul>" +
               "<li><strong>Date:</strong> " + date + "</li>" +
               "<li><strong>Time:</strong> " + startTime + " – " + endTime + "</li>" +
               "<li><strong>Instructor:</strong> " + instructorFullName + "</li>" +
               "</ul>" +
               "<p>A calendar invite is attached. See you there!</p>" +
               "</body></html>";
    }
}
