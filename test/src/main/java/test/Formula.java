package test;

import java.util.concurrent.Callable;

public class Formula {

    public Variable requirements[];
    public Callable<Double> formula;

    public Formula(Callable<Double> _formula, Variable _requirements[]) {
        requirements = _requirements;
        formula = _formula;
    }



}
