package com.Abhijith.api_gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RoutesController {

@Autowired
private GatewayMvcProperties gatewayMvcProperties;

	@GetMapping("/routes")
	public List<Map<String, Object>> getRouteSummary() {
		return gatewayMvcProperties.getRoutes().stream().map(route -> Map.of(
				"id", route.getId(),
				"uri", route.getUri().toString(),
				"paths", route.getPredicates().stream()
						         .filter(p -> "Path".equals(p.getName()))
						         .flatMap(p -> p.getArgs().values().stream())
						         .toList(),
				"rewrite", route.getFilters().stream()
						           .filter(f -> "RewritePath".equals(f.getName()))
						           .findFirst()
						           .map(f -> f.getArgs())
						           .orElse(null)
		)).toList();
	}

}
