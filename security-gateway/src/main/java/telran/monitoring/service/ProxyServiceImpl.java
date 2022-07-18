package telran.monitoring.service;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class ProxyServiceImpl implements ProxyService {
	static Logger LOG = LoggerFactory.getLogger(ProxyService.class);
	@Value("#{${app.back.office.hosts}}")
	Map<String, String> mapOfficeServices;

	@Override
	public ResponseEntity<byte[]> proxyRouting(ProxyExchange<byte[]> proxy, HttpServletRequest request,
			HttpMethod method) {
		switch(method) {
		case GET: return getRouting(proxy, request);
		case POST: return postRouting(proxy, request);
		default: return null;
		}
	}
	private ResponseEntity<byte[]> getRouting(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		String host = getServiceHost(request);
		String queryString = request.getQueryString();
		if (host == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(String.format("host for %s not found", request.getRequestURI()).getBytes());
		}
		String uri = String.format("%s%s?%s", host, request.getRequestURI(), queryString == null ? "" :
			queryString);
		LOG.debug("received URL is {}", uri);
		ProxyExchange<byte[]> proxyUri = proxy.uri(uri);
		return proxyUri.get();
	}
	private ResponseEntity<byte[]> postRouting(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		String host = getServiceHost(request);
		if (host == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(String.format("host for %s not found", request.getRequestURI()).getBytes());
		}
		String uri = host + request.getRequestURI();
		LOG.debug("received URL is {}", uri);
		ProxyExchange<byte[]> proxyUri = proxy.uri(uri);
		
		return proxyUri.post();
	}
	private String getServiceHost(HttpServletRequest request) {
		String servicePrefix = request.getRequestURI().split("/+")[1];
		LOG.trace(" service prefix is {}", servicePrefix);
		
		return mapOfficeServices.get(servicePrefix);
	}
	@PostConstruct
	void logBackOfficeHosts() {
		LOG.info("service hosts: {}", mapOfficeServices);
	}

}
