# JobScheduler

This is a JavaFX program which generates a set of jobs from a psuedorandom number generator (PRNG), and shows the schedule of
each job to a resource at every given stage and outputs the energy function of the given schedule via a given pMin and pMax value.

This value is the minimum and maximum power constraint given to a resource. For example, a pMin value of 20 and a pMax value of 30 corresponds to a mimumim power constraint of 200 Watts and 300 Watts respectively. 

The output can be selected as either a __.txt__ file or a __.csv__ file. With the files being saved in the directory of the program under:
```
src/sample/Output/
```

## Energy Calculation
There is a known bug of the overall energy consumption figures not producing the exact value. This is because the calculation is being drawn from the state of resources at a given arrival time, and not accounting for when a resource becomes idle. Which at times can be in the window between the arrival times of different tasks.

### Total Energy
The total energy consumption may seem like it is too high. However, this is a summation of all the different resource utilisation levels for X amount of time. It can be compared to having a 60 Watt light bulb running for 1 Hr. There is a calculation of either 0.06 kWh or 60 Watt * 3600 (seconds in an hour) = 212,000 Watts. The latter calculation is what you're seeing from this example.

### Average Energy
The average energy value you're seeing is the average power consumption across all active resources for the duration of the schedule, in correlation to the pMin and pMax value selected.

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

Below is a screenshot of the running program, along with the output of the schedule saved in a __.txt__ output.

It is worth noting that some of the files that are generated can be in excess of > 25MB each, in which case the delete
button was implemented so that the files can be removed once you're finished creating the schedule.

### Program Image
![alt text][programImage] 

### Scheduled Output as a .txt File
![alt text][txtImage]


[txtImage]: Documentation/txtImage.png ".txt format"

[programImage]: Documentation/programImage.png "Output after scheduling 5000 jobs"
