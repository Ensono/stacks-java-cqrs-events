FROM azul/zulu-openjdk-alpine:11 as build
WORKDIR /workspace/app
ARG DEFAULT_MODULE=sample-cqrs-events
# Copy the m2 directory if it exists to re-use an external cache.
# The `mvnw` file is also copied so that if an `.m2` directory isn't present
# the command won't fail.
COPY mvnw .m2* /root/.m2/repository/

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .


COPY stacks-java-core-api stacks-java-core-api
COPY stacks-java-core-azure stacks-java-core-azure
COPY stacks-java-core-commons stacks-java-core-commons
COPY stacks-java-core-cqrs stacks-java-core-cqrs
COPY stacks-java-core-messaging stacks-java-core-messaging
COPY stacks-java-core-operations stacks-java-core-operations
COPY sample-cqrs-events sample-cqrs-events

RUN ./mvnw clean\
  && ./mvnw dependency:go-offline --quiet\
	&& ./mvnw install -Dmaven.default-test.skip=true -Dmaven.test.skip=true -DskipTests --no-transfer-progress --quiet \
  && mkdir -p extracted && java -Djarmode=layertools -jar ${DEFAULT_MODULE}/target/*.jar extract --destination extracted

RUN ls -lrt extracted

FROM azul/zulu-openjdk-alpine:11-jre

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/extracted

COPY --from=build ${DEPENDENCY}/dependencies/ ./
RUN true
COPY --from=build ${DEPENDENCY}/spring-boot-loader/ ./
RUN true
COPY --from=build ${DEPENDENCY}/snapshot-dependencies/ ./
RUN true
COPY --from=build ${DEPENDENCY}/application/ ./

RUN if [ -e "/app/emulatorcert.crt" ]; then cd $JAVA_HOME/lib/security && keytool -importcert -cacerts -storepass changeit -noprompt -alias documentdbemulator -file /app/emulatorcert.crt; fi

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]

EXPOSE 9000
