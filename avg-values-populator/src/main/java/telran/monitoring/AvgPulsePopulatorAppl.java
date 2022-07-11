package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.monitoring.dto.PulseProbe;
import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.repo.AvgValueRepository;

@SpringBootApplication
public class AvgPulsePopulatorAppl {
	static Logger LOG = LoggerFactory.getLogger(AvgPulsePopulatorAppl.class);
	@Autowired
AvgValueRepository valueRepository;
	public static void main(String[] args) {
		SpringApplication.run(AvgPulsePopulatorAppl.class, args);

	}
	@Bean
	Consumer<PulseProbe> avgPulseConsumer() {
		return this::getAvgPulseConsumer;
	}
	
	void getAvgPulseConsumer(PulseProbe probe) {
		
		AvgPulseDoc res = valueRepository.save(AvgPulseDoc.build(probe));
		LOG.trace("avg value {} for patient {} has been saved", res.getValue(), res.getPatientId());
	}

}
