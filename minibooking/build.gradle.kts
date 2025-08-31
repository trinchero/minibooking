plugins {
    id("org.springframework.boot") version "3.3.3" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    java
}
java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }
subprojects {
    apply(plugin = "java")
    repositories { mavenCentral() }
    tasks.withType<Test> { useJUnitPlatform() }
}
