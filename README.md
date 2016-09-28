# Overview #

Add UML diagrams (using PlantUML) to Javadocs. Extends the standard Java doclet.

Build using Maven:

    mvn clean install

To see an example of the updated Javadocs, use the Javadoc target:

    mvn javadoc:javadoc

Javadoc output is in target/site/apidocs.

To generate UML diagrams for your own project, add the following to your pom.xml:

    <build>
        <plugins>
            <!-- Generate UML diagrams in javadoc using doclet: mvn javadoc:javadoc -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>2.10.4</version>
                <configuration>
                    <doclet>info.leadinglight.umljavadoclet.UmlJavaDoclet</doclet>
                    <docletArtifact>
                        <groupId>info.leadinglight</groupId>
                        <artifactId>uml-java-doclet</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </docletArtifact>
                    <additionalparam></additionalparam>
                    <useStandardDocletOptions>false</useStandardDocletOptions>
                </configuration>
            </plugin>
        </plugins>
    </build>