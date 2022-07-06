package telran.monitoring.service;

import org.springframework.stereotype.Service;

import telran.monitoring.dto.NotificationData;
import telran.monitoring.repo.*;
@Service
public class DoctorPatientDataProviderServiceImpl implements DoctorPatientDataProviderService {
DoctorRepository doctorRepository;
PatientRepository patientRepository;
VisitRepository visitRepository;

	public DoctorPatientDataProviderServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository,
		VisitRepository visitRepository) {
	this.doctorRepository = doctorRepository;
	this.patientRepository = patientRepository;
	this.visitRepository = visitRepository;
}

	@Override
	public NotificationData getDoctorPatientData(long patientId) {
		String doctorEmail = visitRepository.getDoctorEmail(patientId);
		String doctorName = doctorRepository.findById(doctorEmail).get().getName();
		String patientName = patientRepository.findById(patientId).get().getName();
		return new NotificationData(doctorEmail, doctorName, patientName);
	}

}
