# JobScheduler

This is a JavaFX program which generates a set of jobs from a psuedorandom number generator (PRNG), and shows the schedule of
each job to a resource at every given stage and outputs the energy function of the given schedule via a given pMin and pMax value.

The output can be selected as either a __.txt__ file or a __.csv__ file. With the files being saved in the directory of the program under:
```
src/sample/Output/
```
There is a known bug of the overall energy consumption figures not producing the exact value, as the calculation is being drawn from the state of resources at a given arrival time and not accounting for when a resource becomes idle.

# Build

There is currently an issue with IntelliJ (the IDE this was created on) and bundling JavaFX programs with Ant in any Java
version that isn't Java 8. This was created in Java 11.

Issue Details:
[[JetBrains Ticket 200721]](https://youtrack.jetbrains.com/issue/IDEA-200721?_ga=2.224905754.1868922875.1585741664-751629145.1585393092)

Therefore, the current way to build this program is to either create a virtual environment and bundle all of the imports
into it and then run:
```
src/sample/Main.java
```
Or, create a new Project into an IDE of your choosing such as IntelliJ or Netbeans and
import this project.


## View

Below is a screenshot of the running program, along with the output of schedule saved as a __.txt__ output.

It is worth noting that some of the files that are generated can be in excess of > 25MB each, in which case the delete
button was implemented so that the files can be removed once you're finished creating the schedule.

### Program Image
![alt text][programImage] 

### Scheduled Output as a .txt File
![alt text][txtImage]


[txtImage]: Documentation/txtImage.png ".txt format"

[programImage]: Documentation/programImage.png "Output after scheduling 5000 jobs"
