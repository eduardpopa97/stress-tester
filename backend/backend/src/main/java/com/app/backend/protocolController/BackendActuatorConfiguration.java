package com.app.backend.protocolController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cedarsoftware.util.io.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class BackendActuatorConfiguration {

	@Bean
	public HttpTraceRepository httpTraceRepository() throws IOException {
		return new InMemoryHttpTraceRepository() {
			ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
			Logger logger = LoggerFactory.getLogger(InMemoryHttpTraceRepository.class);

			@Override
			public void add(HttpTrace trace) {
				try {
//					logger.info(objectMapper.writeValueAsString(trace));
					BufferedWriter locallogger = new BufferedWriter(new FileWriter(
							"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\"
							+ "src\\main\\resources\\logs\\loggerSpringBootServer"+
							String.valueOf(System.currentTimeMillis())+".txt"));
					locallogger.append(JsonWriter.formatJson(
							objectMapper.writeValueAsString(trace)));
					locallogger.append(" ");
					locallogger.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} 
				super.add(trace);
			}
		};
	}
}
