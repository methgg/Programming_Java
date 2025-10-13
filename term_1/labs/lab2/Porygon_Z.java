import ru.ifmo.se.pokemon.*;

public final class Porygon_Z extends Porygon2{
    public Porygon_Z(String name, int level){
        super(name, level);

        setStats(85, 80, 70, 135, 75, 90);

        addMove(new DoubleTeam());
    }
}