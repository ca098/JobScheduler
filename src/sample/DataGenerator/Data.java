package sample.DataGenerator;

public class Data {

    private int taskNumber;
    private int arrivalTime;
    private int processingTime;
    private int utilisation;


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
