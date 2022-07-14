package telran.monitoring;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.monitoring.dto.DoctorDto;
import telran.monitoring.dto.PatientDto;
import telran.monitoring.dto.VisitDto;
import telran.monitoring.service.VisitsService;

@SpringBootApplication
public class VisitsAppl {
	@Autowired
	VisitsService service;
	@Value("${app.integration.test.enabled: true}")
	boolean isIntegrationTest;
	@Value("${app.amount.patients: 10}")
	int nPatients;
	@Value("${app.amount.visits: 100}")
	int nVisits;
	@Value("${app.amount.doctors: 5}")
	int nDoctors;
	ThreadLocalRandom tlr = ThreadLocalRandom.current();
public static void main(String[] args) {
	SpringApplication.run(VisitsAppl.class, args);
}
@PostConstruct
void dbInit() {
	if (isIntegrationTest) {
		List<VisitDto> visits = service.getAllVisits(1);
		if (visits.isEmpty()) {
			initDb();
		}
	}
}
private void initDb() {
	createPatients();
	createDoctors();
	createVisits();
	
}
private void createVisits() {
	Stream.generate(()->getRandomVisit()).limit(nVisits).forEach(v -> service.addVisit(v));
	
}
private VisitDto getRandomVisit() {
	long patientId = tlr.nextLong(1, nPatients + 1);
	String doctorEmail = String.format("doctor%d@gmail.com", tlr.nextInt(1, nDoctors + 1));
	String date = LocalDate.of(2022, tlr.nextInt(1, 11), tlr.nextInt(1, 28)).toString();
	return new VisitDto(patientId, doctorEmail, date);
}
private void createDoctors() {
	for (int i = 1; i <= nDoctors; i++) {
		service.addDoctor(new DoctorDto(String.format("doctor%d@gmail.com", i),
				String.format("Doctor%d", i)));
	}
	
}
private void createPatients() {
	for (int i = 1; i <= nPatients; i++) {
		service.addPatient(new PatientDto(i, "patient" + i));
	}
	
}

}
