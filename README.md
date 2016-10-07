# Overview #

Add UML diagrams (using PlantUML) to Javadocs. Extends the standard Java doclet.

# Dependencies #

- GraphViz (as required by PlantUML).
- Java 8 (easy enough to backport to earlier versions).

# Building #

Build using Maven:

    mvn clean install

To see an example of the updated Javadocs, use the Javadoc target:

    mvn javadoc:javadoc

Javadoc output is in target/site/apidocs.

# Installing #

To generate UML diagrams for your own project, add the following to your pom.xml. You will need to build
the project from scratch in your local environment; the distribution has not yet been uploaded to
Maven Central:

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
                    <!-- -linetype polyline / spline / ortho (default) -->
                    <!-- -dependencies public (default) / protected / package / private -->
                    <additionalparam></additionalparam>
                    <useStandardDocletOptions>true</useStandardDocletOptions>
                </configuration>
            </plugin>
        </plugins>
    </build>

# Copyright #

Use it, abuse it. Would be great if you let me know about any improvements with a Pull request.
Happy to consider any requests for mods as I have time. 

# Acknowlegments #

Many thanks to the folks at PlantUML (https://github.com/plantuml/plantuml) for their
fantastic support.
