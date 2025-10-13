import ru.ifmo.se.pokemon.*;

public final class ZapCannon extends SpecialMove{
    public ZapCannon(){
        super(Type.ELECTRIC, 120, 50);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect.paralyze(p);
    }

    @Override
    public String describe(){
        return "использует Zap Cannon";
    }
}