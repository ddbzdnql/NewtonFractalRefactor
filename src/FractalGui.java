import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FractalGui extends Application {

    private static final double WIDTH = 550;
    private static final double TOP_HEIGHT = 100;
    private static final double CENTER_HEIGHT = 550;
    private static final double BOTTOM_HEIGHT = 50;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        FlowPane topObject = new FlowPane();
        topObject.setOrientation(Orientation.HORIZONTAL);
        topObject.setPrefWidth(WIDTH);
        topObject.setPrefHeight(TOP_HEIGHT);

        root.setTop(topObject);
        root.setAlignment(root.getTop(), Pos.CENTER);

        StackPane centerObject = new StackPane();
        centerObject.setPrefWidth(WIDTH);
        centerObject.setPrefHeight(CENTER_HEIGHT);

        root.setCenter(centerObject);
        root.setAlignment(root.getCenter(), Pos.CENTER);

        FlowPane bottomObject = new FlowPane();
        bottomObject.setPrefHeight(BOTTOM_HEIGHT);
        bottomObject.setPrefWidth(WIDTH);

        root.setBottom(bottomObject);
        root.setAlignment(root.getBottom(), Pos.CENTER);

        primaryStage.setScene(new Scene(root, 600,800));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
