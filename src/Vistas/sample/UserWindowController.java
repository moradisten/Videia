package Vistas.sample;


import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;
import java.io.IOException;
import java.util.HashMap;


public class UserWindowController {

    public VBox listasBox;
    public VBox userListsVox;
    public BorderPane mainBorderPane;
    public Button comboBoxProfile;

    public MenuItem logoutItem;
    public MenuItem closeItem;
    public FlowPane flowPaneCenter;

    private Usuario usuarioActual;
    private Controlador controlador = Controlador.getInstanciaUnica();
    public Label userLabel;
    public MenuBar topMenuBar;
    public HBox topHBox;
    public HashMap<String, VideoList> listasUsuario;
    public HashMap<Boolean, VideoList> listasVisibles;

    private boolean editMode;

    public void inicializar(Controlador controlador) {

        this.editMode = false;
        this.controlador = controlador;                     // TODO: Cambio efectuado :S
        usuarioActual = controlador.getUsuarioActual();
        listasUsuario = new HashMap<String, VideoList>();
        listasVisibles = new HashMap<Boolean, VideoList>();

        boolean login = controlador.login(usuarioActual.getUsername(), usuarioActual.getPassword());

        if (login) {
            userLabel.setText(usuarioActual.getUsername());
        }


        logoutItem.setOnAction(event -> {
            logout();
            Window currentWindow =  mainBorderPane.getScene().getWindow();
            currentWindow.fireEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        closeItem.setOnAction(event -> {
            close();
        });

        if (!usuarioActual.getMyVideoLists().isEmpty()) {
            ListView<String> listView = new ListView<>();
            for (VideoList lista : usuarioActual.getMyVideoLists()) {
                listasUsuario.put(lista.getNombre(), lista);
                Button botonLista = new Button(lista.getNombre());
                botonLista.setPrefWidth(115);
                botonLista.setStyle("-fx-cursor: hand");
                botonLista.setOnMouseClicked(ActionEvent -> {
                    mostrarLista(botonLista.getText());
                });
                userListsVox.getChildren().add(botonLista);
            }
        }

    /*    String urls[] = {"https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk",
                "https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk"};


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ThumGridWindow.fxml"));
        GridPane gridPane = null;

        try {
            gridPane = loader.load();
        } catch (IOException e) {}

        ThumbGridController thumbGridController = loader.getController();
        thumbGridController.inicializarPanelCentral(this.mainBorderPane);

        if (gridPane != null) {
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setMaxWidth(600);
            scrollPane.setMinHeight(400);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            mainBorderPane.setCenter(scrollPane);
        }

        thumbGridController.insertImages(new LinkedList<String>(Arrays.asList(urls))); */

    }

    private void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private boolean isEditMode() {
        return editMode;
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }


    private void mostrarLista(String videoListNombre) {
        if (!usuarioActual.contieneVideoList(videoListNombre)) {
            Notificacion.listNotFoundError(videoListNombre);
        } else {

        }
    }

    public void anadirLista(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nuevaLista.fxml"));
        FlowPane nuevaLista = loader.load();
        nuevaListaController nuevaListaController = loader.getController();
        nuevaListaController.inicializar(controlador, this);
        mainBorderPane.setCenter(nuevaLista);
        mainBorderPane.requestLayout();
    }

    public void actualizarVistaListas(VideoList videoList) {
        Button botonLista = new Button(videoList.getNombre());
        botonLista.setStyle("-fx-cursor: hand");
        botonLista.setPrefWidth(115);
        botonLista.setOnMouseClicked(ActionEvent -> {
            mostrarLista(botonLista.getText());
        });
        userListsVox.getChildren().add(botonLista);
        mainBorderPane.requestLayout();
    }

    public void gotoProfileWindow(MouseEvent mouseEvent) throws IOException {



        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfileWindow.fxml"));
        VBox profilePane = loader.load();
        ProfileWindowController profileController = loader.getController();
        profileController.inicializar(controlador);


        Scene profileScene = new Scene(profilePane);
        Stage window = new Stage();
        window.setResizable(true);
        window.setScene(profileScene);
        window.show();

        currentStage.close();
        //profilePane.resize(currentScene.getWindow().getWidth(), currentWindow.getHeight());
        //currentWindow.fireEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSE_REQUEST));

        //((Node) mouseEvent.getSource()).getScene().getWindow().hide();
    }


    public void logout() {
        controlador.logout();

        try {
            volverALogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void volverALogin() throws IOException {


        BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
        FlowPane login =  FXMLLoader.load(getClass().getResource("login.fxml"));
        root.setCenter(login);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Registration Form FXML Application");
        Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void close() {
        System.exit(0);
    }

}
