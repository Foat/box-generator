# box-generator #

This is simple scala swing + java3d application.
The project's objective is to generate some test samples for the following task:
we have a simple room `mainBox`, which is filled with boxes, we need to determine the number of boxes and their positions
using only one image.
_______________

tested on:

* [java 1.7.17](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [scala 2.10.1](http://www.scala-lang.org/)
* [java3d 1.5.2](http://java3d.java.net/)

## How to run ##
* install java3d
* run `gradlew libs build` in your command line if you don't have gradle, `gradle libs build` otherwise
* run `java -jar build/libs/box-generator.jar`

## Useful links ##
* [Java 3D installation](http://download.java.net/media/java3d/builds/release/1.5.2/README-download.html)
* http://www.java-tips.org/other-api-tips/java3d/
* http://www.interactivemesh.org/testspace/j3dmeetsscala.html

## License ##
MIT: http://foat.mit-license.org
