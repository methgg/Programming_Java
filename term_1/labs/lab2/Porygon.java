import ru.ifmo.se.pokemon.*;

public class Porygon extends Pokemon{
    public Porygon(String name, int level){
        super(name, level);

        setType(Type.NORMAL);

        setStats(65, 60, 70, 85, 75, 40);

        setMove(new ZapCannon(), new Thunderbolt());
    }
}