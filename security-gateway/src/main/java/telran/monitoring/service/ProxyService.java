package telran.monitoring.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface ProxyService {
ResponseEntity<byte[]> proxyRouting(ProxyExchange<byte[]> proxy,
		HttpServletRequest request, HttpMethod method);
}
