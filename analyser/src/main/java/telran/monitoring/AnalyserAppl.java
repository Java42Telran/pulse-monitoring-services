package telran.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.monitoring.dto.PulseProbe;

@SpringBootApplication
public class AnalyserAppl {
private static final long TIMEOUT = 3000;
static Logger LOG = LoggerFactory.getLogger(AnalyserAppl.class) ;
	public static void main(String[] args) {
		SpringApplication.run(AnalyserAppl.class, args);

	}
	@Bean
	Consumer<PulseProbe> pulseProbeConsumer() {
		return this::processPulseProbe;
	}
	void processPulseProbe(PulseProbe probe) {
		LOG.debug("sequenceNumber: {}, delay: {}",
				probe.seqNumber, System.currentTimeMillis() - probe.timestamp);
		try {
			Thread.sleep(TIMEOUT);
		} catch (InterruptedException e) {
			
		}
	}

}
