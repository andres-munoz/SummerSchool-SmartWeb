package es.uca.cursoia.ingestors.bouca;

import dev.langchain4j.data.document.BlankDocumentException;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.internal.Utils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class BoucaPdfDocumentParser implements DocumentParser {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    public BoucaPdfDocumentParser() {
    }

    public Document parse(InputStream inputStream) {
        try {
            PDDocument pdfDocument = PDDocument.load(inputStream);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdfDocument);
            text = removeDigitalSignatures(text);
            logger.debug(">>> PDF Text: " + text);
            pdfDocument.close();
            if (Utils.isNullOrBlank(text)) {
                throw new BlankDocumentException();
            } else {
                return Document.from(text);
            }
        } catch (IOException var5) {
            IOException e = var5;
            throw new RuntimeException(e);
        }
    }

    private String removeDigitalSignatures(String text) {
        // Aparecen varias firmas digitales en los documentos PDF
        // Las firmas electronicas tienen un formato similar a:
        // CSV (Código de Verificación
        // ...texto
        // Url de verificación https://sede.uca.es/verifirma/code/IV7WHWCSJ34DXHVL4LQUALTPCM Página 2/2

        // Eliminamos las firmas digitales detectando los comienzos de firma y eliminando hasta el final de la firma
        String[] lines = text.split("\n");
        StringBuilder sb = new StringBuilder();
        boolean firma = false;
        for (String line : lines) {
            if (line.contains("CSV (Código de Verificación")) {
                firma = true;
            }
            if (!firma) {
                sb.append(line).append("\n");
            }
            if (line.contains("Url de verificación")) {
                firma = false;
            }
        }

        return sb.toString();

    }
}
