package com.example.promptgpt;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HelloController {

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button submitButton;

    public void initialize() {
        submitButton.setOnAction(event -> {
            try {
                String input = inputTextArea.getText();
                String output = sendRequest(input);
                outputTextArea.setText(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String sendRequest(String input) throws IOException {
        URL url = new URL("https://api-inference.huggingface.co/models/merve/chatgpt-prompts-bart-long");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Authorization", "Bearer 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");
        httpConn.setRequestProperty("Content-Type", "application/json");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("{\"inputs\": \"" + input + "\"}");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        return response;
    }
}
