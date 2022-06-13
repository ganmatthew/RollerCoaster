module com.csopesy.group1.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.csopesy.group1.app to javafx.fxml;
    exports com.csopesy.group1.app;
}