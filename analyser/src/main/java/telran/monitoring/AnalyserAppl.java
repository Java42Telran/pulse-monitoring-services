package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.monitoring.dto.PulseJump;
import telran.monitoring.dto.PulseProbe;
import telran.monitoring.service.AnalyzerService;

@SpringBootApplication
public class AnalyserAppl {
	@Autowired
AnalyzerService service;
static Logger LOG = LoggerFactory.getLogger(AnalyserAppl.class) ;
	public static void main(String[] args) {
		SpringApplication.run(AnalyserAppl.class, args);

	}
	@Bean
	Consumer<PulseProbe> pulseProbeConsumer() {
		return this::processPulseProbe;
	}
	void processPulseProbe(PulseProbe probe) {
		try {
			PulseJump jump = service.processProbe(probe);
			if (jump != null) {
				LOG.debug("jump: patient {}; previous value {}; current value {}",
						jump.patientId, jump.value, jump.newValue);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}

}
