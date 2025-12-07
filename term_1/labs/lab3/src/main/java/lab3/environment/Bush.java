package lab3.environment;

public record Bush(String location, String density) implements Surrounding{
    @Override
    public void act(){
        System.out.println("Кусты " + location + "(" + density + ") слегка колышутся.");
    }
}