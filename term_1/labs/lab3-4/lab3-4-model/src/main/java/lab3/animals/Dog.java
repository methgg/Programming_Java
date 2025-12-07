package lab3.animals;

import java.util.Random;

import lab3.exceptions.NoOwnerException;
import lab3.humans.Owner;

public class Dog extends Animal{

    private final Owner owner;

    public Dog(String name, String mood, Owner owner) {
        super(name, mood);
        this.owner = owner;
    }

    public void admireOwner(){
        if(owner == null){
            throw new NoOwnerException("У собаки нет хозяина!");
        }

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