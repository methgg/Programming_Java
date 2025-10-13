import ru.ifmo.se.pokemon.*;

public final class Thunderbolt extends SpecialMove{
    public Thunderbolt(){
        super(Type.ELECTRIC, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.1){
            Effect.paralyze(p);
        }
    }

    @Override
    public String describe(){
        return "использует Thunderbolt";
    }
}