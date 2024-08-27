package com.example.micro1.controller;

import com.example.micro1.client.NewDto;
import com.example.micro1.service.YourService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TracingController {

    @Autowired
    private Tracer tracer;

    @Autowired
    private YourService yourService;

    private static final Logger logger = LoggerFactory.getLogger(TracingController.class);

    @GetMapping("/trace")
    public String trace() throws IOException {
        NewDto newDto = null;
        TraceContext context = tracer.currentSpan().context();
        logger.info("Tracing endpoint hit!"+ context.traceId() + " ::" + context.spanId());
            newDto =  yourService.getYourData("1");
        return "Tracing endpoint hit!"+ newDto.traceId;
    }
}