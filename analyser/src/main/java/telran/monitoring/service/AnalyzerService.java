package telran.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.monitoring.dto.*;
import telran.monitoring.entities.LastProbe;
import telran.monitoring.repo.LastProbesRepository;

@Service
public class AnalyzerService {
	static Logger LOG = LoggerFactory.getLogger(AnalyzerService.class);
	@Value("${app.jump.threshold: 0.6}")
	double jumpThreshold;
LastProbesRepository probesRepository;

public AnalyzerService(LastProbesRepository probesRepository) {
	this.probesRepository = probesRepository;
}
@Transactional
public PulseJump processProbe(PulseProbe probe) {
	LastProbe lastProbe = probesRepository.findById(probe.patientId).orElse(null);
	PulseJump jump = null;
	if (lastProbe == null) {
		LOG.debug("for patient {} no saved values", probe.patientId);
		lastProbe = new LastProbe(probe.patientId, probe.value);
	} else {
		LOG.trace("for patient {} last value {}", lastProbe.getPatientId(), lastProbe.getLastValue());
		if (isJump(lastProbe.getLastValue(), probe.value)) {
			jump = new PulseJump(lastProbe.getPatientId(), lastProbe.getLastValue(), probe.value);
		}
		lastProbe.setLastValue(probe.value);
	}
	probesRepository.save(lastProbe);
	return jump;
}
private boolean isJump(int lastValue, int value) {
	int delta = Math.abs(lastValue - value);
	
	return delta >= lastValue * jumpThreshold;
}
}
