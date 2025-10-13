import ru.ifmo.se.pokemon.*;

public class lab2{
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Pheromosa("Феромоза", 45);
        Pokemon p2 = new Phanpy("Фанпи", 86);
        Pokemon p3 = new Donphan("Донфан", 86);
        Pokemon p4 = new Porygon("Поригон", 126);
        Pokemon p5 = new Porygon2("Поригон2", 126);
        Pokemon p6 = new Porygon_Z("Поригон-Z", 126);

        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}