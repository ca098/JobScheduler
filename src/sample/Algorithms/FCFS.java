package sample.Algorithms;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import sample.ScheduleController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

// Test upload after file deletion?

public class FCFS {

    public static ArrayList<String> wattageSchedule = new ArrayList<>();

    public static ArrayList<Resource> Algorithm(ArrayList<Task> data, ProgressBar bar,
                                                ProgressIndicator indicator, Label timeTaken,
                                                String fileType, int pMin, int pMax) throws IOException {

        long startTime = System.nanoTime();

        int total = 0;

        ArrayList<Resource> assignedResources = new ArrayList<>();
        String formattedFileType = fileType.replaceAll("\\s+", "");
        String fName = String.format("src/sample/Output/FCFS_%dJobs%s", data.size(), formattedFileType);

        File file = new File(fName);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {

            for (Task task : data) {

                int taskArrivalTime = task.getArrivalTime();

                for (Resource resource : assignedResources) {
                    resource.updateResource(taskArrivalTime);

                }
                // If task is the 0th one
                if (task.getTaskNo() == 0) {
                    Resource r = new Resource(0);
                    r.addTask(task);
                    assignedResources.add(r);
                } else {
                    // Keep looking at the assigned resources until the task is added
                    boolean added = false;
                    while (!added) {
                        for (int i = 0; i < assignedResources.size() && !added; i++) {
                            // Check if we can add task to existing resource

                            if (assignedResources.get(i).canAddTaskToResource(task)) {
                                added = true;
                            }
                        }

                        // Create new resource if none exist with enough space
                        if (!added) {
                            Resource newResource = new Resource(assignedResources.size());
                            newResource.canAddTaskToResource(task);
                            assignedResources.add(newResource);
                            added = true;
                        }
                    }
                }

//                System.out.println("\n******** Resource state at Arrival Time: " + taskArrivalTime + " ********\n");
//                for (Resource r : assignedResources) {
//                    System.out.println("Resource: " + r.getResourceID() + ", Utilisation: " + r.getCurrentUtilisation() + "%");
//                r.printTasksOnResource();
//                }


                total = populateFile(pw, taskArrivalTime, assignedResources, pMin, pMax);

                wattageSchedule.add(total + "=" + taskArrivalTime);

                if (task.getTaskNo() > 1) {
                    double size = ((double) task.getTaskNo()) / data.size();

                    try {
                        double value = round(size, 2);
                        bar.setProgress(value);
                        indicator.setProgress(value);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }
            ScheduleController.totalTime = data.get(data.size()-1).getArrivalTime() + data.get(data.size()-1).getProcessingTime();;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        timeTaken.setText(String.format("Scheduler completed in: %dms ", duration));

        return assignedResources;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int cost(int pMin, int pMax, int utilisation) {
        if (utilisation == 0) {
            return pMin;
        } else {
            return ((pMax - pMin) * utilisation / 100 + pMin) * 10;
        }
    }

    public static int populateFile(PrintWriter pw, int taskArrivalTime, ArrayList<Resource> assignedResources,
                                   int pMin, int pMax) {

        pw.println("\n******** Resource state at Arrival Time: " + taskArrivalTime + " ********\n");
        int total = 0;
        for (Resource r : assignedResources) {
            int currentPower = 0;
            currentPower += cost(pMin, pMax, r.getCurrentUtilisation());
            total += cost(pMin, pMax, r.getCurrentUtilisation());
            pw.println("Resource: " + r.getResourceID() + ", Utilisation: " + r.getCurrentUtilisation() + "%" + ", Watts: " + currentPower);
            String formattedString = r.getTasksOnResource().toString()
                    .replace(",", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();
            pw.println(formattedString);
            pw.println("--------------------");
        }
        return total;
    }
}
