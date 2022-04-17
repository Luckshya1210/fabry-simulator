package fabry;


import static java.lang.Math.PI;

public class Unit {
    public String name;
    public Double value;
    Unit (String name, Double value) {
        this.name=name;
        this.value=value;
    }
    public static Unit nanometer = new Unit("nm", 0.000000001);
    public static Unit angstrom = new Unit("angstrom", 0.0000000001);
    public static Unit radian = new Unit("rad", 1.0);
    public static Unit degree = new Unit("degree", 180/PI);
    public static Unit one = new Unit("1", 1.0);
    public static Unit candela = new Unit("cd", 1.0);
    public static Unit meter = new Unit("m", 1.0);



}