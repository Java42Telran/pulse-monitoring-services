package telran.monitoring.service;

import telran.monitoring.dto.NotificationData;

public interface DoctorPatientDataProviderService {
NotificationData getDoctorPatientData(long patientId);
}
