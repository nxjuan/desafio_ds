# ---- build ----
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY api/pom.xml .
COPY api/src ./src
RUN mvn -q -DskipTests package

# ---- run ----
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
