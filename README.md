## Overview ##

Add UML diagrams (using PlantUML) to Javadocs. Extends the standard Java doclet.

### Background ###

The inspiration for this project is better communication between the developers on my team. We want to 
follow the principles of DDD and Model-Driven Design 
(https://www.amazon.ca/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215/ref=sr_1_1?s=books&ie=UTF8&qid=1475876769&sr=1-1&keywords=domain+driven+design) 
and automatically generating class diagrams is a key part of that approach.

### Diagrams ###

Generates three types of diagrams:

- Overview: Shows all classes within all packages. Click on a class to get to the Javadoc for that class.
Click on a package to get to the Javadoc for that package.
- Package: Shows all of the classes within a package, and the relationships between them. Does not
show relationships with classes outside the package.
- Class: Shows all of the relationships with other classes. If the other class is in the same package,
it is shown in yellow (default color). If in another package within the set of packages, it is shown
in white. If the other class is outside any of the packages, it is shown in grey.

The relationships with other classes are determined by what the Javadoc API provides, which is from the
attribute and method declarations for a class. Any usages that are buried within code will not be shown.
If I think a relationship is important enough to be shown, I will make it explicit (e.g. add an attribute
to the other class). 

Layouts can appear strange. This is due to the use of GraphViz. Good enough for my purposes, although
there are other options that are being explored (http://plantuml.sourceforge.net/qa/?qa=4842/graphviz-is-not-good-enough).

### Dependencies ###

- GraphViz (as required by PlantUML).
- Java 8 (easy enough to backport to earlier versions).

NOTE: The doclet tool was completely changed in Java 9; uml-java-doclet will not work with any JDK later than 8.

### Building ###

Build using Maven:

    mvn clean install

To see an example of the updated Javadocs, use the Javadoc target:

    mvn javadoc:javadoc

Javadoc output is in target/site/apidocs.

### Installing ###

To generate UML diagrams for your own project, add the following to your pom.xml. You will need to build
the project from scratch in your local environment; the distribution has not yet been uploaded to
Maven Central:

    <build>
        <plugins>
            <!-- Generate UML diagrams in javadoc using doclet: mvn javadoc:javadoc -->
            <plugin>                                                                         
                <groupId>org.apache.maven.plugins</groupId>                                  
                <artifactId>maven-javadoc-plugin</artifactId>                                
                <version>3.2.0</version>
                <configuration>
                    <doclet>info.leadinglight.umljavadoclet.UmlJavaDoclet</doclet>
                    <docletArtifact>
                        <groupId>info.leadinglight</groupId>
                        <artifactId>uml-java-doclet</artifactId>
                        <version>1.0-SNAPSHOT</version>                                               
                    </docletArtifact>
                    <!--
                        Standard doclet option for specifying different location for javadocs:
                        -reportOutputDirectory: parent folder
                        -destDir: name of the directory in the folder.
                        Refer to: https://maven.apache.org/plugins-archives/maven-javadoc-plugin-3.0.1/examples/output-configuration.html
                    -->
                    <reportOutputDirectory>/path/to/parent/folder</reportOutputDirectory>
                    <destDir>folder</destDir>
                    <!--
                        Specify options that are specific to uml-java-doclet as a single line, in format of
                        -optionname value -optionname2 value...

                        Options include:
                        -linetype polyline / spline / ortho (default)
                        -dependencies public (default) / protected / package / private
                        -package-orientation top-to-bottom (default) / left-to-right
                        -output-model true / false (default)
                    -->
                    <additionalparam></additionalparam>
                    <useStandardDocletOptions>true</useStandardDocletOptions>
                </configuration>
            </plugin>
        </plugins>
    </build>

# Acknowlegments #

Many thanks to the folks at PlantUML (https://github.com/plantuml/plantuml) for their
fantastic support.

# License #

Copyright 2016 Gerald Boersma

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

