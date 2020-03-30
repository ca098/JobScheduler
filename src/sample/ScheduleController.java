package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.Algorithms.FCFS;
import sample.Algorithms.Resource;
import sample.Algorithms.Task;
import sample.Algorithms.TaskReader;
import sample.DataGenerator.Generator;

import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleController {

    @FXML
    private ComboBox<Integer> jobCombo;

    @FXML
    private ComboBox<String> fileChoiceCombo;

    @FXML
    private Button submitButton;

    @FXML
    private Label timeLabel;

    @FXML
    private Label timeTakenLbl;

    @FXML
    private Label locationLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Rectangle timeRectangle;

    @FXML
    private Rectangle progressRectangle;


    public void initialize() {

        ArrayList<Integer> jobNumber = new ArrayList<>();
        for (int j = 5000; j<=50000; j = j+5000) {
            jobNumber.add(j);
        }

        ArrayList<String> fileChoices = new ArrayList<>();
        fileChoices.add(".txt ");
        fileChoices.add(".csv ");

        ObservableList<Integer> pMinMaxValues = FXCollections.observableArrayList(jobNumber);
        ObservableList<String> fileChoicesVisible = FXCollections.observableArrayList(fileChoices);
        jobCombo.setItems(pMinMaxValues);
        fileChoiceCombo.setItems(fileChoicesVisible);


        setClock(timeLabel);
    }

    public void chosenJobs() {
        Integer selectedNumber = jobCombo.getValue();
        String fileType = fileChoiceCombo.getValue();

        progressBar.setVisible(true);
        progressIndicator.setVisible(true);
        timeRectangle.setVisible(true);
        progressRectangle.setVisible(true);

        if (selectedNumber == null) {
            selectedNumber = 0;
        }
        Generator.main(selectedNumber);


        String fPath = String.format("src/sample/Output/%dJobs.txt", selectedNumber);

        String visibleFpath = String.format("src/sample/Output/%dJobs%s", selectedNumber, fileType);

        try {

            ArrayList<Task> tasks = TaskReader.readTasksFromFile(fPath);
            ArrayList<Resource> fcfsAlgorithm = FCFS.Algorithm(tasks, progressBar, progressIndicator, timeTakenLbl, fileType);

        } catch (Exception e) {
            System.out.println("File not found: " + e.toString());
        }

        locationLabel.setText("Schedule was saved at " + visibleFpath);
        locationLabel.setVisible(true);

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
        if (jobCombo.getValue() != null && fileChoiceCombo.getValue() != null){
            submitButton.setDisable(false);
        }

    }

    public void newTask() {
        progressBar.setProgress(0);
        progressIndicator.setVisible(false);
        progressBar.setVisible(false);
        progressIndicator.setProgress(0);
        locationLabel.setText("");
        timeTakenLbl.setText("");
        timeRectangle.setVisible(false);
        progressRectangle.setVisible(false);

    }


    public void setClock(Label timeLabel) {
        Thread timerThread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM HH:mm");
            while (true) {
                try {
                    Thread.sleep(1000); //1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> {
                    timeLabel.setText(time);
                });
            }
        });
        timerThread.start();//start the thread and its ok
    }
}
