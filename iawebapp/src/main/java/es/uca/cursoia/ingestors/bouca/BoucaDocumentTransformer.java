//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package es.uca.cursoia.ingestors.bouca;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoucaDocumentTransformer implements DocumentTransformer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    Map<String, String> metadataCssSelectors;


    public BoucaDocumentTransformer() {
    }


    @Override
    public Document transform(Document document) {
        return document;
    }

    @Override
    public List<Document> transformAll(List<Document> documents) {

        // We only expect one document
        Document doc = documents.get(0);
        String url = doc.metadata().getString("url");

        // Añadimos metadato de titulo con el numero del BOUCA
        String title = url.substring(url.lastIndexOf('/') + 1);
        doc.metadata().put("title", title);

        // Añadimos metadatos de fecha de última actualización
        doc.metadata().put("lastUpdate", LocalDateTime.now().toString());

        List<String> toc = parseTableOfContents(doc);
        logger.debug(">>> Table of Contents: ");
        toc.forEach(e -> logger.debug(e));

        return splitDocument(doc);
    }


    private List<String> parseTableOfContents(Document doc) {

        // EL indice empieza con una linea con SUMARIO
        // Luego sigue una lista de secciones, subsecciones y subsubsecciones con su nombre y la pagina
        // Ejemplo:
        // SUMARIO
        // I. DISPOSICIONES Y ACUERDOS DE LOS ÓRGANOS DE GOBIERNO DE LA
        // UNIVERSIDAD DE CÁDIZ .................................................................................................... 5
        // I.1. CONSEJO SOCIAL ........................................................................................................ 5
        // Acuerdo del Consejo Social de 29 de febrero de 2024, sobre propuesta de nombramiento
        // de la Gerencia de la Universidad de Cádiz por parte del Rector. ..................................... 5
        // Acuerdo del Consejo Social de 29 de febrero de 2024, sobre actividades del Consejo Social
        // 2022-2023 ........................................................................................................................... 5
        // I.3 RECTOR ......................................................................................................................... 7
        // Resolución del Rector UCA/R34REC/2024 por la que convocan diversos procesos
        // electorales por renovación del sector de estudiantes del Claustro, para cubrir vacantes en
        // el Consejo de Gobierno y elección de Delegados de Campus. ......................................... 7
        // Resolución del Rector de la Universidad de Cádiz UCA/R36REC/2024 por la que se
        // convocan ayudas para la realización de proyectos y actividades de colaboración
        // internacional para el curso 2023/2024 en el ámbito del Aula Universitaria del Estrecho.
        // .......................................................................................................................................... 10

        List<String> result = new ArrayList<>();

        boolean inToc = false;

        logger.debug(">>> Contents: ");
        doc.text().lines().forEach(e -> logger.debug(e));

        for (String line : doc.text().lines().toList()) {
            // detectar el fin del índice
            if (result.size() > 1 && line.contains("I. DISPOSICIONES")) {
                break;
            }

            if (line.contains("SUMARIO")) {
                inToc = true;
            }

            if (inToc && line.matches("^[IVXLCDM]+\\..*")) {
                result.add(line);
            }


        }


        return result;

    }

    public List<Document> splitDocument(Document document) {

        List<Document> result = new ArrayList<>();


        int i = 0;
        // Dividir el documento en subdocumentos, que estan separados por los caracteres ***]]]

        String[] data = document.text().split("\\* \\* \\*");

        for (String subdoc : data) {
            Metadata metadata = document.metadata().copy();
            // pintamos metadata
           /*
            metadata.asMap().forEach((k, v) -> System.out.println(k + " : " + v));
            metadata.add("url", "url");
            metadata.add("boucanum", "url");
            metadata.add("numpagina", "url");
            metadata.add("seccion", "url");
            metadata.add("fecha", "url");
            */

            // Cada subdocumento es un documento
            Document subDocument = Document.from(subdoc, metadata);


            // Sanitizamos el documento
            HtmlToTextDocumentTransformer htmlTextExtractor = new HtmlToTextDocumentTransformer();
            subDocument = htmlTextExtractor.transform(subDocument);

            logger.info(">>> Document metadata..." + subDocument.metadata());
            logger.debug(">>> Document text...\n " + subDocument.text());
            result.add(subDocument);

        }


        return result;
    }


}
