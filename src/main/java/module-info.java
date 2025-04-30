module com.cab302ai_teacher.ai_teacher {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;
    exports com.cab302ai_teacher;
    exports com.cab302ai_teacher.controller to javafx.fxml;

    opens com.cab302ai_teacher to javafx.fxml;
    opens com.cab302ai_teacher.controller to javafx.fxml;
}