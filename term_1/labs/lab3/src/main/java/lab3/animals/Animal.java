package lab3.animals;

import lab3.actions.Actionable;

import java.util.Objects;

abstract class Animal implements Actionable{

    protected String name;
    protected String mood;

    public Animal(String name, String mood) {
        this.name = name;
        this.mood = mood;
    }

    public abstract void observe();

    @Override
    public abstract void act();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Animal)) return false;
        Animal other = (Animal)obj;
        return Objects.equals(name, other.name) && Objects.equals(mood, other.mood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mood);
    }

    @Override
    public String toString() {
        return "Animal[name=" + name + ", mood =" + mood +"]";
    }
}