package lab3;

import java.util.ArrayList;
import java.util.Random;
import lab3.animals.*;
import lab3.environment.*;
import lab3.exceptions.*;
import lab3.humans.*;

public class Main {
    public static void main(String[] args) {
        Owner mrMorkou = new Owner("Мистер Моркоу");
        Dog rex = new Dog("Рекс", "восторженно", mrMorkou);
        Random rand = new Random();

        ArrayList<Bush> bushes = new ArrayList<>();
        String[] locs = {"слева", "справа", "спереди"};
        String[] dens = {"густые", "редкие", "средние"};
        for (String loc : locs){
            bushes.add(new Bush(loc, dens[rand.nextInt(dens.length)]));    
        } 
        
        int numRunaways = rand.nextInt(5) + 1;

        try {
            if (numRunaways > bushes.size())
                throw new NotEnoughBushesException();

            ArrayList<Runaway> runaways = new ArrayList<>();

            for (int i = 0; i < numRunaways; i++){
                runaways.add(new Runaway("Беглец " + (i + 1), bushes.get(i)));
            }

            rex.act();

            for (Runaway r :runaways) r.act();

            mrMorkou.act();
            mrMorkou.trackRunaways(runaways);

            
        } catch (NotEnoughBushesException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}