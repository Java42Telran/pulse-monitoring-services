package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import telran.monitoring.dto.NotificationData;
import telran.monitoring.dto.PulseJump;

@SpringBootApplication
public class JumpsMailNotifierAppl {
	@Autowired
	JavaMailSender sender;
	static Logger LOG = LoggerFactory.getLogger(JumpsMailNotifierAppl.class);
	@Autowired
	NotificationDataProviderClient client;
	@Value("${app.mail.subject: Pulse Jump Notification}")
	private String subject;

	public static void main(String[] args) {
		SpringApplication.run(JumpsMailNotifierAppl.class, args);

	}
	@Bean
	Consumer<PulseJump> jumpsConsumer(){
		return this::jumpsProcessing;
		
	}
	private void jumpsProcessing(PulseJump pulseJump) {
		LOG.trace(" received pulseJump with patient {} previous value {} current value {}",
				pulseJump.patientId, pulseJump.value, pulseJump.newValue);
		sendMail(pulseJump);
		
	}
	private void sendMail(PulseJump pulseJump) {
		NotificationData data = client.getData(pulseJump.patientId);
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(data.doctorMail);
		smm.setSubject(subject);
		smm.setText(getMailText(pulseJump, data));
		sender.send(smm);
		
	}
	private String getMailText(PulseJump pulseJump, NotificationData data) {
		String res =  String.format("Dear %s\nYour patient %s"
				+ " has jump pulse value\nprevious value %d; current value %d",
				data.doctorName, data.patientName, pulseJump.value, pulseJump.newValue);
		LOG.debug(res);
		return res;
	}

}
