package telran.monitoring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.monitoring.dto.PulseProbe;
import telran.monitoring.entities.ProbesList;
import telran.monitoring.repo.ProbesListsRepository;

@Service
public class AvgReducerService {
	static Logger LOG = LoggerFactory.getLogger(AvgReducerService.class);
	ProbesListsRepository probesListsRepository;
	@Value("${app.reducing.size: 3}")
	int reducingSize;

	public AvgReducerService(ProbesListsRepository probesListsRepository) {
		this.probesListsRepository = probesListsRepository;
	}
	@Transactional
	public Integer reducing(PulseProbe probe) {
		ProbesList probesList = probesListsRepository.findById(probe.patientId).orElse(null);
		Integer avgValue = null;
		if (probesList == null) {
			LOG.debug("for patient {} no saved pulse values", probe.patientId);
			probesList = new ProbesList(probe.patientId);
		} else {
			LOG.trace("for patient {} number of saved pulse values is {}",
					probesList.getPatientId(), probesList.getPulseValues().size());
		}
		List<Integer> pulseValues = probesList.getPulseValues();
		pulseValues.add(probe.value);
		if (pulseValues.size() >= reducingSize) {
			 avgValue = pulseValues.stream().collect(Collectors.averagingInt(x -> x)).intValue();
			pulseValues.clear();
		}
		probesListsRepository.save(probesList);
		return avgValue;
		
	}

}
