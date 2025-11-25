import java.util.Objects;

public abstract class Human implements Actionable{
    protected String name;

    public Human(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public abstract void act();

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Human)) return false;
        Human other = (Human) obj;
        return Objects.equals(other.name, name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name); 
    }

    @Override
    public String toString(){
        return "Human[name=" + name + "]";
    }
}