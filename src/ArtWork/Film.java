package ArtWork;

import ArtWork.IArtWork;
import Person.Person;

import java.util.List;

public class Film implements IArtWork {
    String name;
    List<Person> cast;
    int year;
    int id;

    protected static int countFilms = 0;

    public Film(String name, int year) {
        this.name = name;
        this.year = year;
        id = ++countFilms;
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
        return id;
    }


}
