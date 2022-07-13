package telran.monitoring;

import java.net.URI;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import static telran.monitoring.api.ApiConstants.*;

import telran.monitoring.dto.NotificationData;

@Component
public class NotificationDataProviderClient {
	static Logger  LOG = LoggerFactory.getLogger(NotificationDataProviderClient.class);
	RestTemplate restTemplate = new RestTemplate();
	@Value("${app.data.provider.host}")
	String dataProviderHost;
	@Value("${app.data.provider.port: 8282}")
	int dataProviderPort;
public NotificationData getData(long patientId) {
	String url = getDataProviderUrl(patientId);
	LOG.trace("sending request for notification data to {}", url);
	ResponseEntity<NotificationData> response = restTemplate.exchange(url,
			HttpMethod.GET, null, NotificationData.class);
	return response.getBody();
}
private String getDataProviderUrl(long patientId) {
	
	return String.format("%s:%d%s%d", dataProviderHost, dataProviderPort, DOCTOR_PATIENT_PROVIDER_URL,
			patientId);
}
}
