import java.util.Random;

public class Runaway extends Human{
    private RunawayState state = RunawayState.HIDING;
    private Bush bush;

    public Runaway(String name, Bush bush){
        super(name);
        this.bush = bush;
    }

    public RunawayState getState(){
        return state;
    }

    public Bush getBush(){
        return bush;
    }

    public void hide(){
        state = RunawayState.HIDING;
        System.out.println(name + " остается прятаться в кустах " + bush.location() + "(" + bush.density() + ").");
    }

    public void move(Bush newbush){
        if (newbush.density().equals("густые")) {
            newbush.act();
            System.out.println(name + "не может пройти через густые кусты " + newbush.location() + " и остается прятаться.");
            hide();
        }
        else {
            newbush.act();
            state = RunawayState.MOVING;
            bush = newbush;
            System.out.println(name + " перемещается в " + newbush.density() + " кусты " + newbush.location() + ".");
        }
        
    }

    @Override
    public void act(){
        Random rand = new Random();
        if (rand.nextBoolean()){
            hide();
        }
        else{
            String[] locs = {"слева", "справа", "спереди"};
            String[] dens = {"густые", "средние", "редкие"};

            move(new Bush(locs[rand.nextInt(locs.length)], dens[rand.nextInt(dens.length)]));
        }

    }
    //возможно нужно переопределить hashCode и equals
}