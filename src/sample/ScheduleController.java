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
import sample.Algorithms.BF;
import sample.Algorithms.FCFS;
import sample.Algorithms.Task;
import sample.DataGenerator.Generator;

import java.awt.*;
import java.io.*;
import java.net.*;
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
    private Label FCFSEnergyLabel;

    @FXML
    private Label totalEnergylbl;

    @FXML
    private Label VMLbl_FCFS;

    @FXML
    private Label VMLbl_BF;

    @FXML
    private Label allocatedLbl;

    @FXML
    private Label averageEnergyLbl;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Rectangle timeRectangle;

    @FXML
    private Rectangle energyRectangle;

    @FXML
    private Rectangle VMRectangle;

    @FXML
    private TextArea filesTextArea;

    @FXML
    private Button deleteButton;

    public static int totalTime;
    public static ArrayList<Task> taskList;

    public void initialize() {

        ArrayList<Integer> jobNumber = new ArrayList<>();
        jobNumber.add(2500);
        for (int j = 5000; j <= 50000; j = j + 5000) {
            jobNumber.add(j);
        }

        ArrayList<Integer> pMinValue = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            pMinValue.add(i * 10);
        }

        ArrayList<String> fileChoices = new ArrayList<>();
        fileChoices.add(".txt ");
        fileChoices.add(".csv ");

        ObservableList<Integer> pMinVisValue = FXCollections.observableArrayList(pMinValue);
        ObservableList<Integer> jobAmount = FXCollections.observableArrayList(jobNumber);
        ObservableList<String> fileChoicesVisible = FXCollections.observableArrayList(fileChoices);

        pMinCombo.setItems(pMinVisValue);
        jobCombo.setItems(jobAmount);
        fileChoiceCombo.setItems(fileChoicesVisible);

        setClock(timeLabel);
    }


    public void fileLocation() {
        try {
            Desktop.getDesktop().open(new File("src/sample/Output"));
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }


    public void ActivateSchedule() {

        Integer selectedNumber = jobCombo.getValue();
        String fileType = fileChoiceCombo.getValue();

        progressBar.setVisible(true);
        progressIndicator.setVisible(true);
        timeRectangle.setVisible(true);

        if (selectedNumber == null) {
            selectedNumber = 0;
        }

        Generator.main(selectedNumber);

        int pMin = pMinCombo.getValue();
        int pMax = pMaxCombo.getValue();

        long startTime = System.nanoTime();

        try {

            FCFS.Algorithm(taskList, fileType, pMin, pMax);
            BF.Algorithm(taskList, progressBar, progressIndicator, fileType, pMin, pMax);

        } catch (Exception e) {
            System.out.println("Error Creating Schedules: " + e.toString());
        }

        final File folder = new File("src/sample/Output");
        listFiles(folder);

        deleteButton.setDisable(false);

        long FCFS_overallAmount = Wattage(FCFS.wattageSchedule);
        long BF_overallAmount = Wattage(BF.wattageSchedule);

        energyRectangle.setVisible(true);

        double FCFS_kWhAmount = (double) FCFS_overallAmount / 3600000;
        double BF_kWhAmount = (double) BF_overallAmount / 3600000;

        double FCFS_averageEnergy = (double) FCFS_overallAmount / totalTime;
        double BF_averageEnergy = (double) BF_overallAmount / totalTime;

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        timeTakenLbl.setText(String.format("Scheduler completed in: %dms ", duration));

        FCFSEnergyLabel.setText("         Average Energy \n          Consumption" +
                "\n"+
                "\n\n\n" +
                "      Total Consumption\n" +
                "\n\n\n");


        averageEnergyLbl.setText(String.format("FCFS  = %.2f W\nBF       = %.2f W", FCFS_averageEnergy, BF_averageEnergy));
        totalEnergylbl.setText(String.format("FCFS  = %.2f kWh\nBF       = %.2f kWh", FCFS_kWhAmount, BF_kWhAmount));


        VMRectangle.setVisible(true);

        allocatedLbl.setText("VMs Allocated");
        VMLbl_FCFS.setText("FCFS VMs: " + FCFS.totalNumberOfMachines);
        VMLbl_BF.setText("BF VMs: " + BF.totalNumberOfMachines);


        // Clear Data from Previous Computation
        FCFS.wattageSchedule.clear();
        BF.wattageSchedule.clear();
    }


    public long Wattage(ArrayList<String> wattageSchedule) {
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

    public void injectPMaxCombo() {
        ArrayList<Integer> intList = new ArrayList<>();
        int lowestValue = pMinCombo.getValue() + 10;
        for (int i = lowestValue / 10; i < 11; i++) {
            intList.add(i * 10);
        }
        ObservableList<Integer> pMaxOutput = FXCollections.observableArrayList(intList);
        pMaxCombo.setItems(pMaxOutput);
    }

    public void clearPMax() {
        pMaxCombo.setValue(null);
    }

    public void activateButton() {
        pMaxCombo.setDisable(false);
        submitButton.setDisable(jobCombo.getValue() == null || fileChoiceCombo.getValue() == null
                || pMinCombo.getValue() == null || pMaxCombo.getValue() == null);
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
        FCFSEnergyLabel.setText("");
        totalEnergylbl.setText("");
        averageEnergyLbl.setText("");
        VMLbl_FCFS.setText("");
        VMLbl_BF.setText("");
        allocatedLbl.setText("");
        VMRectangle.setVisible(false);

        energyRectangle.setVisible(false);
    }


    public void deleteFiles() {
        final File folder = new File("src/sample/Output");
        String[] entries = folder.list();
        if(entries != null) {
            for (String s : entries) {
                File currentFile = new File(folder.getPath(), s);
                boolean isDeleted = currentFile.delete();
            }
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