package fabry;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class GraphTab {
    public Tab tab;
    public LineChart chart;
    private VBox vbox;
    private AnchorPane anchorPane;
    private Label label_x;
    private Label label_y;
    private Label label_vs;
    private Label label_error;
    private ChoiceBox<String> choiceBox_x;
    private ChoiceBox<String> choiceBox_y;
    private Spinner<Double> spinner_x;
    private Spinner<Double> spinner_y;
    private Dictionary<String, Variable> choices;
    private SpinnerValueFactory<Double> valueFactory;


    public Variable x_var;
    public Variable y_var;

    private NumberAxis axis_x;
    private NumberAxis axis_y;

    public void setChoices(Dictionary<String, Variable> choices) {
        if (choiceBox_x.getItems().size() > 0) {
            String k;

            int idc=0;
            this.choices = choices;
            for (ChoiceBox<String> box = choiceBox_x; idc<2; box=choiceBox_y) {
                System.out.println("aaa");
                idc++;
                k = box.getValue();
                Enumeration<String> e = choices.keys();
                boolean found=false;
                while (e.hasMoreElements()) {
                    if (Objects.equals(k, e.nextElement())) {
                        found=true;
                        break;
                    }
                }
                System.out.println(found);
                box.getItems().clear();

                if (found) {
                    box.getItems().add(k);
                    e = choices.keys();
                    String i;
                    while (e.hasMoreElements()) {
                        i=e.nextElement();
                        if (!i.equals(k)) {
                            box.getItems().add(i);
                        }
                    }
                } else {
                    e = choices.keys();
                    while (e.hasMoreElements()) {
                        box.getItems().add(e.nextElement());
                    }

                }
            }

        } else {
            choiceBox_x.getItems().clear();
            choiceBox_y.getItems().clear();
            Enumeration<String> e = choices.keys();
            String choice;
            while (e.hasMoreElements()) {
                choice = e.nextElement();
                choiceBox_x.getItems().add(choice);
                choiceBox_y.getItems().add(choice);
            }
        }

    }




    GraphTab (String tab_name, Dictionary<String, Variable> choices) {
        this.choices = choices;
        tab = new Tab(tab_name);

        vbox = new VBox();
        anchorPane = new AnchorPane();
        label_x = new Label("X-AXIS");
        label_y = new Label("Y-AXIS");
        label_vs = new Label("VS");
        label_error = new Label("");
        choiceBox_x = new ChoiceBox<String>();
        choiceBox_y = new ChoiceBox<String>();

        setChoices(choices);

        valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10.0, 1.0, 0.1);
        spinner_x = new Spinner<Double>();
        spinner_y = new Spinner<Double>();
        spinner_x.setPrefHeight(25.0);
        spinner_y.setPrefHeight(25.0);
        spinner_x.setPrefWidth(60.0);
        spinner_y.setPrefWidth(60.0);

        spinner_x.setValueFactory(valueFactory);
        spinner_y.setValueFactory(valueFactory);

        axis_x = new NumberAxis(0, 100, 10);
        axis_y = new NumberAxis(0, 100, 10);
        chart = new LineChart(axis_x, axis_y);
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.getXAxis().setAutoRanging(true);
        chart.getYAxis().setAutoRanging(true);

        tab.setContent(vbox);
        vbox.getChildren().addAll(anchorPane, chart, label_error);

        AnchorPane.setLeftAnchor(label_x, 582.0);
        AnchorPane.setTopAnchor(label_x, 12.0);

        AnchorPane.setRightAnchor(label_y, 582.0);
        AnchorPane.setTopAnchor(label_y, 12.0);

        AnchorPane.setLeftAnchor(choiceBox_x, 238.0);
        AnchorPane.setTopAnchor(choiceBox_x, 8.0);

        AnchorPane.setRightAnchor(choiceBox_y, 238.0);
        AnchorPane.setTopAnchor(choiceBox_y, 8.0);

        AnchorPane.setLeftAnchor(label_vs, 582.0);
        AnchorPane.setRightAnchor(label_vs, 582.0);
        label_vs.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(label_vs, 12.0);

        AnchorPane.setLeftAnchor(spinner_x, 144.0);
        AnchorPane.setTopAnchor(spinner_x, 8.0);

        AnchorPane.setRightAnchor(spinner_y, 144.0);
        AnchorPane.setTopAnchor(spinner_y, 8.0);


        choiceBox_x.valueProperty().addListener((observable, old_value, new_value) -> {
            if (!new_value.equals(old_value)){
                x_var = this.choices.get(new_value);
                graph();

            }
        });
        choiceBox_y.valueProperty().addListener((observable, old_value, new_value) -> {
            if (!new_value.equals(old_value)){
                y_var = this.choices.get(new_value);
                graph();

            }
        });

        spinner_x.valueProperty().addListener((observable, old_value, new_value) -> {
            graph();
        });

        spinner_y.valueProperty().addListener((observable, old_value, new_value) -> {
            graph();
        });


        anchorPane.getChildren().addAll(label_x, label_y, label_vs, choiceBox_x, choiceBox_y, spinner_x, spinner_y);


    }

    public void graph() {
        // Assuming Y and X are both not selected.
        double scale_x = spinner_x.getValue();
        double scale_y = spinner_y.getValue();

        XYChart.Series<Double, Double> series= new XYChart.Series<Double, Double>();
        try {
            for (double i = 0; i < 100/scale_x; i+=0.2/scale_x) {
                x_var.value = i *x_var.unit;
                y_var.calculateGraph(x_var);
//                System.out.println(x_var.value/x_var.unit);
//                System.out.println(y_var.value/y_var.unit);

                series.getData().add(new XYChart.Data<Double, Double>(x_var.value/x_var.unit, y_var.value/y_var.unit));
            }
        } catch (Exceptions.UnfulfilledDependenciesException err) {
            label_error.setText("Unfulfilled.");
        } catch (Exceptions.MathError err) {
            label_error.setText("MathError");
        }

        chart.getData().clear();
        chart.getData().add(series);


    }


}
