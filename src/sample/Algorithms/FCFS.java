package sample.Algorithms;

import sample.ScheduleController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FCFS {

    public static ArrayList<String> wattageSchedule = new ArrayList<>();

    public static int totalNumberOfMachines = 0;

    public static void Algorithm(ArrayList<Task> data, String fileType,
                                                int pMin, int pMax) throws IOException {
        int total;

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

                total = Handler.populateFile(pw, taskArrivalTime, assignedResources, pMin, pMax);

                wattageSchedule.add(total + "=" + taskArrivalTime);

            }
            ScheduleController.totalTime = data.get(data.size()-1).getArrivalTime() + data.get(data.size()-1).getProcessingTime();
        }

        totalNumberOfMachines = assignedResources.size();
    }
}