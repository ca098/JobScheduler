//package sample.Algorithms;
//
//import javafx.scene.control.Label;
//import javafx.scene.control.ProgressBar;
//import javafx.scene.control.ProgressIndicator;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FCFSTest {
//
//    @Test
//    void algorithm() {
//
//        ArrayList<Task> testData = testList();
//        ProgressBar dummyBar = null;
//        ProgressIndicator dummyIndicator = null;
//        Label dummyLabel = null;
//        String dummyFileType = ".txt";
//        assertEquals(FCFS.Algorithm(testData, dummyBar, dummyIndicator, dummyLabel, dummyFileType, 20, 30));
//    }
//
//
//    public ArrayList<Task> testList() {
//        String fPath = "src/Tests/4Jobs.txt";
//        return TaskReader.readTasksFromFile(fPath);
//    }
//
//    public ArrayList<Resource> desiredOutput() {
//
//        return new ArrayList<>();
//    }
//}