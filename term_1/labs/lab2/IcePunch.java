import ru.ifmo.se.pokemon.*;

public final class IcePunch extends PhysicalMove{
    public IcePunch(){
        super(Type.ICE, 75, 100);
    }
    public String describe(){
        return "использует Ice Punch!";
    }
}