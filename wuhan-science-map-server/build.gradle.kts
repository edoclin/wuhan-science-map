import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}
group = "wuhan.science"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    maven {
        setUrl("https://repo.osgeo.org/repository/release/")
    }
    maven {
        setUrl("https://repo.osgeo.org/repository/snapshot/")
    }

    maven {
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    mavenCentral()

}

dependencies {
    implementation("org.geotools:gt-geojson:27.1")
    implementation("com.alipay.sdk:alipay-sdk-java:4.35.79.ALL")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.2")
    implementation("p6spy:p6spy:3.9.1")

    implementation("cn.dev33:sa-token-spring-boot-starter:1.31.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.postgis:postgis-jdbc:2021.1.0")
    implementation("cn.hutool:hutool-all:5.8.8")
    implementation("redis.clients:jedis:4.3.0")
    implementation("cn.dev33:sa-token-jwt:1.31.0")
    implementation("com.baomidou:mybatis-plus-generator:3.5.3")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("com.qcloud:cos_api:5.6.103")
    implementation("com.qcloud:cos-sts_api:3.1.1")
    implementation("redis.clients:jedis:3.7.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    runtimeOnly("org.postgresql:postgresql:42.5.0")

    runtimeOnly("mysql:mysql-connector-java")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.plugin.ResolveMainClassName> {

}
