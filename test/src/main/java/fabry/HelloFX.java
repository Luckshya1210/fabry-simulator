package fabry;



import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Math.*;


public class HelloFX extends Application {


    @FXML public CheckBox distance_checkBox;
    @FXML public TextField distance_textField;
    @FXML public ChoiceBox<String> distance_choiceBox;
    @FXML public Slider distance_slider;



    @FXML public CheckBox n_checkBox;
    @FXML public TextField n_textField;
    @FXML public ChoiceBox<String> n_choiceBox;
    @FXML public Slider n_slider;



    @FXML public CheckBox theta_checkBox;
    @FXML public TextField theta_textField;
    @FXML public ChoiceBox<String> theta_choiceBox;
    @FXML public Slider theta_slider;



    @FXML public CheckBox wavelength_checkBox;
    @FXML public TextField wavelength_textField;
    @FXML public ChoiceBox<String> wavelength_choiceBox;
    @FXML public Slider wavelength_slider;



    @FXML public CheckBox I_incident_checkBox;
    @FXML public TextField I_incident_textField;
    @FXML public ChoiceBox<String> I_incident_choiceBox;
    @FXML public Slider I_incident_slider;



    @FXML public CheckBox I_transmitted_checkBox;
    @FXML public TextField I_transmitted_textField;
    @FXML public ChoiceBox<String> I_transmitted_choiceBox;
    @FXML public Slider I_transmitted_slider;



    @FXML public CheckBox R_checkBox;
    @FXML public TextField R_textField;
    @FXML public ChoiceBox<String> R_choiceBox;
    @FXML public Slider R_slider;



    @FXML public CheckBox B_checkBox;
    @FXML public TextField B_textField;
    @FXML public ChoiceBox<String> B_choiceBox;
    @FXML public Slider B_slider;



    @FXML public CheckBox F_checkBox;
    @FXML public TextField F_textField;
    @FXML public ChoiceBox<String> F_choiceBox;
    @FXML public Slider F_slider;


    @FXML public CheckBox T_checkBox;
    @FXML public TextField T_textField;
    @FXML public ChoiceBox<String> T_choiceBox;
    @FXML public Slider T_slider;


    @FXML public CheckBox del_checkBox;
    @FXML public TextField del_textField;
    @FXML public ChoiceBox<String> del_choiceBox;
    @FXML public Slider del_slider;


    @FXML public CheckBox delta_checkBox;
    @FXML public TextField delta_textField;
    @FXML public ChoiceBox<String> delta_choiceBox;
    @FXML public Slider delta_slider;

    public Variable T;
    public Variable distance;
    public Variable n;
    public Variable theta;
    public Variable wavelength;
    public Variable I_incident;
    public Variable I_transmitted;
    public Variable R;
    public Variable B;
    public Variable F;
    public Variable del;
    public Variable delta;



    @FXML public TabPane tabPane;
    @FXML public Tab plusTab;
    public int plusTabIndex;
    public Vector<GraphTab> graphTabVector;

    public Dictionary<String, Variable> getFreeVariables() {
        Dictionary<String, Variable> dict = new Hashtable<String, Variable>();

        if (!T.checkBox.isSelected()) { dict.put(T.symbol, T); }
        if (!distance.checkBox.isSelected()) { dict.put(distance.symbol, distance); }
        if (!n.checkBox.isSelected()) { dict.put(n.symbol, n); }
        if (!theta.checkBox.isSelected()) { dict.put(theta.symbol, theta); }
        if (!wavelength.checkBox.isSelected()) { dict.put(wavelength.symbol, wavelength); }
        if (!I_incident.checkBox.isSelected()) { dict.put(I_incident.symbol, I_incident); }
        if (!I_transmitted.checkBox.isSelected()) { dict.put(I_transmitted.symbol, I_transmitted); }
        if (!R.checkBox.isSelected()) { dict.put(R.symbol, R); }
        if (!B.checkBox.isSelected()) { dict.put(B.symbol, B); }
        if (!F.checkBox.isSelected()) { dict.put(F.symbol, F); }
        if (!del.checkBox.isSelected()) { dict.put(del.symbol, del); }
        if (!delta.checkBox.isSelected()) { dict.put(delta.symbol, delta); }

        return dict;
    }



