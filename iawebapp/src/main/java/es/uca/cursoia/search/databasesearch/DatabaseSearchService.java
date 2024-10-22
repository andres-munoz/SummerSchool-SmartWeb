package es.uca.cursoia.search.databasesearch;

import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseSearchService {

    final DataSource dataSource;
    private final ChatLanguageModel chatLanguageModel;
    private JdbcTemplate jdbcTemplate;
    private ContentRetriever contentRetriever;

    public DatabaseSearchService(ChatLanguageModel chatLanguageModel, JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.chatLanguageModel = chatLanguageModel;
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;

        contentRetriever = SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatLanguageModel(chatLanguageModel)
                .build();
    }

    public List<List<String>> executeNLQuery(String naturalLanguageQuery) {
        Query query = Query.from(naturalLanguageQuery);
        List<Content> content = contentRetriever.retrieve(query);
        String text = content.get(0).textSegment().text();
        // Convertir el resultado
        // Tomar lo que viene a partir del caracter dos puntos :
        text = text.substring(text.indexOf(":") + 2);
        // Cada linea viene delimitada por un salto de linea y cada columna separado por comas
        List<List<String>> result = new ArrayList<>();
        String[] lines = text.split("\n");
        for (String line : lines) {
            List<String> row = new ArrayList<>();
            String[] columns = line.split(",");
            for (String column : columns) {
                row.add(column);
            }
            result.add(row);
        }


        return result;
    }


}