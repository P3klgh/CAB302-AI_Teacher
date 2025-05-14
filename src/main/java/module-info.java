module com.cab302ai_teacher.ai_teacher {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;
    requires openai.java.client.okhttp;
    requires openai.java.core;
    requires java.desktop;
    exports com.cab302ai_teacher;
    exports com.cab302ai_teacher.controller to javafx.fxml;

    opens com.cab302ai_teacher to javafx.fxml;
    opens com.cab302ai_teacher.controller to javafx.fxml;
    exports com.cab302ai_teacher.model to javafx.fxml;
    opens com.cab302ai_teacher.model to javafx.fxml;
}
