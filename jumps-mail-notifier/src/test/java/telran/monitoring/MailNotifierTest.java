package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import telran.monitoring.dto.NotificationData;
import telran.monitoring.dto.PulseJump;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class MailNotifierTest {
	private static final long PATIENT_ID = 123;
	private static final int PREVIOUS_VALUE = 50;
	private static final int CURRENT_VALUE = 150;
	private static final String DOCTOR_MAIL = "doctor@gmail.com";
	private static final String DOCTOR_NAME = "Doctor";
	private static final String PATIENT_NAME = "Patient";
	@Autowired
InputDestination provider;
	@RegisterExtension
		GreenMailExtension mailExtension =
		new GreenMailExtension(ServerSetupTest.SMTP)
		.withConfiguration(GreenMailConfiguration.aConfig().withUser("pulse", "12345.com"));
	PulseJump jump = new PulseJump(PATIENT_ID, PREVIOUS_VALUE, CURRENT_VALUE);
	@MockBean
	NotificationDataProviderClient client;
	@Value("${app.mail.subject}")
	String subject;
	
	@Test
	void test() throws Exception {
		when(client.getData(PATIENT_ID))
		.thenReturn(new NotificationData(DOCTOR_MAIL, DOCTOR_NAME, PATIENT_NAME));
		provider.send(new GenericMessage<PulseJump>(jump), "jumpsConsumer-in-0");
		MimeMessage message = mailExtension.getReceivedMessages()[0];
		assertEquals(DOCTOR_MAIL, message.getAllRecipients()[0].toString());
		assertEquals(subject, message.getSubject());
		
	}

}
