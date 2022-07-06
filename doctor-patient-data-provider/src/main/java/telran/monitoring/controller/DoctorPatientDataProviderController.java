package telran.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static telran.monitoring.api.ApiConstants.*;

import telran.monitoring.dto.NotificationData;
import telran.monitoring.service.DoctorPatientDataProviderService;

@RestController
public class DoctorPatientDataProviderController {
	@Autowired
	DoctorPatientDataProviderService  service;
	@GetMapping(DOCTOR_PATIENT_PROVIDER_URL + "{id}")
	NotificationData getDoctorPatientData(@PathVariable(name = "id") long patientId) {
		return service.getDoctorPatientData(patientId);
	}

}
