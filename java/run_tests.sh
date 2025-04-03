export BASE_URL=http://localhost:9000

mvn -f ../api-tests/pom.xml clean verify
open ../api-tests/target/site/serenity/index.html