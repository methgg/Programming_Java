import ru.ifmo.se.pokemon.*;

public class lab2{
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Pheromosa("Феромоза", 45);
        Pokemon p2 = new Phanphy("Фанфи", 25);

        b.addAlly(p1);
        b.addFoe(p2);
        b.go();
    }
}