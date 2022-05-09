package ArtWork;

import java.util.List;
import Person.Person;
public interface IArtWork {
    public String getName();
    public List<Person> getCast();
    public int getYear();
    public int getID();
}
