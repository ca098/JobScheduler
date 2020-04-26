package sample.Algorithms;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BF {

    public static ArrayList<String> wattageSchedule = new ArrayList<>();

    public static int totalNumberOfMachines = 0;

    public static void Algorithm(ArrayList<Task> data, ProgressBar bar,
                                                ProgressIndicator indicator, String fileType,
                                                int pMin, int pMax) throws IOException {
        int total;

        ArrayList<Resource> assignedResources = new ArrayList<>();
        String formattedFileType = fileType.replaceAll("\\s+", "");
        String fName = String.format("src/sample/Output/BF_%dJobs%s", data.size(), formattedFileType);

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
                }
                else {
                    int max = 0;
                    int index = 0;

                    for (int i = 0; i < assignedResources.size(); i++) {

                        int util = task.getUtilisation() + assignedResources.get(i).getCurrentUtilisation();
                        if (util <= 100 && util > max) {
                                max = util;
                                index = i;
                        }
                    }

                    if (max > 0) {
                        assignedResources.get(index).AddTaskToResourceBF(task);
                    }

                    else{
                        Resource newResource = new Resource(assignedResources.size());
                        newResource.canAddTaskToResource(task);
                        assignedResources.add(newResource);
                    }
                }

                total = Handler.populateFile(pw, taskArrivalTime, assignedResources, pMin, pMax);

                wattageSchedule.add(total + "=" + taskArrivalTime);

                // Logic for progress bar and indicator
                if (task.getTaskNo() > 1) {
                    double size = ((double) task.getTaskNo()) / data.size();

                    try {
                        double value = Handler.round(size, 2);
                        bar.setProgress(value);
                        indicator.setProgress(value);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }
        }
        totalNumberOfMachines = assignedResources.size();
    }
}
