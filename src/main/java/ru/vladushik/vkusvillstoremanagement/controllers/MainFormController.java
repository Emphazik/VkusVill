package ru.vladushik.vkusvillstoremanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.vladushik.vkusvillstoremanagement.HelloApplication;
import ru.vladushik.vkusvillstoremanagement.dao.ProductDao;
import ru.vladushik.vkusvillstoremanagement.database.Database;
import ru.vladushik.vkusvillstoremanagement.entity.Category;
import ru.vladushik.vkusvillstoremanagement.entity.User;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private ComboBox<Category> categoryBox;

    @FXML
    private TextField countText;

    @FXML
    private TextField priceText;

    @FXML
    private ImageView productImage;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane productsForm;

    @FXML
    private TableView<ProductDao> productsTable;

    @FXML
    private TableColumn<ProductDao, Integer> columnId;

    @FXML
    private TableColumn<ProductDao, String> columnName;

    @FXML
    private TableColumn<ProductDao, String> columnCategory;

    @FXML
    private TableColumn<ProductDao, Integer> columnCount;

    @FXML
    private TableColumn<ProductDao, Integer> columnPrice;

    @FXML
    private TableColumn<ProductDao, Image> columnImage;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField productIdText;

    @FXML
    private TextField productNameText;

    @FXML
    private Label usernameLabel;

    @FXML
    private AnchorPane imagePane;

    public User user;
    private ProductDao dao;
    private Alert alert;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;
    private byte[] imageBytes;

    private ObservableList<ProductDao> products = FXCollections.observableArrayList();

    private boolean checkEmptyFields() {
        return productIdText.getText().isEmpty()
                || productNameText.getText().isEmpty()
                || countText.getText().isEmpty()
                || countText.getText().chars().allMatch(Character::isDigit)
                || priceText.getText().isEmpty()
                || priceText.getText().chars().allMatch(Character::isDigit)
                || categoryBox.getSelectionModel().isEmpty();
    }

    public void onAddClick() {
        if (checkEmptyFields()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkProdID = "SELECT * FROM products WHERE id = '" + productIdText.getText() + "'";
            connect = Database.getConnection();
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(productIdText.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO products "
                            + "(name, price, count, category_id, image)"
                            + "VALUES(?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, productIdText.getText());
                    prepare.setInt(2, Integer.parseInt(priceText.getText()));
                    prepare.setInt(3, Integer.parseInt(countText.getText()));
                    prepare.setInt(4, categoryBox.getSelectionModel().getSelectedItem().getId());
                    prepare.setBytes(5, new byte[]{});
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    onClearClick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onUpdateClick() {
        if (checkEmptyFields()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String updateData = "UPDATE products SET id = ?, name = ?, count = ?, price = ?, category_id = ?, image = ? WHERE id = ?";
            connect = Database.getConnection();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE ProductDao ID: " + productIdText.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    onClearClick();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDeleteClick() {
        if (dao.getId() == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE ProductDao ID: " + productIdText.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM products WHERE id = ?";
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.setInt(1, user.getId());
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    onClearClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }

    public void onClearClick() {
        // Update
        showData();
        // Clear
        productIdText.setText("");
        productNameText.setText("");
        categoryBox.getSelectionModel().clearSelection();
        countText.setText("");
        priceText.setText("");
        productImage.setImage(null);
    }

    public void onImportClick() throws IOException {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if (file != null) {
            imageBytes = Files.readAllBytes(file.toPath());
            image = new Image(file.toURI().toString(), 120, 127, false, true);
            productImage.setImage(image);
        }
    }

    public ObservableList<ProductDao> fetchData() {
        ObservableList<ProductDao> listData = FXCollections.observableArrayList();
        String sql = "SELECT p.id, p.name, p.price, p.count, c.name, p.image FROM products p INNER JOIN categories c ON p.category_id = c.id";
        connect = Database.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ProductDao prodData;
            while (result.next()) {
                prodData = new ProductDao(
                        result.getInt("p.id"),
                        result.getString("p.name"),
                        result.getInt("p.price"),
                        result.getInt("p.count"),
                        result.getString("c.name"),
                        new Image(new ByteArrayInputStream(result.getBytes("p.image")))
                );
                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void showData() {
        products = fetchData();

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        productsTable.setItems(products);
    }

    public void selectData() {
        dao = productsTable.getSelectionModel().getSelectedItem();
        int num = productsTable.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        productIdText.setText(String.valueOf(dao.getId()));
        productNameText.setText(dao.getName());
        countText.setText(String.valueOf(dao.getCount()));
        priceText.setText(String.valueOf(dao.getPrice()));
        image = dao.getImage();
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        image.getPixelReader().getPixels(0, 0, w, h, PixelFormat.getByteBgraInstance(), imageBytes, 0, w * 4);
        productImage.setImage(image);
    }

    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(HelloApplication.class.getResource("login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Авторизация");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsername() {
        String name = user.getUsername();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        usernameLabel.setText(name);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
        imagePane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        });
        imagePane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage()) {
                productImage.setImage(db.getImage());
                event.setDropCompleted(true);
            }
        });
    }

}
