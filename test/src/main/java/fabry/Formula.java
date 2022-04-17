package fabry;

import java.util.concurrent.Callable;

public class Formula {

    public Variable requirements[];
    public Callable<Double> formula;

    public double call() throws Exception {
        return formula.call();
    }

    public Formula(Callable<Double> _formula, Variable _requirements[]) {
        requirements = _requirements;
        formula = _formula;
    }



}
