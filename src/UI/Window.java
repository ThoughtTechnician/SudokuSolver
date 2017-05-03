package UI;/**
 * Created by connor on 5/2/17.
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class Window extends Application {

	private static final int BOARD_DIMENSION = 9;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();

		/**
		 * Create nontant grid
		 */
		GridPane nontantGrid = new GridPane();
		nontantGrid.setStyle("-fx-background-color: black; -fx-hgap: 5; -fx-vgap: 5; -fx-border-width: 5; -fx-border-color: black");
		GridPane[][] nontants = new GridPane[3][3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				GridPane current = new GridPane();
				nontants[x][y] = current;
				current.setStyle("-fx-background-color: white;");
				nontantGrid.add(current, y, x);
				GridPane.setHgrow(current, Priority.ALWAYS);
				GridPane.setVgrow(current, Priority.ALWAYS);
			}
		}
		/**
		 * Create Textfields("boxes") and put them in their respective nontants
		 */
		TextField[][] boxes = new TextField[BOARD_DIMENSION][BOARD_DIMENSION];
		for (int i = 0; i < BOARD_DIMENSION; i++) {
			for (int j = 0; j < BOARD_DIMENSION; j++) {
				boxes[i][j] = new TextField();
				boxes[i][j].setMaxWidth(Double.MAX_VALUE);
				boxes[i][j].setMaxHeight(Double.MAX_VALUE);
				boxes[i][j].setAlignment(Pos.CENTER);
				nontants[i / 3][j / 3].add(boxes[i][j], j % 3, i % 3);
				GridPane.setHgrow(boxes[i][j], Priority.ALWAYS);
				GridPane.setVgrow(boxes[i][j], Priority.ALWAYS);
			}
		}


		/**
		 * Add Instruction Label (Top)
		 */
		HBox topPane = new HBox();
		topPane.setAlignment(Pos.CENTER);
		Label commandLabel = new Label("Please enter the known values!");
		topPane.getChildren().add(commandLabel);
		HBox.setMargin(commandLabel, new Insets(10,10,10,10));
		root.setTop(topPane);

		/**
		 * Add nontant grid (Center)
		 */
		BorderPane.setMargin(nontantGrid, new Insets(10,10,10,10));
		root.setCenter(nontantGrid);
		nontantGrid.widthProperty().addListener((observable, oldValue, newValue) -> doSomething(nontantGrid, oldValue, newValue));
		nontantGrid.setMinHeight(250);
		nontantGrid.setMinWidth(250);

		/**
		 * Add Solve button (Bottom)
		 */
		HBox bottomPane = new HBox();
		bottomPane.setAlignment(Pos.CENTER_RIGHT);
		HBox.setMargin(nontantGrid, new Insets(10,10,10,10));
		Button solveButton = new Button("Solve Puzzle!");
		bottomPane.getChildren().add(solveButton);
		HBox.setMargin(solveButton, new Insets(10,10,10,10));
		root.setBottom(bottomPane);

		primaryStage.setTitle("Sudoku Solver");
		Scene scene = new Scene(root, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(300);
		primaryStage.setMinHeight(350);
		primaryStage.show();
	}

	private void doSomething(Pane node, Number oldValue, Number newValue) {
		System.out.println("oldValue: " + oldValue + ", newValue: " + newValue);
//    	node.setMaxHeight(newValue.longValue() /2);
		node.setMinHeight(newValue.longValue());
		node.setMaxHeight(newValue.longValue() + 1);

	}
}
