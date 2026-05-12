# =====================================================================
# Stage 1: build com Maven + JDK 17 (Eclipse Temurin)
# =====================================================================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp -DskipTests package

RUN cp target/*.jar app.jar

# =====================================================================
# Stage 2: runtime leve com JRE 17
# =====================================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder --chown=spring:spring /build/app.jar /app/app.jar

ENV JAVA_OPTS="-Xms128m -Xmx400m -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
ENV SPRING_PROFILES_ACTIVE=local

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar /app/app.jar"]
