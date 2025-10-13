import ru.ifmo.se.pokemon.*;

public final class ThunderFang extends PhysicalMove{
    public ThunderFang(){
        super(Type.ELECTRIC, 65, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.1){
            Effect.paralyze(p);
        }
        if (Math.random() <= 0.1){
            Effect.flinch(p);
        }
    }

    @Override
    public String describe(){
        return "использует Thunder Fang";
    }
}