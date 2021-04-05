FROM openjdk:8-jdk-alpine
ENV TZ=America/Sao_Paulo
RUN echo ${TZ} > /etc/timezone
RUN adduser -Dh /home/exemploapi exemploapi
USER exemploapi
EXPOSE 8080
RUN export ENCRYPT_KEY=IMSYMMETRIC
CMD ["java", "-Xmx1024m","-Xms1024m", "-jar", "/app/exemplo-api.jar", "--spring.profiles.active=docker"]
ADD ./target/exemplo-api.jar /app/
