plugins { id("org.springframework.boot"); id("io.spring.dependency-management") }
dependencies {
    implementation(project(":libs:common-domain"))
    implementation(project(":libs:common-kafka"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-streams:3.7.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}
