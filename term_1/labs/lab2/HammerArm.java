import ru.ifmo.se.pokemon.*;

public final class HammerArm extends PhysicalMove{
    public HammerArm(){
        super(Type.FIGHTING, 100, 90);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        Effect e = new Effect().stat(Stat.SPEED, -1);
        p.addEffect(e);
    }

    @Override
    public String describe(){
        return "использует Hammer Arm";
    }
}