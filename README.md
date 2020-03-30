# JobScheduler

A JavaFX program which generates a set of jobs from a psuedorandom number generator (PRNG), and shows the schedule of
each job to a resource at each every given stage.

Output can be selected as either in a .txt file or a .csv file. With the files being saved in the directory of the program
under sample/Output.

#Build

There is currently an issue with IntelliJ (the IDE this was created on) and bundling JavaFX programs with Ant in any Java
version that isn't Java 8. This was created in Java 11.

Therefore, the current way to build this program is to either create a virtual environment and bundle all of the imports
into it then run sample/Main.java. Or create a new Project into an IDE of your choosing such as IntelliJ or Netbeans and
import this project. 
