FROM openjdk:17 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./scripts/mvnw && \
    ./scripts/mvnw -B package -DskipTests --file pom.xml

FROM openjdk:17-slim AS runner

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar
COPY --from=builder /app/target/classes /app/classes

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
