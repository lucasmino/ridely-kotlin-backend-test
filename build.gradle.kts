import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
}

group = "tech.jaya"
version = "0.0.1-SNAPSHOT"

val flywayVersion = "10.10.0"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.h2database:h2")

	// Development tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Open API
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// Database & Migration
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core:$flywayVersion")
	implementation("com.mysql:mysql-connector-j:8.4.0")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")


	runtimeOnly("org.flywaydb:flyway-mysql:$flywayVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//aws tools
	implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs:3.0.2")

	implementation("me.paulschwarz:spring-dotenv:3.0.0")


}

//tasks.test {
//	ignoreFailures = true
//}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
