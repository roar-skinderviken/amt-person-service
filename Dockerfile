FROM ghcr.io/navikt/poao-baseimages/java:17
COPY /application/target/amt-person-service.jar app.jar
