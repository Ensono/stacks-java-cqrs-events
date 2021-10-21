mvn -f ../api-tests/pom.xml clean verify
open ../api-tests/target/site/serenity/index.html
mvn -f ../api-tests-karate/pom.xml clean test
open ../api-tests-karate/target/surefire-reports/karate-summary.html