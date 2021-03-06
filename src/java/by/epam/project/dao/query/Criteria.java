package by.epam.project.dao.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class of collection of parameters for query forming.
 * @author Helena.Grouk
 * @param <T>
 */
public class Criteria<T> {
    private Map<String, T> params = new HashMap<>();
    public Criteria() {
    }
    public void addParam(String name, T value) {
        params.put(name, value);
    }
    public T getParam(String name){
        return params.get(name);
    }
    public Collection<T> getAll(){
        return params.values();
    }
    public void remuveParam(String name){
        params.remove(name);
    }
}
