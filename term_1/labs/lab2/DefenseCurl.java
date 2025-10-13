import ru.ifmo.se.pokemon.*;

public final class DefenseCurl extends StatusMove{
    public DefenseCurl(){
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        Effect e = new Effect().stat(Stat.DEFENSE, 1);
        p.addEffect(e);
    }

    @Override
    public String describe(){
        return "использует Defense Curl";
    }
}