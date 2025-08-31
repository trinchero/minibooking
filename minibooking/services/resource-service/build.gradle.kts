plugins { id("org.springframework.boot"); id("io.spring.dependency-management") }
dependencies {
    implementation(project(":libs:common-domain"))
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
