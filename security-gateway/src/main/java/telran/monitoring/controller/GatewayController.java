package telran.monitoring.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import telran.monitoring.service.ProxyService;

@RestController
public class GatewayController {
	@Autowired
	ProxyService proxyService;
	@GetMapping("/**")
	ResponseEntity<byte[]> getRequests(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		return proxyService.proxyRouting(proxy, request, HttpMethod.GET);
	}
	@PostMapping("/**")
	ResponseEntity<byte[]> postRequests(ProxyExchange<byte[]> proxy, HttpServletRequest request) {
		return proxyService.proxyRouting(proxy, request, HttpMethod.POST);
	}

}
