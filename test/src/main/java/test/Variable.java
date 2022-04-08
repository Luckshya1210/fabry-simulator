package test;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import test.Exceptions.UnfulfilledDependenciesException;
import test.Exceptions.MathError;

class Unit {
    public String name;
    public Double value;
    public Unit (String _name, Double _value) {
        name=_name;
        value=_value;
    }
}


public class Variable {


    public CheckBox checkBox;
    public TextField textField;
    public ChoiceBox<String> choiceBox;
    public Slider slider;
    public String name;

    public double value;
    public double _value;
    public double unit;

    public Vector<Formula> formulas = new Vector<Formula>();
    public Vector<Variable> requiredBy = new Vector<Variable>();

    public Dictionary<String, Double> units;

    // Adds formula to the vector formula
    // And adds the requirements of formula to requiredBy

    public void addFormula(Formula formula) {

        formulas.addElement(formula);
        for (Variable d : formula.requirements) {
            boolean found=false;
            for (Variable v : requiredBy) {
                if (v==d) {
                    found=true;
                }
            }
            if (!found) {
                requiredBy.addElement(d);
            }
        }
    }





    public void setNull() {
        textField.setText("???");
    }


    public double getValue() {
        return value*unit;
    }


    public boolean evaluating=false;

    public void evaluate() throws UnfulfilledDependenciesException,MathError{

        evaluating=true;
        try {
            for (Formula formula : formulas) {
                try {
                    for (Variable d : formula.requirements) {
                        if (!d.checkBox.isSelected()) {
                            if (evaluating) {
                                throw new UnfulfilledDependenciesException();
                            }
                            d.evaluate();
                        }
                    }
                } catch (UnfulfilledDependenciesException | MathError err) {
                    continue;
                }
                try {
                    value = formula.formula.call();
                    break;
                } catch (Exception err) {
                    throw new MathError();
                }
            }
        } finally {
            evaluating=false;
        }

    }


    public void update_requiredBy() {
        for (Variable var : requiredBy) {
            try {
                var.evaluate();
            } catch (UnfulfilledDependenciesException | MathError ignored) {
                ;
            }
        }
    }

    public Vector<Variable> checkConflicts() {
        Vector<Variable> conflicts = new Vector<Variable>();

        for (Variable r : requiredBy) {
            int count = 0;
            for (Formula formula : r.formulas) {
                boolean satisfied = true;
                for (Variable v : formula.requirements) {
                    if (!v.checkBox.isSelected()) {
                        satisfied=false;
                        break;
                    }
                }
                if (satisfied) {
                    count++;
                    if (count>1) {
                        conflicts.addElement(r);
                        break;
                    }
                }
            }
        }
        return conflicts;
    }

    private Vector<String> conflictNames(Vector<Variable> conflicts) {
        Vector<String> names = new Vector<String>();
        for (Variable v : conflicts) {
            names.addElement(v.name);
        }
        return names;
    }



    public Variable(String _name, Vector<Unit> _units) {
        name = _name;
        for (Unit u : _units) {
            units.put(u.name, u.value);
        }
    }

    public void Initialise_Listeners() {


        slider.valueProperty().addListener((observable, old_value, new_value) -> {
            value = new_value.doubleValue()*unit;
            textField.setText(Integer.toString((int)(value*unit)));
            update_requiredBy();
        });

        checkBox.selectedProperty().addListener((observable, old_value, new_value) -> {
            if (new_value!=old_value) {
                if (new_value) {
                    Vector<Variable> conflicts = checkConflicts();
                    if (conflicts.size() > 0) {
                        DialogBox.display("Cannot enable this variable", String.format("You cannot enable %s variable because it's value conflicts with: ", String.join(", ", conflictNames(conflicts))));
                    }
                }
            }
        });

        textField.textProperty().addListener((observable, old_value, new_value) -> {
            if (!new_value.equals(old_value)) {
                int _new_value;
                try {
                    _new_value = Integer.parseInt(new_value);
                } catch (NumberFormatException e) {
                    DialogBox.display("Bad Value.", "Enter only positive integer in text field.");
                    textField.setText(old_value);
                    return;
                }
                if (!((slider.getMin() <= _new_value) && (_new_value <= slider.getMax()))) {
                    DialogBox.display("Value out of bounds.", String.format("Please enter a value more than %s and less than %s.", slider.getMin(), slider.getMax()));
                    textField.setText(old_value);
                } else {
                    slider.setValue(_new_value);
                    value = _new_value;
                    update_requiredBy();
                }
            }
        });


        choiceBox.valueProperty().addListener((observable, old_value, new_value) -> {
            if (!new_value.equals(old_value)) {
                unit=units.get(new_value);
                update_requiredBy();
            }
        });

    }
}
