import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;

public class App extends Application{
    //text fields
    private TextField idField = new TextField();
    private TextField lastnameField = new TextField();
    private TextField firstnameField = new TextField();
    private TextField middleField = new TextField();
    private TextField addressField = new TextField();
    private TextField cityField = new TextField();
    private TextField stateField = new TextField();
    private TextField phoneField = new TextField();
    private TextField emailField = new TextField();
    //buttons
    private Button viewButton = new Button("View");
    private Button insertButton = new Button("Insert");
    private Button updateButton = new Button("Update");
    //connection to localhost
    private Connection connection;

    @Override
    public void start(Stage primaryStage){
        //gets the database
        initializeDB();
        //gui javafx stuff to set up
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        //adding the stuff to the field
        gridPane.add(new Label("ID"), 0, 0);
        gridPane.add(idField, 1, 0);
        gridPane.add(new Label("Last Name"), 0, 1);
        gridPane.add(lastnameField, 1, 1);
        gridPane.add(new Label("First Name"), 0, 2);
        gridPane.add(firstnameField, 1, 2);
        gridPane.add(new Label("MI"), 0, 3);
        gridPane.add(middleField, 1, 3);
        gridPane.add(new Label("Address"), 0, 4);
        gridPane.add(addressField, 1, 4);
        gridPane.add(new Label("City"), 0, 5);
        gridPane.add(cityField, 1, 5);
        gridPane.add(new Label("State"), 0, 6);
        gridPane.add(stateField, 1, 6);
        gridPane.add(new Label("Telephone"), 0, 7);
        gridPane.add(phoneField, 1, 7);
        gridPane.add(new Label("Email"), 0, 8);
        gridPane.add(emailField, 1, 8);
        //puts the buttons on the bottom
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(viewButton, insertButton, updateButton);
        hBox.setAlignment(Pos.CENTER);

        gridPane.add(hBox, 1, 9);
        //sets up events
        viewButton.setOnAction(e -> view());
        insertButton.setOnAction(e -> insert());
        updateButton.setOnAction(e -> update());
        //creates app
        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setTitle("staff databse");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDB(){
        try{
            //gets conenction using jar file I had to install
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testing", "root", "");
            System.out.println("Database connected");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void view(){
        //checks if the id is valid, if it is fills the boxes, if not displays not found
        String id = idField.getText();
        String sqlQuery = "SELECT * FROM Staff WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                lastnameField.setText(resultSet.getString("lastName"));
                firstnameField.setText(resultSet.getString("firstName"));
                middleField.setText(resultSet.getString("mi"));
                addressField.setText(resultSet.getString("address"));
                cityField.setText(resultSet.getString("city"));
                stateField.setText(resultSet.getString("state"));
                phoneField.setText(resultSet.getString("telephone"));
                emailField.setText(resultSet.getString("email"));
            }else{
                showAlert("No record found for ID " + id);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void insert() {
        //adds something to the database, then tells if successful
        String sqlQuery = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, idField.getText());
            preparedStatement.setString(2, lastnameField.getText());
            preparedStatement.setString(3, firstnameField.getText());
            preparedStatement.setString(4, middleField.getText());
            preparedStatement.setString(5, addressField.getText());
            preparedStatement.setString(6, cityField.getText());
            preparedStatement.setString(7, stateField.getText());
            preparedStatement.setString(8, phoneField.getText());
            preparedStatement.setString(9, emailField.getText());
            preparedStatement.executeUpdate();
            showAlert("Record inserted successfully");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void update(){
        //updates a databse entry using the id and new info
        String sqlQuery = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, lastnameField.getText());
            preparedStatement.setString(2, firstnameField.getText());
            preparedStatement.setString(3, middleField.getText());
            preparedStatement.setString(4, addressField.getText());
            preparedStatement.setString(5, cityField.getText());
            preparedStatement.setString(6, stateField.getText());
            preparedStatement.setString(7, phoneField.getText());
            preparedStatement.setString(8, emailField.getText());
            preparedStatement.setString(9, idField.getText());
            preparedStatement.executeUpdate();
            showAlert("Record updated successfully");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void showAlert(String message){
        //makes an alert pop up rather than just text on the screen
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("extra extra read all about it");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}