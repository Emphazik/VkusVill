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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.vladushik.vkusvillstoremanagement.HelloApplication;
import ru.vladushik.vkusvillstoremanagement.dao.ProductDao;
import ru.vladushik.vkusvillstoremanagement.database.Database;
import ru.vladushik.vkusvillstoremanagement.entity.Category;
import ru.vladushik.vkusvillstoremanagement.entity.User;

import javafx.collections.transformation.FilteredList;
import java.util.function.Predicate;

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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private ComboBox<Category> categoryBox;

    @FXML
    private TextField SearchTextField;

    private FilteredList<ProductDao> filteredList;

    @FXML
    private TextField countText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField weightText;

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
    private TableColumn<ProductDao, Integer> columnWeight;

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
    private StackPane imagePane;

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
        return productNameText.getText().isEmpty()
                || countText.getText().isEmpty()
                || priceText.getText().isEmpty()
                || weightText.getText().isEmpty()
                || categoryBox.getSelectionModel().isEmpty();
    }

    public void onAddClick() {
        if (checkEmptyFields()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Cообщение об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все пустые поля");
            alert.showAndWait();
        } else {
            String checkProdID = "SELECT * FROM products WHERE id = '" + productIdText.getText() + "'";
            connect = Database.getConnection();
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Cообщение об ошибке");
                    alert.setHeaderText(null);
                    alert.setContentText(productIdText.getText() + " уже занято");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO products "
                            + "(name, price, count, category_id, image, weight )"
                            + "VALUES(?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, productNameText.getText());
                    prepare.setInt(2, Integer.parseInt(priceText.getText()));
                    prepare.setInt(3, Integer.parseInt(countText.getText()));
                    prepare.setInt(4, categoryBox.getSelectionModel().getSelectedItem().getId());
                    prepare.setBytes(5, imageBytes);
                    prepare.setInt(6, Integer.parseInt(weightText.getText()));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Cообщение об ошибке");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно добавлено!");
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
            alert.setTitle("Cообщение об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все пустые поля");
            alert.showAndWait();
        } else {
            String updateData = "UPDATE products SET name = ?, count = ?, price = ?, category_id = ?, image = ?, weight = ? WHERE id = ?";
            connect = Database.getConnection();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cообщение об ошибке");
                alert.setHeaderText(null);
                alert.setContentText("Вы уверены, что хотите обновить ProductDao ID: " + productIdText.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, productNameText.getText());
                    prepare.setInt(2, Integer.parseInt(countText.getText()));
                    prepare.setInt(3, Integer.parseInt(priceText.getText()));
                    prepare.setInt(4, categoryBox.getSelectionModel().getSelectedItem().getId());
                    prepare.setBytes(5, imageBytes);
                    prepare.setInt(6, Integer.parseInt(weightText.getText()));
                    prepare.setInt(7, Integer.parseInt(productIdText.getText()));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Cообщение об ошибке");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно обновлено!");
                    alert.showAndWait();

                    onClearClick();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Cообщение об ошибке");
                    alert.setHeaderText(null);
                    alert.setContentText("Отменить.");
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
            alert.setTitle("Cообщение об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все пустые поля");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cообщение об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверены, что хотите удалить ProductDao ID: " + productIdText.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM products WHERE id = ?";
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.setInt(1, user.getId());
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Cообщение об ошибке");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно удален!");
                    alert.showAndWait();

                    onClearClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Cообщение об ошибке");
                alert.setHeaderText(null);
                alert.setContentText("Отменить");
                alert.showAndWait();
            }
        }
    }

    public void onClearClick() {
        // Обновить
        showData();
        // Очистить
        productIdText.setText("");
        productNameText.setText("");
        categoryBox.getSelectionModel().clearSelection();
        countText.setText("");
        priceText.setText("");
        productImage.setImage(null);
        weightText.setText("");
    }

    public void onImportClick() throws IOException {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Выбрать картинку", "*", "*png", "*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if (file != null) {
            imageBytes = Files.readAllBytes(file.toPath());
            image = new Image(file.toURI().toString(), 120, 127, false, true);
            productImage.setImage(image);
        }
    }

    public ObservableList<ProductDao> fetchData() {
        ObservableList<ProductDao> listData = FXCollections.observableArrayList();
        String sql = "SELECT p.id, p.name, p.price, p.count, c.name, p.image, p.weight FROM products p INNER JOIN categories c ON p.category_id = c.id";
        connect = Database.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ProductDao prodData;
            while (result.next()) {
                prodData = new ProductDao(
                        result.getInt(1),
                        result.getString(2),
                        result.getInt(3),
                        result.getInt(4),
                        result.getString(5),
                        result.getBytes(6),
                        result.getInt(7)
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
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList = new FilteredList<>(products, createPredicate(newValue));
            productsTable.setItems(filteredList);
        });

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnImage.setCellFactory(param -> {
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(50);
            imageview.setFitWidth(50);
            TableCell<ProductDao, Image> cell = new TableCell<>() {
                public void updateItem(Image item, boolean empty) {
                        imageview.setImage(item);
                }
            };
            cell.setGraphic(imageview);
            return cell;
        });
        columnImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
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
        imageBytes = dao.getBytes();
        productImage.setImage(image);
        weightText.setText(String.valueOf(dao.getWeight()));
        var categories = fetchCategories();
        categoryBox.setItems(categories);
        categoryBox.getSelectionModel().select(
                categories.stream().filter(c ->
                        Objects.equals(c.getName(), dao.getCategory())
                ).findFirst().get()
        );
    }

    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cообщение об ошибке");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверены, что хотите выйти из системы?");
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
       /* usernameLabel.setFont(Font.font("Book Antiqua", FontWeight.NORMAL, 18)); */

    }

    private ObservableList<Category> fetchCategories() {
        ObservableList<Category> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM categories";
        connect = Database.getConnection();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                listData.add(new Category(
                        result.getInt("id"),
                        result.getString("name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();

        categoryBox.setItems(fetchCategories());
        imagePane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                try {
                    imageBytes = Files.readAllBytes(db.getFiles().getFirst().toPath());
                    image = new Image(new ByteArrayInputStream(imageBytes), 120, 127, false, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productImage.setImage(image);
                event.setDropCompleted(true);
                event.consume();
            }
        });
        imagePane.setOnDragEntered(DragEvent::consume);
        imagePane.setOnDragExited(DragEvent::consume);
        imagePane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage() || db.hasFiles()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
    }

    private Predicate<ProductDao> createPredicate(String searchText) {
        return product -> {
            // Если поисковая строка пустая, отображаем все продукты
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            // Иначе фильтруем продукты по введенному тексту
            String lowerCaseSearchText = searchText.toLowerCase();
            return product.getName().toLowerCase().contains(lowerCaseSearchText)
                    || product.getCategory().toLowerCase().contains(lowerCaseSearchText);
        };
    }

}
