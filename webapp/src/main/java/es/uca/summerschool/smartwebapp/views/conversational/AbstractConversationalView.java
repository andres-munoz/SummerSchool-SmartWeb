package es.uca.summerschool.smartwebapp.views.conversational;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import org.vaadin.firitin.components.messagelist.MarkdownMessage;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@AnonymousAllowed
public abstract class AbstractConversationalView extends VerticalLayout {

    private final Button newChatButton;
    private final VerticalLayout chat;
    private final MessageInput messageInput;
    private final AuthenticatedUser authenticatedUser;
    private final Conversational service;
    private UI ui;
    private String chatSessionId;

    public AbstractConversationalView(AuthenticatedUser authenticatedUser, Conversational service) {
        this.authenticatedUser = authenticatedUser;
        this.service = service;

        // Set the layout
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window

        // Get the current UI
        ui = UI.getCurrent();


        // Build the chat
        newChatButton = new Button("New chat");
        newChatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(newChatButton);

        chat = new VerticalLayout();
        addAndExpand(new Scroller(chat));

        // Add the input component
        messageInput = new MessageInput();
        messageInput.setWidthFull();
        add(messageInput);

        // Add the button click listener

        messageInput.addSubmitListener(e -> {
            String input = e.getValue();
            messageInput.setEnabled(false);

            MarkdownMessage userMessage = createUserMessage(input);
            chat.add(userMessage);

            ask(input);
        });


        // Add the button click listener
        newChatButton.addClickListener(e -> {
            startConversation();
        });

        startConversation();
    }


    private void startConversation() {
        messageInput.setEnabled(true);
        chat.removeAll();
        chatSessionId = UUID.randomUUID().toString();

        MarkdownMessage answer = createIAMessage();
        chat.add(answer);
        String currentUser = authenticatedUser.get().map(user -> user.getUsername()).orElse("User");
        answer.appendMarkdown(service.getWelcomeMessage(currentUser));

    }


    private void ask(String input) {

        // Create a new chat message
        MarkdownMessage answer = createIAMessage();
        chat.add(answer);

        // Disable the input while we are processing the answer
        messageInput.setEnabled(false);

        // Call the conversational service to get the response
        Flux<String> result = service.chat(chatSessionId, input);

        // Process the response
        result.doOnNext(token -> {
            ui.access(() -> {
                answer.appendMarkdownAsync(token);
                answer.setAutoScroll(true);
            });
        }).doOnComplete(() -> {
            ui.access(() -> {
                messageInput.setEnabled(true);
                messageInput.getElement().callJsFunction("focus");
            });
        }).doOnError(er -> {
            er.printStackTrace();
        }).subscribe();


    }


    private MarkdownMessage createIAMessage() {
        MarkdownMessage answer = new MarkdownMessage("IA");
        answer.setAvatarColor(new MarkdownMessage.Color("orange"));
        answer.getElement().setProperty("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm dd-MM-YYYY")));
        return answer;
    }

    private MarkdownMessage createUserMessage(String input) {
        System.out.println("\n\n>>> User Message: \n" + input);
        MarkdownMessage userMessage = new MarkdownMessage(input, "You");
        userMessage.setAvatarColor(new MarkdownMessage.Color("blue"));
        userMessage.getElement().setProperty("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm dd-MM-YYYY")));
        return userMessage;
    }


}
