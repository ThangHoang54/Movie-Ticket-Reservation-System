module org.example.finalexam {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;

    opens org.example.finalexam to javafx.fxml;
    opens org.example.finalexam.controller to javafx.fxml;
    opens org.example.finalexam.controller.pop_up to javafx.fxml;
    exports org.example.finalexam.model;
    exports org.example.finalexam;
    opens org.example.finalexam.model to java.base;
    opens org.example.finalexam.Image;
    exports org.example.finalexam.controller;
    exports org.example.finalexam.controller.admin;
    opens org.example.finalexam.controller.admin to javafx.fxml;
    exports org.example.finalexam.controller.user;
    opens org.example.finalexam.controller.user to javafx.fxml;
}