plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-jaeger:1.10.1")
	implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
	implementation("io.zipkin.reporter2:zipkin-sender-urlconnection")
	implementation("io.opentelemetry:opentelemetry-extension-trace-propagators:1.9.1")
	implementation("io.micrometer:micrometer-core")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.okhttp3:okhttp:4.9.3")
	implementation("io.opentelemetry:opentelemetry-semconv:1.9.1-alpha")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
	useJUnitPlatform()
}
