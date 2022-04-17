package fabry;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Vector;

import javafx.scene.control.*;
import fabry.Exceptions.UnfulfilledDependenciesException;
import fabry.Exceptions.MathError;


public class Variable {

    public Main parent;


    public CheckBox checkBox;
    public TextField textField;
    public ChoiceBox<String> choiceBox;
    public Slider slider;

    public Double value;
    public Double unit;
    public String name;
    public String symbol;
    public String description;

    public Vector<Formula> formulas = new Vector<Formula>();
    public Vector<Variable> requiredBy = new Vector<Variable>();

    public Dictionary<String, Double> units = new Hashtable<String, Double>();

    // Adds formula to the vector formula
    // And adds the requirements of formula to requiredBy

    public void addFormula(Formula formula) {

        formulas.addElement(formula);
        for (Variable d : formula.requirements) {
            if (!requiredBy.contains(d)) {
                requiredBy.addElement(d);
            }
        }
    }

    public void addUnit(String name, Double value) {
        units.put(name, value);
        choiceBox.getItems().add(name);
        if (units.size()==1) {
            choiceBox.getSelectionModel().selectFirst();
            this.unit=value;
        }
    }

    public void addUnit(Unit unit) {
        units.put(unit.name, unit.value);
        choiceBox.getItems().add(unit.name);
        if (units.size()==1) {
            choiceBox.getSelectionModel().selectFirst();
            this.unit=unit.value;
        }
    }



    public void setNull() {
        textField.setText("???");
    }


    public Double getValue() {
        return value;
    }


    public boolean evaluating;

    public void evaluate() throws UnfulfilledDependenciesException, MathError{
        try {
            calculate();
            textField.setText(String.format("%.2f",value/unit));
        } catch (UnfulfilledDependenciesException err) {
            textField.setText("???");
        } catch (MathError err) {
            textField.setText("MathError");
        }

    }

    public void calculate() throws UnfulfilledDependenciesException,MathError{

        evaluating=true;
        try {
            for (Formula formula : formulas) {
                try {
                    for (Variable r : formula.requirements) {
                        if (!r.checkBox.isSelected()) {
                            if (r.evaluating) {
                                throw new UnfulfilledDependenciesException();
                            } else {
                                r.calculate();
                            }
                        }
                    }
                } catch (UnfulfilledDependenciesException err) {
                    continue;
                }
                try {
                    value = formula.call();
                    evaluating=false;
                    break;
                } catch (Exception err) {
                    throw new MathError();
                }
            }
            if (evaluating) {
                throw new UnfulfilledDependenciesException();
            }
        } finally {
            evaluating=false;
        }

    }


    public void calculateGraph(Variable given) throws UnfulfilledDependenciesException, MathError{
        evaluating=true;

        try {
            if (given==this) {
                return;
            }
            for (Formula formula : formulas) {
                try {
                    for (Variable r : formula.requirements) {
                        if (!(r.checkBox.isSelected() | r==given)) {
                            if (r.evaluating) {
                                throw new UnfulfilledDependenciesException();
                            } else {
                                r.calculate();
                            }
                        }
                    }
                } catch (UnfulfilledDependenciesException err) {
                    continue;
                }
                try {
                    value = formula.call();
                    evaluating=false;
                    break;
                } catch (Exception err) {
                    throw new MathError();
                }
            }
            if (evaluating) {
                throw new UnfulfilledDependenciesException();
            }
        } finally {
            evaluating=false;
        }
    }


    public void update_requiredBy() {
        for (Variable var : requiredBy) {
            if (!var.checkBox.isSelected()) {
                try {
                    var.evaluate();
                } catch (UnfulfilledDependenciesException | MathError ignored) {
                    ;
                }
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

    public Vector<String> conflictNames(Vector<Variable> conflicts) {
        Vector<String> names = new Vector<String>();
        for (Variable v : conflicts) {
            names.addElement(v.symbol);
        }
        return names;
    }

    public void setDisable(boolean bool) {
        this.slider.setDisable(bool);
        this.choiceBox.setDisable(bool);
        this.textField.setDisable(bool);
    }


    Variable(String _description, CheckBox _checkBox, Slider _slider, TextField _textField, ChoiceBox<String> _choiceBox, Main parent){
        this.description = _description;
        this.checkBox=_checkBox;
        this.slider=_slider;
        this.textField=_textField;
        this.choiceBox=_choiceBox;
        this.parent = parent;
    }

    public void DecorateClass() {
        setDisable(true);


        symbol = this.checkBox.getText();

        checkBox.setTooltip(new Tooltip(description));

        slider.valueProperty().addListener((observable, old_value, new_value) -> {
            if (!Objects.equals(new_value, old_value)) {
                value = new_value.doubleValue() * unit;
                textField.setText(String.format("%.2f", new_value.doubleValue()));
                update_requiredBy();
                parent.refreshGraph(this);
                //            System.out.println(new_value);
            }
        });

        checkBox.selectedProperty().addListener((observable, old_value, new_value) -> {
            if (new_value!=old_value) {
                System.out.println("c");
                if (new_value) {
                    if (satisfied()) {
                        checkBox.setSelected(false);
                        DialogBox.display("Cannot enable this variable.","Cannot enable this variable because it's conflicts with already enabled variables.");
                    } else {

                        //                    System.out.println(321331);
                        Vector<Variable> conflicts = checkConflicts();
                        System.out.println(conflicts.size());
                        if (conflicts.size() > 0) {
                            checkBox.setSelected(false);
                            DialogBox.display("Cannot enable this variable", String.format("You cannot enable %s variable because it's value conflicts with: ", String.join(", ", conflictNames(conflicts))));
                        } else {
                            setDisable(false);
                        }
                    }
                } else {
                    setDisable(true);
                }
                parent.refreshGraphOptions();
                System.out.println("end");
            }
        });

        textField.textProperty().addListener((observable, old_value, new_value) -> {
            if (!new_value.equals(old_value)) {
                double _new_value;
                try {
                    _new_value = Double.parseDouble(new_value);
                } catch (NumberFormatException e) {
                    if (!((new_value.equals("???") | new_value.equals("MathError")) && !checkBox.isSelected())) {
                        DialogBox.display("Bad Value.", "Enter only positive integer in text field.");
                        textField.setText(old_value);
                    }
                    return;
                }
                if ((slider.getMin() <= _new_value) && (_new_value <= slider.getMax())) {
                    slider.setValue(_new_value);
                    value = _new_value*unit;
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

    public boolean satisfied() {
        for (Formula f : formulas) {
            boolean s=true;
            for (Variable r : f.requirements) {
                if (!r.checkBox.isSelected()) {
                    s=false;
                    break;
                }
            }
            if (s) {
                return true;
            }
        }
        return false;
    }
}
