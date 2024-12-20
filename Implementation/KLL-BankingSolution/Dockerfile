# Step 1: Use an official JDK runtime as a base image
FROM openjdk:21

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the application JAR file
COPY target/KLL-BankingSolution-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the default Spring Boot port
EXPOSE 8080

# Step 5: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
