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
import javafx.scene.control.TextArea;

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
    private ComboBox<String> fileChoiceCombo;

    @FXML
    private Button submitButton;

    @FXML
    private Label timeLabel;

    @FXML
    private Label timeTakenLbl;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Rectangle timeRectangle;


    @FXML
    private TextArea filesTextArea;

    @FXML
    private Button deleteButton;




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

        final File folder = new File("src/sample/Output");
        listFiles(folder);



        deleteButton.setDisable(false);

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
        progressIndicator.setProgress(0);
        resetUI();
    }

    private void resetUI() {
        progressIndicator.setVisible(false);
        progressBar.setVisible(false);
        timeTakenLbl.setText("");
        timeRectangle.setVisible(false);
    }



    public void deleteFiles() {
        final File folder = new File("src/sample/Output");
        String[]entries = folder.list();
        for(String s: entries){
            File currentFile = new File(folder.getPath(),s);
            currentFile.delete();
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


//    public void listFiles() {
//        try (Stream<Path> paths = Files.walk(Paths.get("src/sample/Output"))) {
//            paths
//                    .filter(Files::isRegularFile)
//                    .forEach(filesTextArea::setText);
//        }
//        catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }


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
                Platform.runLater(() -> timeLabel.setText(time));
            }
        });
        timerThread.start();
    }
}
