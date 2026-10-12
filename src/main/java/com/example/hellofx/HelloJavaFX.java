package com.example.hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    // Stores all customers added by the user
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // Customer name input
        Label nameLabel = new Label("Customer Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");

        // Province selection
        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceBox.setPromptText("Select Province");

        // Buttons
        Button addButton = new Button("Add Customer");
        Button deleteButton = new Button("Delete Customer");

        // Customer table
        TableView<Customer> customerTable = new TableView<>();

        // Name column
        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Customer Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        // Province column
        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        // Make columns fill the available table width
        customerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        customerTable.getColumns().addAll(
                nameColumn, provinceColumn
        );

        // Connect the table to the observable customer list
        customerTable.setItems(customers);

        // ADD CUSTOMER ACTION
        addButton.setOnAction(e -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            // Validate user input
            if (name.isEmpty() || province == null) {

                Alert warning = new Alert(
                        Alert.AlertType.WARNING
                );

                warning.setTitle("Invalid Input");
                warning.setHeaderText("Missing Information");
                warning.setContentText(
                        "Please enter a customer name "
                                + "and select a province."
                );

                warning.showAndWait();
                return;
            }

            // Create and store the new customer
            Customer customer = new Customer(name, province);
            customers.add(customer);

            // Clear the input controls
            nameField.clear();
            provinceBox.setValue(null);

            // Return focus to the name field
            nameField.requestFocus();
        });

        // DELETE CUSTOMER ACTION
        deleteButton.setOnAction(e -> {

            Customer selectedCustomer =
                    customerTable.getSelectionModel()
                            .getSelectedItem();

            // Ensure a customer is selected
            if (selectedCustomer == null) {

                Alert warning = new Alert(
                        Alert.AlertType.WARNING
                );

                warning.setTitle("No Selection");
                warning.setHeaderText("No Customer Selected");
                warning.setContentText(
                        "Please select a customer from the table."
                );

                warning.showAndWait();
                return;
            }

            // Ask for confirmation before deleting
            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Delete Customer");
            confirmation.setHeaderText(
                    "Delete selected customer?"
            );

            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName() + "?"
            );

            confirmation.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {
                    customers.remove(selectedCustomer);
                }
            });
        });

        // Pressing Enter in the name field adds the customer
        nameField.setOnAction(e -> addButton.fire());

        // Arrange controls vertically
        VBox layout = new VBox(15);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        layout.getChildren().addAll(
                nameLabel,
                nameField,
                provinceBox,
                addButton,
                deleteButton,
                customerTable
        );

        // Application window
        Scene scene = new Scene(layout, 600, 500);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        nameField.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}