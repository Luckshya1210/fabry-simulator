hello:
	java --module-path fx/javafx-sdk-17.0.2/lib/ --add-modules javafx.controls,javafx.fxml test/HelloFX.java

a:
	g++ a.cpp
	./a.out

controller:
	java --module-path fx/javafx-sdk-17.0.2/lib/ --add-modules javafx.controls,javafx.fxml test/Controller.java