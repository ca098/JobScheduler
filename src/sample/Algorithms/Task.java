package sample.Algorithms;


// Class to model a single Task. Tasks get allocated to resources assuming the resource does not exceed 100% utilisation.

public class Task {

    private final int taskNo;
    private final int arrivalTime;
    private final int processingTime;
    private final int utilisation;
    private boolean finished;

    public Task(int taskNo, int arrivalTime, int processingTime, int utilisation) {
        this.taskNo = taskNo;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.utilisation = utilisation;
    }

    public int getFinishTime() {return arrivalTime + processingTime;}

    public int getTaskNo() {
        return taskNo;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getUtilisation() {
        return utilisation;
    }

    // Check if a task should be finished. Find some way to deallocate tasks when they reach their finish time.
    public boolean isTaskFinished(int currentTime) {
        return currentTime == (this.arrivalTime + this.processingTime);
    }

    public void print() {
        System.out.println("Task: " + this.taskNo);
        System.out.println("Arrival time    = " + this.arrivalTime);
        System.out.println("Processing time = " + this.processingTime);
        System.out.println("Utilisation     = " + this.utilisation);
        System.out.println(" ");
    }


    @Override
    public String toString() {
        return "\nTASK: " + this.taskNo + "\nArrival time    = " + this.arrivalTime
                + "\nProcessing time = " + this.processingTime + "\nUtilisation     = "
                + this.utilisation + "\n";
    }
}
