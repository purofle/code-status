FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG JAR_FILE
ENV JAVA_OPTS="-Xmx128m -Xms128m"
COPY ${JAR_FILE} app.jar
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
