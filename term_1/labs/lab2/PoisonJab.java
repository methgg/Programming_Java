import ru.ifmo.se.pokemon.*;

public final class PoisonJab extends PhysicalMove{
    public PoisonJab(){
        super(Type.POISON, 80, 100);
    }
    protected void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.3){
            Effect.poison(p);
        }
    }
    public String describe(){
        return "использует Poison Jab!";
    }
}