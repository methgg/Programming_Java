import java.util.ArrayList;

public class Owner extends Human{

    public Owner(String name){
        super(name);
    }

    public void trackRunaways(ArrayList<Runaway> runaways){
        for (Runaway r : runaways){
            if(r.getState() == RunawayState.MOVING) {
                System.out.println(name + " выслеживает " + r.getName() + ", который направяяется к кустам " + r.getBush().location() + ".");
            }
            else {
                System.out.println(name + " не может выследить " + r.getName() + ".");
            }
        }
    }

    @Override
    public void act(){
        System.out.println(name + " внимательно осматривает кусты.");
    }
}