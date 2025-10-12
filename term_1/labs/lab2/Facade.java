import ru.ifmo.se.pokemon.*;

public final class Facade extends PhysicalMove{
    public Facade(){
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def){
        double basePower = super.calcBaseDamage(att, def);
        Status status = att.getCondition();
        if (status == Status.BURN || status == Status.PARALYZE || status == Status.POISON){
            basePower *= 2;
        }

        return basePower;
    }

    @Override
    public String describe(){
        return "использует Facade";
    }
}