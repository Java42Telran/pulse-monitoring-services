package telran.monitoring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import telran.monitoring.dto.PulseProbe;
import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.repo.AvgValueRepository;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgValuesPopulatorTest {
	@Autowired
	AvgValueRepository avgRepository;
	@Autowired
	InputDestination producer;
	PulseProbe[] probes = {
			new PulseProbe(123, 100),
			new PulseProbe(124, 150)
			
	};
	
	String bindingName = "avgPulseConsumer-in-0";
	@Test
	void test() {
		producer.send(new GenericMessage<PulseProbe>(probes[0]), bindingName);
		producer.send(new GenericMessage<PulseProbe>(probes[1]), bindingName);
		AvgPulseDoc docsExpected[] = {
			AvgPulseDoc.build(probes[0]),
			AvgPulseDoc.build(probes[1])
		}; 
		List<AvgPulseDoc> docsActual = avgRepository.findAll();
		assertEquals(docsExpected[0], docsActual.get(0));
		assertEquals(docsExpected[1], docsActual.get(1));
	}
	
}
