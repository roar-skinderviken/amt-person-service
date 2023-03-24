FROM ghcr.io/navikt/poao-baseimages/java:17
COPY /build/libs/amt-person-service.jar app.jar
