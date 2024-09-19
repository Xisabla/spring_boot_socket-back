FROM openjdk:17 AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && \
    ./mvnw clean package -DskipTests

FROM openjdk:17-slim AS runner

WORKDIR /app

RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar /app/app.jar
COPY --from=builder /app/target/classes /app/classes

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
