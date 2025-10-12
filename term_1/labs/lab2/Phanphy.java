import ru.ifmo.se.pokemon.*;

public class Phanphy extends Pokemon{
    public Phanphy(String name, int level){
        super(name, level);

        setType(Type.GROUND);

        setStats(90, 60, 60, 40, 40, 40);

        setMove(new Growl(), new Facade());
    }
}