FROM azul/zulu-openjdk-alpine:11 as build
WORKDIR /workspace/app

# Copy the m2 directory if it exists to re-use an external cache.
# The `mvnw` file is also copied so that if an `.m2` directory isn't present
# the command won't fail.
COPY mvnw .m2* /root/.m2/repository/

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ls -la /root/.m2/repository

# RUN ./mvnw help:evaluate -Dexpression=settings.localRepository -q -DforceStdout

RUN ./mvnw dependency:go-offline \
	&& ./mvnw process-resources \
	&& ./mvnw package -Dmaven.default-test.skip=true -Dmaven.test.skip=true -DskipTests --no-transfer-progress \
	&& mkdir -p target/dependency && (cd target/dependency; ${JAVA_HOME}/bin/jar -xf ../*.jar)

FROM azul/zulu-openjdk-alpine:11-jre

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.xxAMIDOxx.xxSTACKSxx.Application"]

EXPOSE 9000
