package ArtWork;

import ArtWork.IArtWork;
import Person.Person;

import java.util.List;

public class Play implements IArtWork {
    String name;
    String productionCompany;
    List<Person> cast;
    int year;
    int ID;
    protected static int countPlay = 0;

    public Play(String name, String productionCompany, int year) {
        this.name = name;
        this.year = year;
        this.productionCompany = productionCompany;
        ID = ++countPlay;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Person> getCast() {
        return cast;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getID() {
        return ID;
    }

    public String getProductionCompany() {
        return productionCompany;
    }
}
