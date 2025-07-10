package es.uca.summerschool.smartwebapp.services.conversational;

import java.util.List;

public interface Searchable {

    public List<List<String>> search(String query);

}