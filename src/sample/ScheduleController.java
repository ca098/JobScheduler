package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import sample.Algorithms.FCFS;
import sample.Algorithms.Resource;
import sample.Algorithms.Task;
import sample.Algorithms.TaskReader;
import sample.DataGenerator.Generator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ScheduleController {

    @FXML
    private ComboBox<Integer> jobCombo;

    @FXML
    private ComboBox<Integer> pMinCombo;

    @FXML
    private ComboBox<Integer> pMaxCombo;

    @FXML
    private ComboBox<String> fileChoiceCombo;

    @FXML
    private Button submitButton;

    @FXML
    private Label timeLabel;

    @FXML
    private Label timeTakenLbl;

    @FXML
    private Label energyLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Rectangle timeRectangle;

    @FXML
    private Rectangle energyRectangle;

    @FXML
    private TextArea filesTextArea;

    @FXML
    private Button deleteButton;

    public static int totalTime;


    public void initialize() {

        ArrayList<Integer> jobNumber = new ArrayList<>();
        for (int j = 5000; j <= 50000; j = j + 5000) {
            jobNumber.add(j);
        }

        ArrayList<Integer> pMinValue = new ArrayList<>();
        ArrayList<Integer> pMaxValue = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            pMinValue.add(i * 10);
            pMaxValue.add(i * 10);
        }

        pMaxValue.add(100);

        ArrayList<String> fileChoices = new ArrayList<>();
        fileChoices.add(".txt ");
        fileChoices.add(".csv ");

        ObservableList<Integer> pMinVisValue = FXCollections.observableArrayList(pMinValue);
        ObservableList<Integer> pMaxVisValue = FXCollections.observableArrayList(pMaxValue);
        ObservableList<Integer> jobAmount = FXCollections.observableArrayList(jobNumber);
        ObservableList<String> fileChoicesVisible = FXCollections.observableArrayList(fileChoices);

        pMinCombo.setItems(pMinVisValue);
        pMaxCombo.setItems(pMaxVisValue);


        jobCombo.setItems(jobAmount);
        fileChoiceCombo.setItems(fileChoicesVisible);

        setClock(timeLabel);
    }

    public void chosenJobs() {
        Integer selectedNumber = jobCombo.getValue();
        String fileType = fileChoiceCombo.getValue();

        progressBar.setVisible(true);
        progressIndicator.setVisible(true);
        timeRectangle.setVisible(true);

        if (selectedNumber == null) {
            selectedNumber = 0;
        }
        Generator.main(selectedNumber);


        String fPath = String.format("src/sample/Output/%dJobs.txt", selectedNumber);

        try {

            ArrayList<Task> tasks = TaskReader.readTasksFromFile(fPath);


//            ArrayList<Task> tasks = TaskReader.readTasksFromFile("src/sample/Output/4Jobs.txt");


            int pMin = pMinCombo.getValue();
            int pMax = pMaxCombo.getValue();

            ArrayList<Resource> fcfsAlgorithm = FCFS.Algorithm(tasks, progressBar, progressIndicator,
                    timeTakenLbl, fileType, pMin, pMax);

        } catch (Exception e) {
            System.out.println("File not found: " + e.toString());
        }

        final File folder = new File("src/sample/Output");
        listFiles(folder);

        deleteButton.setDisable(false);

        long overallAmount = FCFSWattage(FCFS.wattageSchedule);
        energyRectangle.setVisible(true);

        double kWhAmount = (double) overallAmount / 3600000;

        double averageEnergy = (double) overallAmount / totalTime;

        energyLabel.setText(String.format("        Average Energy\n Consumption: %.2f W\n\n" +
                "    Total Consumption:\n            %.2f (kWh)", averageEnergy, kWhAmount));

        FCFS.wattageSchedule.clear();
    }


    public long FCFSWattage(ArrayList<String> wattageSchedule) {
        long amount = 0;
        for (int i = 0; i < wattageSchedule.size() - 1; i++) {

            String[] parts = wattageSchedule.get(i).split("=");
            int power = Integer.parseInt(parts[0]);
            int currentTime = Integer.parseInt(parts[1]);

            String[] nextParts = wattageSchedule.get(i + 1).split("=");
            int nextTime = Integer.parseInt(nextParts[1]);

            amount += power * (nextTime - currentTime);

        }
        return amount;
    }

    public void gitReadMe() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/ca098/JobScheduler").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void closeProgram() {
        Platform.exit();
        System.exit(0);
    }


    public void activateButton() {
        if (pMinCombo.getValue() != null)
            pMaxCombo.setValue(pMinCombo.getValue() + 10);
        if (jobCombo.getValue() != null && fileChoiceCombo.getValue() != null
                && pMinCombo.getValue() != null) {
            submitButton.setDisable(false);
        }
    }

    public void newTask() {
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
        resetUI();
    }

    private void resetUI() {
        progressIndicator.setVisible(false);
        progressBar.setVisible(false);
        timeTakenLbl.setText("");
        timeRectangle.setVisible(false);
        energyLabel.setText("");
        energyRectangle.setVisible(false);
    }


    public void deleteFiles() {
        final File folder = new File("src/sample/Output");
        String[] entries = folder.list();
        for (String s : entries) {
            File currentFile = new File(folder.getPath(), s);
            boolean isDeleted = currentFile.delete();
        }

        filesTextArea.setText("");
        deleteButton.setDisable(true);
        resetUI();
    }


    public void listFiles(final File folder) {
        String files = "";
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFiles(fileEntry);
            } else {
                files += fileEntry.getName() + "\n";
                filesTextArea.setText(files);
            }
        }
    }


    public void setClock(Label timeLabel) {
        Thread timerThread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM HH:mm");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> timeLabel.setText(time));
            }
        });
        timerThread.start();
    }
}