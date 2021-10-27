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
#COPY src src
COPY stacks-java-core-api stacks-java-core-api
COPY stacks-java-core-azure stacks-java-core-azure
COPY stacks-java-core-commons stacks-java-core-commons
COPY stacks-java-core-cqrs stacks-java-core-cqrs
COPY stacks-java-core-messaging stacks-java-core-messaging
COPY stacks-java-core-operations stacks-java-core-operations
COPY sample-cqrs-events sample-cqrs-events

# RUN ./mvnw help:evaluate -Dexpression=settings.localRepository -q -DforceStdout

#RUN ./mvnw clean \
#    && ./mvnw dependency:go-offline --quiet\
#	&& ./mvnw install -Dmaven.default-test.skip=true -Dmaven.test.skip=true -DskipTests --no-transfer-progress --quiet -Plocal\
#    && mkdir -p target/dependency && (cd target/dependency; ${JAVA_HOME}/bin/jar -xf /workspace/app/sample-cqrs-events/target/*.jar) \
RUN ./mvnw clean
RUN ./mvnw install -Dmaven.default-test.skip=true -Dmaven.test.skip=true -DskipTests --no-transfer-progress

FROM azul/zulu-openjdk-alpine:11-jre

WORKDIR /workspace/app
COPY --from=build /workspace/app .

VOLUME /tmp

RUN if [ -e "/app/emulatorcert.crt" ]; then cd $JAVA_HOME/lib/security && keytool -importcert -cacerts -storepass changeit -noprompt -alias documentdbemulator -file /app/emulatorcert.crt; fi

#RUN if [ "$AUTH_MS_PROFILE" = "test" ]; then mvn verify ; fi

#RUN \
#        cd $JAVA_HOME/lib/security \
#        && keytool -importcert -cacerts -storepass changeit -noprompt -alias documentdbemulator -file /app/emulatorcert.crt

ENTRYPOINT ["./mvnw","spring-boot:run", "-pl","sample-cqrs-events"]
EXPOSE 9000
