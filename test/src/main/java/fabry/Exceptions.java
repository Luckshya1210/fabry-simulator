package fabry;

public class Exceptions {
    public static class UnfulfilledDependenciesException extends Exception {
        public UnfulfilledDependenciesException() {
            super();
        }
    }
    public static class MathError extends Exception {
        public MathError() {
            super();
        }
    }
}
