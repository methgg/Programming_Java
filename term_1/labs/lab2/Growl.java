import ru.ifmo.se.pokemon.*;

public final class Growl extends StatusMove{
    public Growl(){
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect e = new Effect().stat(Stat.ATTACK, -1);
        p.addEffect(e);
    }


    @Override
    public String describe(){
        return "использует Growl";
    }
}