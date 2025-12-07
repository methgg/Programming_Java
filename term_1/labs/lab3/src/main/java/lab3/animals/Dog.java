package lab3.animals;

import lab3.humans.Owner;

import java.util.Random;

public class Dog extends Animal{

    private Owner owner;

    public Dog(String name, String mood, Owner owner) {
        super(name, mood);
        this.owner = owner;
    }

    public void admireOwner(){
        Random rand = new Random();
        if (rand.nextBoolean()) {
            System.out.println(name + " восхищается догадливостью " + owner.getName() + ".");
        }
        else{
            System.out.println(name + " сомневается в догадливости " + owner.getName() + ".");
        }
    }
    
    @Override
    public void observe(){
        System.out.println(name + " осматривает округу.");
    }

    @Override
    public void act(){
        observe();
        admireOwner();
    }

}