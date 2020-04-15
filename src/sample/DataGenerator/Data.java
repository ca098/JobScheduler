package sample.DataGenerator;

public class Data {

    private final int taskNumber;
    private final int arrivalTime;
    private final int processingTime;
    private final int utilisation;


    public Data(int taskNumber, int arrivalTime, int processingTime, int utilisation) {
        this.taskNumber = taskNumber;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.utilisation = utilisation;

    }


    public int getArrivalTime() {return arrivalTime;}


    @Override
    public String toString()
    {
        return String.format("%d,%d,%d,%d", taskNumber, arrivalTime, processingTime, utilisation);
    }
}