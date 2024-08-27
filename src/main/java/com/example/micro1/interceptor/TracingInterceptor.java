package com.example.micro1.interceptor;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TracingInterceptor implements Interceptor {

    @Autowired
    private Tracer tracer;

    private final TextMapPropagator propagator;

    private final static Logger logger = LoggerFactory.getLogger(TracingInterceptor.class);

    public TracingInterceptor() {
//        this.tracer = GlobalOpenTelemetry.get().getTracer("micro1"); // Replace with your tracer name
        this.propagator = GlobalOpenTelemetry.getPropagators().getTextMapPropagator();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        Span span = tracer.spanBuilder(request.url().toString())
//                .setSpanKind(SpanKind.CLIENT)
//                .setAttribute(SemanticAttributes.HTTP_METHOD, request.method())
//                .setAttribute(SemanticAttributes.HTTP_URL, request.url().toString())
//                .startSpan();
        Span span  = tracer.currentSpan();

            // Inject the span context into the request headers
//            Context context = Context.current().with(span);
            Request.Builder requestBuilder = request.newBuilder();
//            propagator.inject(span.context(), requestBuilder, Request.Builder::addHeader);
            request = requestBuilder.build();

            MDC.put("traceId", span.context().traceId());
            MDC.put("spanId", span.context().spanId());

            for (String name : request.headers().names()) {
                logger.info("Header: {} = {}", name, request.header(name));
            }
            System.out.println("Request headers: " + request.headers());
            System.out.println("traceparent: " + request.header("traceparent"));
            System.out.println("tracestate: " + request.header("tracestate"));
            System.out.println("X-B3-TraceId: " + request.header("X-B3-TraceId"));
            System.out.println("X-B3-SpanId: " + request.header("X-B3-SpanId"));
            System.out.println("X-B3-ParentSpanId: " + request.header("X-B3-ParentSpanId"));
            System.out.println("X-B3-Sampled: " + request.header("X-B3-Sampled"));
            System.out.println("X-B3-Flags: " + request.header("X-B3-Flags"));
            System.out.println("b3: " + request.header("b3"));

            Response response = chain.proceed(request);
logger.info("Response: " + response);
//            span.setAttribute(SemanticAttributes.HTTP_STATUS_CODE, response.code());
//            if (response.isSuccessful()) {
//                span.setStatus(StatusCode.OK);
//            } else {
//                span.setStatus(StatusCode.ERROR);
//            }

            return response;

    }
}