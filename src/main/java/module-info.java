module com.cab302ai_teacher.ai_teacher {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cab302ai_teacher.ai_teacher to javafx.fxml;
    exports com.cab302ai_teacher.ai_teacher;
}