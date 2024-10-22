package es.uca.cursoia.conversational.assistants.bouca;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import es.uca.cursoia.conversational.assistants.AssistantService;
import es.uca.cursoia.ingestors.bouca.BoucaETL;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoucaAssistantService extends AssistantService {

    private final BoucaETL boucaETL;

    private final BoucaAssistant boucaAssistant;
    StreamingChatLanguageModel streamingChatLanguageModel;
    ChatLanguageModel chatLanguageModel;

    public BoucaAssistantService(ChatLanguageModel chatLanguageModel, StreamingChatLanguageModel streamingChatLanguageModel, BoucaETL boucaETL) {
        this.chatLanguageModel = chatLanguageModel;
        this.streamingChatLanguageModel = streamingChatLanguageModel;
        this.boucaETL = boucaETL;

        boucaAssistant = AiServices.builder(BoucaAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> createSystemMessage(BoucaAssistant.SPECIFIC_PROMPT_TEMPLATE, (String) chatSessionId))
                //.contentRetriever(createContentRetriever())
                .retrievalAugmentor(createRetrievalAugmentor())
                .build();

    }


    private EmbeddingStoreContentRetriever createContentRetriever() {

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(boucaETL.getEmbeddingStore())
                .embeddingModel(boucaETL.getEmbeddingModel())
                .minScore(boucaETL.MIN_SCORE)
                .maxResults(boucaETL.MAX_RESULTS_RETRIEVED)
                .displayName("BOUCA")
                .build();


    }

    private RetrievalAugmentor createRetrievalAugmentor() {

        EmbeddingStoreContentRetriever retriever = createContentRetriever();

        DefaultContentInjector injector = DefaultContentInjector.builder()
                .metadataKeysToInclude(List.of("title", "index", "url"))   //"file_name",
                .build();

        QueryTransformer queryTransformer = new CompressingQueryTransformer(chatLanguageModel);

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(retriever)
                .contentInjector(injector)
                .build();

        return retrievalAugmentor;


    }


    public TokenStream chat(String chatSessionId, String userMessage) {
        return boucaAssistant.chat(chatSessionId, userMessage);
    }


}
