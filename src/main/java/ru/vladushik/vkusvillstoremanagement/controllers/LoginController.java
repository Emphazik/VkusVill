package ru.vladushik.vkusvillstoremanagement.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.vladushik.vkusvillstoremanagement.HelloApplication;
import ru.vladushik.vkusvillstoremanagement.database.Database;
import ru.vladushik.vkusvillstoremanagement.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private TextField si_username;

    @FXML
    private PasswordField passwordSignIn;

    @FXML
    private TextField loginSignIn;

    @FXML
    private TextField loginSignUp;

    @FXML
    private Button si_loginBtn;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private PasswordField passwordSignUp;

    @FXML
    private TextField su_answer;

    @FXML
    private Button su_signupBtn;

    @FXML
    private TextField fp_username;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_back;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField npNewPassword;

    @FXML
    private PasswordField npConfirmPassword;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private Button np_back;

    @FXML
    private AnchorPane side_form;

    @FXML
    private Button side_CreateBtn;

    @FXML
    private Button side_alreadyHave;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    private User user;

    public void loginBtn() {
        if (loginSignIn.getText().isEmpty() || passwordSignIn.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        } else {
            String selectData = "SELECT * FROM users WHERE login = ? and password = ?";
            connect = Database.getConnection();
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, loginSignIn.getText());
                prepare.setString(2, passwordSignIn.getText());
                result = prepare.executeQuery();
                if (result.next()) {
                    user = new User();
                    user.setId(result.getInt("id"));
                    user.setLogin(result.getString("login"));
                    user.setPassword(result.getString("password"));
                    user.setUsername(result.getString("username"));
                    user.setSecret(result.getString("secret"));

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();
                    // LINK YOUR MAIN FORM
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
                    Parent root = loader.load();
                    MainFormController c = loader.getController();
                    c.user = user;
                    c.displayUsername();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle("");
                    stage.setMinWidth(1100);
                    stage.setMinHeight(600);
                    stage.setScene(scene);
                    stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("VkusLogotype.jpg")));
                    stage.show();

                    si_loginBtn.getScene().getWindow().hide();
                } else { // IF NOT, THEN THE ERROR MESSAGE WILL APPEAR
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Login/Password");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void regBtn() {
        if (su_username.getText().isEmpty() || passwordSignUp.getText().isEmpty()
                || su_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String regData = "INSERT INTO users (username, login, password, secret) VALUES (?,?,?,?)";
            connect = Database.getConnection();
            try {
                String checkUsername = "SELECT username FROM users WHERE username = '" + su_username.getText() + "'";
                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " is already taken");
                    alert.showAndWait();
                } else if (passwordSignUp.getText().length() < 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password, atleast 8 characters are needed");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, su_username.getText());
                    prepare.setString(2, loginSignUp.getText());
                    prepare.setString(3, passwordSignUp.getText());
                    prepare.setString(4, su_answer.getText());
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered Account!");
                    alert.showAndWait();

                    su_username.setText("");
                    passwordSignUp.setText("");
                    su_answer.setText("");

                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(side_form);
                    slider.setToX(0);
                    slider.setDuration(Duration.seconds(.5));
                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        side_CreateBtn.setVisible(true);
                    });
                    slider.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);
    }

    public void proceedBtn() {
        if (fp_username.getText().isEmpty() || fp_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String selectData = "SELECT * FROM users WHERE username = ? AND secret = ?";
            connect = Database.getConnection();
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, fp_answer.getText());
                result = prepare.executeQuery();
                if (result.next()) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Information");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changePassBtn() {
        if (npNewPassword.getText().isEmpty() || npConfirmPassword.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            if (npNewPassword.getText().equals(npConfirmPassword.getText())) {
                String getDate = "SELECT date FROM users WHERE username = '"
                        + fp_username.getText() + "'";
                connect = Database.getConnection();
                try {
                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();
                    String updatePass = "UPDATE users SET password = '"
                            + npNewPassword.getText() + "', secret = '"
                            + fp_answer.getText() + "' WHERE username = '"
                            + fp_username.getText() + "'";
                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    alert.showAndWait();

                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);
                    // TO CLEAR FIELDS
                    npConfirmPassword.setText("");
                    npNewPassword.setText("");
                    fp_answer.setText("");
                    fp_username.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Not match");
                alert.showAndWait();
            }
        }
    }

    public void backToLoginForm() {
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }

    public void backToQuestionForm() {
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        if (event.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);
                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
            slider.play();
        }
    }

}