    public void newTab() {
        System.out.println("a");
        GraphTab graphTab = new GraphTab("??? vs ???", getFreeVariables());
        graphTabVector.addElement(graphTab);
        tabPane.getTabs().add(plusTabIndex, graphTab.tab);
        plusTabIndex++;
        tabPane.getSelectionModel().select(graphTab.tab);
        System.out.println("b");
    }

    public void initialize() {
        graphTabVector = new Vector<GraphTab>();
        plusTabIndex=1;
        plusTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (plusTab.isSelected()) {
                    newTab();
                }
            }
        });

        distance = new Variable(
                "Distance between partial mirrors.",
                distance_checkBox,
                distance_slider,
                distance_textField,
                distance_choiceBox,
                this
        );
        n = new Variable(
                "Refractive Index of cavity.",
                n_checkBox,
                n_slider,
                n_textField,
                n_choiceBox,
                this
        );
        theta = new Variable(
                "Angle of incidence.",
                theta_checkBox,
                theta_slider,
                theta_textField,
                theta_choiceBox,
                this
        );
        wavelength = new Variable(
                "Wavelength of incident light.",
                wavelength_checkBox,
                wavelength_slider,
                wavelength_textField,
                wavelength_choiceBox,
                this
        );
        I_incident = new Variable(
                "Intensity of incident light.",
                I_incident_checkBox,
                I_incident_slider,
                I_incident_textField,
                I_incident_choiceBox,
                this
        );
        I_transmitted = new Variable(
                "Intensity of transmitted light.",
                I_transmitted_checkBox,
                I_transmitted_slider,
                I_transmitted_textField,
                I_transmitted_choiceBox,
                this
        );
        R = new Variable(
                "Reflectivity of mirrors.",
                R_checkBox,
                R_slider,
                R_textField,
                R_choiceBox,
                this
        );
        B = new Variable(
                "Angle of refraction.",
                B_checkBox,
                B_slider,
                B_textField,
                B_choiceBox,
                this
        );
        F = new Variable(
                "Coefficient of Finesse.",
                F_checkBox,
                F_slider,
                F_textField,
                F_choiceBox,
                this
        );
        del = new Variable(
                "Phase Difference.",
                del_checkBox,
                del_slider,
                del_textField,
                del_choiceBox,
                this
        );
        delta = new Variable(
                "Path Difference.",
                delta_checkBox,
                delta_slider,
                delta_textField,
                delta_choiceBox,
                this
        );
        T = new Variable(
                "Transmissivity.",
                T_checkBox,
                T_slider,
                T_textField,
                T_choiceBox,
                this
        );

        delta.DecorateClass();
        wavelength.DecorateClass();
        del.DecorateClass();
        F.DecorateClass();
        T.DecorateClass();
        theta.DecorateClass();
        I_transmitted.DecorateClass();
        I_incident.DecorateClass();
        R.DecorateClass();
        B.DecorateClass();
        n.DecorateClass();
        distance.DecorateClass();


        // F
        {
            F.addUnit(Unit.one);
            {
                Variable[] requirements = new Variable[1];
                requirements[0] = R;

                Formula f = new Formula(() -> {
                    return 4*R.getValue()/pow(1-R.getValue(), 2);
                }, requirements);

                F.addFormula(f);

            }

            {
                Variable[] r = new Variable[2];
                r[0] = T;
                r[1] = del;

                Formula f = new Formula(
                        () -> ((1/T.value)-1)/pow(sin(del.value/2), 2),
                        r
                );

                F.addFormula(f);
            }
        }

        // Del
        {
            del.addUnit(Unit.radian);
            del.addUnit(Unit.degree);
            {
                Variable[] r = new Variable[2];
                r[0] = delta;
                r[1] = wavelength;

                Formula f = new Formula( () -> 2*PI*delta.getValue()/wavelength.getValue(), r);

                del.addFormula(f);
            }

            {
                Variable[] r = new Variable[2];
                r[0] = F;
                r[1] = T;

                Formula f = new Formula(
                        () -> 2*Math.asin(Math.sqrt((1/T.value-1)/F.value)),
                        r
                );

                del.addFormula(f);
            }


        }

        // Delta
        {
            delta.addUnit(Unit.nanometer);
            delta.addUnit(Unit.angstrom);
            {
                Variable[] r = new Variable[2];
                r[0] = del;
                r[1] = wavelength;

                Formula f = new Formula( () -> del.getValue()*wavelength.getValue()/(2*PI), r );

                delta.addFormula(f);

            }

            {
                Variable[] r = new Variable[3];
                r[0] = n;
                r[1]=distance;
                r[2] = B;

                Formula f = new Formula (
                        () -> 2*n.value*distance.value*Math.cos(B.value),
                        r
                );
                delta.addFormula(f);
            }


        }


        // Wavelength
        {
            wavelength.addUnit(Unit.nanometer);
            wavelength.addUnit(Unit.angstrom);
            {
                Variable[] r = new Variable[2];
                r[0] = del;
                r[1] = delta;

                Formula f = new Formula( () -> 2*PI*delta.getValue()/del.getValue(), r );
                wavelength.addFormula(f);
            }
        }

        // n
        {
            n.addUnit(Unit.one);
            {
                Variable[] r = new Variable[3];
                r[0] = delta;
                r[1] = distance;
                r[2] = B;
                Formula f = new Formula(
                        () -> delta.value/2/distance.value/Math.cos(B.value), r
                );
                n.addFormula(f);
            }
        }

        // distance
        {
            distance.addUnit(Unit.meter);
            {
                Variable[] r = new Variable[3];
                r[0] = delta;
                r[1] = n;
                r[2] = B;
                Formula f = new Formula(
                        () -> delta.value/2/n.value/Math.cos(B.value), r
                );
                distance.addFormula(f);
            }
        }

        // beta
        {
            B.addUnit(Unit.radian);
            B.addUnit(Unit.degree);
            {
                Variable[] r = new Variable[3];
                r[0] = delta;
                r[1] = n;
                r[2] = distance;
                Formula f = new Formula(
                        () -> Math.acos(delta.value/2/n.value/distance.value), r
                );
                B.addFormula(f);
            }
        }

        // Transmittance
        {
            T.addUnit(Unit.one);
            {
                Variable[] r = new Variable[2];
                r[0] = I_transmitted;
                r[1] = I_incident;
                Formula f = new Formula(
                        () -> I_transmitted.value/I_incident.value, r
                );
                T.addFormula(f);
            }

            {
                Variable[] r = new Variable[2];
                r[0] = F;
                r[1] = del;
                Formula f = new Formula(
                        () -> 1/(1+F.value*Math.pow(Math.sin(del.value/2), 2)), r
                );
                T.addFormula(f);
            }
        }

        // I_transmitted
        {
            I_transmitted.addUnit(Unit.candela);
            {
                Variable[] r = new Variable[2];
                r[0] = T;
                r[1] = I_incident;
                Formula f = new Formula(
                        () -> T.value*I_incident.value, r
                );
                I_transmitted.addFormula(f);
            }
        }

        // I_incident
        {
            I_incident.addUnit(Unit.candela);
            {
                Variable[] r = new Variable[2];
                r[0] = T;
                r[1] = I_transmitted;
                Formula f = new Formula(
                        () -> I_transmitted.value/T.value, r
                );
                I_incident.addFormula(f);
            }
        }
    }



    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("HelloFX.fxml")));
        stage.setTitle("titleaavd");
        Scene scene = new Scene(root, 1200,800);
        stage.setScene(scene);
        stage.show();
//        Initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void refreshGraphOptions(){
        System.out.println("aa");
        Dictionary<String, Variable> freeVars = getFreeVariables();
        System.out.println("bb");
        for (GraphTab graphTab : graphTabVector) {
            graphTab.setChoices(freeVars);
        }
        System.out.println("cc");

    }

    public void refreshGraph(Variable changed) {
        for (GraphTab graphTab : graphTabVector) {
            if (changed.requiredBy.contains(graphTab.x_var) | changed.requiredBy.contains(graphTab.y_var)) {
                graphTab.graph();
            }
        }
    }



}

