package com.example.micro1.config;


import io.micrometer.tracing.otel.bridge.*;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporterBuilder;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import java.util.Collections;

import static io.opentelemetry.sdk.trace.samplers.Sampler.alwaysOn;

@Configuration
public class OpenTelemetryConfig {

    @Bean
    public Tracer tracer() {

        SpanExporter spanExporter = new ZipkinSpanExporterBuilder()
                .setSender(URLConnectionSender.create("http://localhost:9411/api/v2/spans"))
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setSampler(alwaysOn())
                .setResource(
                        Resource.getDefault().merge(
                                Resource.create(
                                        Attributes.builder()
                                                .put(ResourceAttributes.SERVICE_NAME, "micro1")
                                                .build())))
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(
                        TextMapPropagator.composite(
                                W3CTraceContextPropagator.getInstance(),
                                B3Propagator.injectingMultiHeaders() // Use B3Propagator.injectingSingleHeader() for single header B3, or B3Propagator.injectingMultiHeaders() for multi-header B3
                        )
                ))
                .build();
        GlobalOpenTelemetry.set(openTelemetrySdk);
        return openTelemetrySdk.getTracerProvider()
                .get("io.micrometer.micrometer-tracing");
    }


    @Bean
    public OtelCurrentTraceContext otelCurrentTraceContext() {
        return new OtelCurrentTraceContext();
    }

    @Bean
    public Slf4JBaggageEventListener slf4JBaggageEventListener() {
        return new Slf4JBaggageEventListener(Collections.emptyList());
    }

    @Bean
    public Slf4JEventListener slf4JEventListener() {
        return new Slf4JEventListener();
    }


    @Bean
    public OtelTracer otelTracer(Slf4JEventListener slf4JEventListener, Slf4JBaggageEventListener slf4JBaggageEventListener, OtelCurrentTraceContext otelCurrentTraceContext, Tracer tracer) {
        return new OtelTracer(tracer, otelCurrentTraceContext, event -> {
            slf4JEventListener.onEvent(event);
            slf4JBaggageEventListener.onEvent(event);
        }, new OtelBaggageManager(otelCurrentTraceContext, Collections.emptyList(), Collections.emptyList()));
    }
}
