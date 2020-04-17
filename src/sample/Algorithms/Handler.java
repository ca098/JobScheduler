package sample.Algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Handler {


    public static ArrayList<Task> readTasksFromFile(String fPath) {

        ArrayList<Task> job = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(fPath))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] tokens = str.split(",");
                int task = Integer.parseInt(tokens[0]);
                int arrival = Integer.parseInt(tokens[1]);
                int proc = Integer.parseInt(tokens[2]);
                int util = Integer.parseInt(tokens[3]);
                Task t = new Task(task, arrival, proc, util);

                job.add(t);

            }
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

        return job;
    }


    public static int populateFile(PrintWriter pw, int taskArrivalTime, ArrayList<Resource> assignedResources,
                                   int pMin, int pMax) {

        pw.println("\n******** Resource state at Arrival Time: " + taskArrivalTime + " ********\n");
        int total = 0;
        for (Resource r : assignedResources) {
            int currentPower = 0;
            currentPower += Resource.cost(pMin, pMax, r.getCurrentUtilisation());
            total += Resource.cost(pMin, pMax, r.getCurrentUtilisation());
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
