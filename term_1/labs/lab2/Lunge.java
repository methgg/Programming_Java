import ru.ifmo.se.pokemon.*;

public final class Lunge extends PhysicalMove{
    public Lunge(){
        super(Type.BUG, 80, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect e = new Effect().stat(Stat.ATTACK, -1);
        p.addEffect(e);
    }
    
    @Override
    public String describe(){
        return "использует Lunge";
    }
}