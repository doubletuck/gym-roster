FROM eclipse-temurin:21-alpine

COPY target/gym-roster.jar /opt/app/

ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""
ENV SPRING_PROFILES_ACTIVE=""

CMD [ "java", "-jar", "/opt/app/gym-roster.jar" ]