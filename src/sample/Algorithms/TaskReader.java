package sample.Algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TaskReader {


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


}
