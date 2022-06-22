package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.monitoring.dto.PulseProbe;
import telran.monitoring.service.AvgReducerService;

@SpringBootApplication
public class AvgReducerAppl {
	@Autowired
	AvgReducerService service;
static Logger LOG = LoggerFactory.getLogger(AvgReducerAppl.class);
	public static void main(String[] args) {
		SpringApplication.run(AvgReducerAppl.class, args);

	}
	@Bean
	Consumer<PulseProbe> pulseProbeConsumer() {
		return this::processPulseProbe;
	}
	void processPulseProbe(PulseProbe probe) {
		Integer avgValue = service.reducing(probe);
		if (avgValue != null) {
			LOG.debug("for patient {} avg value is {}", probe.patientId, avgValue);
		}
		
	}

}
