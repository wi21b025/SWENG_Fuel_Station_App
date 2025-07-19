module com.example.javafxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.apache.pdfbox;


    opens com.example.javafxapp to javafx.fxml;
    exports com.example.javafxapp;
}