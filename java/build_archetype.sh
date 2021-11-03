rm -rf archetype
mvn clean archetype:create-from-project -DpropertyFile=archetype.properties
mvn install -f archetype/pom.xml