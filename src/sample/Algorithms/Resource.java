package sample.Algorithms;


import java.util.ArrayList;
// Class to model a Resource. A resource being a single computational machine with a maximum utilisation of 100%.
// Pmin and Pmax refer to the power consumption.

public class Resource {


    private int pMin; //Power
    private int pMax;
    private int resourceID; // Name of the processor
    private int currentUtilisation;
    private ArrayList<Task> tasksOnResource;
    private int timeFree; // The time at which a resources last task finishes


    Resource(int id) {
        this.resourceID = id;
        this.currentUtilisation = 0;
        this.tasksOnResource = new ArrayList<>();
    }

    // Always add a task if the utilisation is below 100%
    // Updates the current utilisation after adding the task.
    public boolean canAddTaskToResource(Task task) {
        int util = task.getUtilisation();

        if (util + this.currentUtilisation <= 100) {
            this.tasksOnResource.add(task);
            updateUtilisation();
            return true;
        } else return false;
    }

    public void addTask(Task t) {
        tasksOnResource.add(t);
        updateUtilisation();
    }

    public int TimeAlone(Task incomingTask) {
        int alone = 0;
        int newTaskFinish = incomingTask.getArrivalTime() + incomingTask.getProcessingTime();
        for (Task existingTasks : tasksOnResource) {
            if (newTaskFinish < existingTasks.getArrivalTime() + existingTasks.getProcessingTime()) {
                alone = 0;
            } else {
                alone = existingTasks.getArrivalTime() + existingTasks.getProcessingTime() - newTaskFinish;
            }
        }
        return alone;
    }

    public void AddTaskToResourceBF(Task task) {
        this.tasksOnResource.add(task);
        updateUtilisation();
    }

//    public int TimeTogether(Task incomingTask) {
//        int together = 0;
//        int newTaskFinish = incomingTask.getArrivalTime() + incomingTask.getProcessingTime();
//        for (Task existingTasks : tasksOnResource) {
//            if (incomingTask.getArrivalTime() + incomingTask.getProcessingTime() <
//                    existingTasks.getArrivalTime() + existingTasks.getProcessingTime()) {
//                together = 0;
//            }
//            else {
//                together = newTaskFinish - existingTasks.getArrivalTime() + existingTasks.getProcessingTime();
//            }
//        }
//        return together;
//    }

    // Iterate through the tasks assigned to this resource and update the utilisation.
    // No check if it exceeds 100%, so this needs to be done when adding a new task to a resource.
    public void updateUtilisation() {
        int newUtil = 0;
        for (Task task : tasksOnResource) {
            newUtil += task.getUtilisation();
        }
        this.currentUtilisation = newUtil;
    }

    public int getResourceID() {
        return resourceID;
    }

    public int getCurrentUtilisation() {
        updateUtilisation();
        return currentUtilisation;
    }

    public boolean resourceFull() {
        return (getCurrentUtilisation() >= 100);
    }

    // Refresh resource every second, if the task runs its time it will be removed from the
    // active tasks on this resource.
    public void updateResource(int currentTime) {
        for (int i = 0; i < tasksOnResource.size(); i++) {
            if ((tasksOnResource.get(i).getArrivalTime() + tasksOnResource.get(i).getProcessingTime()) <= currentTime) {
                tasksOnResource.remove(i);
                updateUtilisation();
            }
        }
    }

    public boolean isTaskFinished(int currentTime) {
        for (Task task : tasksOnResource) {
            if (currentTime >= task.getArrivalTime() + task.getProcessingTime()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Task> getTasksOnResource() {
        return tasksOnResource;
    }

    public void printTasksOnResource() {
        for (Task t : tasksOnResource) {
            t.print();
        }
        System.out.println("--------------------");
    }

    public static int cost(int pMin, int pMax, int utilisation) {
        if (utilisation == 0) {
            return pMin;
        } else {
            return ((pMax - pMin) * utilisation / 100 + pMin) * 10;
        }
    }
}