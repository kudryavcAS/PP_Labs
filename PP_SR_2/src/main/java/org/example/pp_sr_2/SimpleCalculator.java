package org.example.pp_sr_2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimpleCalculator extends Application {

    private TextField num1Field;
    private TextField num2Field;
    private ComboBox<String> operationComboBox;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        Label num1Label = new Label("Первое число:");
        Label num2Label = new Label("Второе число:");
        Label operationLabel = new Label("Операция:");

        num1Field = new TextField();
        num2Field = new TextField();

        setupNumberValidation(num1Field);
        setupNumberValidation(num2Field);

        operationComboBox = new ComboBox<>();
        operationComboBox.getItems().addAll("+", "-", "*", "/", "^", "%", "log");
        operationComboBox.setValue("+");

        Button calculateButton = new Button("Вычислить");
        resultLabel = new Label("Результат: ");

        calculateButton.setOnAction(_ -> calculate());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(num1Label, 0, 0);
        grid.add(num1Field, 1, 0);
        grid.add(num2Label, 0, 1);
        grid.add(num2Field, 1, 1);
        grid.add(operationLabel, 0, 2);
        grid.add(operationComboBox, 1, 2);
        grid.add(calculateButton, 0, 3, 2, 1);
        grid.add(resultLabel, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Расширенный калькулятор");
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(250);

        primaryStage.show();
    }

    private void setupNumberValidation(TextField textField) {
        textField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    private void calculate() {
        try {
            if (num1Field.getText().isEmpty() || num2Field.getText().isEmpty()) {
                resultLabel.setText("Ошибка: введите оба числа!");
                return;
            }

            double num1 = Double.parseDouble(num1Field.getText());
            double num2 = Double.parseDouble(num2Field.getText());
            String operation = operationComboBox.getValue();

            double result = 0;

            switch (operation) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        resultLabel.setText("Ошибка: деление на ноль!");
                        return;
                    }
                    result = num1 / num2;
                    break;
                case "^":
                    result = Math.pow(num1, num2);
                    break;
                case "%":
                    if (num2 == 0) {
                        resultLabel.setText("Ошибка: деление на ноль!");
                        return;
                    }
                    result = num1 % num2;
                    break;
                case "log":
                    if (num1 <= 0) {
                        resultLabel.setText("Ошибка: логарифм от неположительного числа!");
                        return;
                    }
                    if (num2 <= 0 || num2 == 1) {
                        resultLabel.setText("Ошибка: основание логарифма должно быть > 0 и ≠ 1!");
                        return;
                    }
                    result = Math.log(num1) / Math.log(num2);
                    break;
            }

            resultLabel.setText(String.format("Результат: %.4f", result));

        } catch (NumberFormatException e) {
            resultLabel.setText("Ошибка: введите корректные числа!");
        } catch (Exception e) {
            resultLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}