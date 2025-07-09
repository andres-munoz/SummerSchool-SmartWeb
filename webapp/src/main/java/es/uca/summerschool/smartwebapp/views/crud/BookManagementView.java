package es.uca.summerschool.smartwebapp.views.crud;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import es.uca.summerschool.smartwebapp.data.Book;
import es.uca.summerschool.smartwebapp.services.ai.BookExtractorService;
import es.uca.summerschool.smartwebapp.services.ai.ImageDescriptorService;
import es.uca.summerschool.smartwebapp.services.ai.TextHandlerService;
import es.uca.summerschool.smartwebapp.services.crud.BookService;
import jakarta.validation.constraints.NotNull;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;

@PageTitle("CRUD + AI")
@Route("master-detail/:sampleBookID?/:action?(edit)")
@Menu(order = 5, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@AnonymousAllowed
public class BookManagementView extends Div implements BeforeEnterObserver {

    private final String SAMPLEBOOK_ID = "sampleBookID";
    private final String SAMPLEBOOK_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<Book> grid = new Grid<>(Book.class, false);
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final BeanValidationBinder<Book> binder;
    private final BookService sampleBookService;
    private final BookExtractorService bookExtractorService;
    private final TextHandlerService textHandlerService;
    private final ImageDescriptorService imageDescriptorService;
    private Upload image;
    private Image imagePreview;
    private TextField name;
    private TextField author;
    private DatePicker publicationDate;
    private IntegerField pages;
    private TextField isbn;
    private TextArea description;
    private TextArea reviews;
    private Book sampleBook;

    public BookManagementView(BookService sampleBookService, BookExtractorService bookExtractorService, TextHandlerService textHandlerService, ImageDescriptorService imageDescriptorService) {
        this.sampleBookService = sampleBookService;
        this.bookExtractorService = bookExtractorService;
        this.textHandlerService = textHandlerService;
        this.imageDescriptorService = imageDescriptorService;
        addClassNames("book-management-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        LitRenderer<Book> imageRenderer = LitRenderer
                .<Book>of("<img style='height: 64px' src=${item.image} />").withProperty("image", item -> {
                    if (item != null && item.getImage() != null) {
                        return "data:image;base64," + Base64.getEncoder().encodeToString(item.getImage());
                    } else {
                        return "";
                    }
                });
        grid.addColumn(imageRenderer).setHeader("Image").setWidth("68px").setFlexGrow(0);

        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("author").setAutoWidth(true);
        grid.addColumn("publicationDate").setAutoWidth(true);
        grid.addColumn("pages").setAutoWidth(true);
        grid.addColumn("isbn").setAutoWidth(true);
        grid.setItems(query -> sampleBookService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEBOOK_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(BookManagementView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Book.class);


        binder.bindInstanceFields(this);

        attachImageUpload(image, imagePreview);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.sampleBook == null) {
                    this.sampleBook = new Book();
                }
                binder.writeBean(this.sampleBook);
                sampleBookService.save(this.sampleBook);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(BookManagementView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> sampleBookId = event.getRouteParameters().get(SAMPLEBOOK_ID).map(Long::parseLong);
        if (sampleBookId.isPresent()) {
            Optional<Book> sampleBookFromBackend = sampleBookService.get(sampleBookId.get());
            if (sampleBookFromBackend.isPresent()) {
                populateForm(sampleBookFromBackend.get());
            } else {
                Notification.show(String.format("The requested sampleBook was not found, ID = %s", sampleBookId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(BookManagementView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        NativeLabel imageLabel = new NativeLabel("Image");
        imagePreview = new Image();
        imagePreview.setWidth("100%");
        image = new Upload();
        image.getStyle().set("box-sizing", "border-box");
        image.getElement().appendChild(imagePreview.getElement());
        name = new TextField("Name");
        author = new TextField("Author");
        publicationDate = new DatePicker("Publication Date");
        pages = new IntegerField("Pages");
        isbn = new TextField("Isbn");
        description = new TextArea("Description");
        reviews = new TextArea("Reviews");


        MenuBar menuBar = new MenuBar();
        MenuItem magic = menuBar.addItem("Magic");
        SubMenu magicSubMenu = magic.getSubMenu();

        magicSubMenu.addItem("Describe image", e -> {
            describeImage();
        });

        magicSubMenu.addItem("Translate description", e1 -> {
            openTranslationDialog();
        });

        magicSubMenu.addItem("Summarize description", e1 -> {
            summarizeText();
        });

        magicSubMenu.addItem("Analyse review", e -> {
            analyseSentiment();
        });

        magicSubMenu.addItem("Extract data from text", e -> {
            openExtractionDialog();
        });


        formLayout.add(imageLabel, image, name, author, publicationDate, pages, isbn, menuBar, description, reviews);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void describeImage() {

        if (this.sampleBook != null && this.sampleBook.getImage() != null) {
            String imageDescription = imageDescriptorService.describe(this.sampleBook.getImage());

            Notification notification = new Notification();
            notification.setDuration(5000);
            notification.setPosition(Notification.Position.MIDDLE);

            if (imageDescription != null && !imageDescription.isEmpty()) {
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setText("Image description: " + imageDescription);
            } else {
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setText("No description could be generated for the image.");
            }
            notification.open();


        } else {
            Notification.show("Please upload an image to describe.");
        }


    }

    private void analyseSentiment() {
        String text = this.reviews.getValue();
        if (text != null && !text.isEmpty()) {
            TextHandlerService.Sentiment sentiment = textHandlerService.analyzeSentimentOf(text);

            Notification notification = new Notification();
            notification.setDuration(5000);
            notification.setPosition(Notification.Position.MIDDLE);
            switch (sentiment) {
                case POSITIVE:
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setText("Sentiment analysis result: Positive :-)");
                    break;
                case NEUTRAL:
                    notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                    notification.setText("Sentiment analysis result: Neutral :|");
                    break;
                case NEGATIVE:
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setText("Sentiment analysis result: Negative :(");
                    break;
            }
            notification.open();
        } else {
            Notification.show("Please enter a valid text for sentiment analysis.");
        }
    }

    private void summarizeText() {

        String text = this.description.getValue();
        if (text != null && !text.isEmpty()) {
            String summary = textHandlerService.summarize(text, 20);
            this.sampleBook.setDescription(summary);
            populateForm(this.sampleBook);
        } else {
            Notification.show("Please enter a valid book description.");
        }

    }

    private void openTranslationDialog() {
        // Open a dialog showing a selector of the language among the available ones (en, es, fr, de)
        Select<String> languageSelector = new Select<>();
        languageSelector.setLabel("Select Language");
        languageSelector.setItems("en", "es", "fr", "de");
        languageSelector.setValue("es"); // Default to Spanish
        Dialog dialog = new Dialog();
        dialog.add(languageSelector);
        Button translateButton = new Button("Translate", event -> {
            String selectedLanguage = languageSelector.getValue();
            String textToTranslate = this.description.getValue();
            if (this.sampleBook != null && textToTranslate != null) {
                String translatedText = textHandlerService.translate(textToTranslate, selectedLanguage);
                this.sampleBook.setDescription(translatedText);
                populateForm(this.sampleBook);
                dialog.close();
            } else {
                Notification.show("Please enter a valid book description.");
            }
        });
        dialog.add(translateButton);
        dialog.setWidth("400px");
        dialog.open();
        translateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


    }

    @NotNull
    private Dialog openExtractionDialog() {
        // Open a dialog to enter text
        TextArea textField = new TextArea("Enter book description");
        textField.setWidth("100%");
        Dialog dialog = new Dialog();
        dialog.add(textField);
        Button extractButton = new Button("Extract", event -> {
            String text = textField.getValue();
            if (text != null && !text.isEmpty()) {
                Book extractedBook = bookExtractorService.extractBookFrom(text);
                populateForm(extractedBook);
                dialog.close();
            } else {
                Notification.show("Please enter a valid book description.");
            }
        });
        dialog.add(extractButton);
        dialog.setWidth("600px");
        dialog.setHeight("400px");
        dialog.open();
        extractButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return dialog;
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void attachImageUpload(Upload upload, Image preview) {
        ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();
        upload.setAcceptedFileTypes("image/*");
        upload.setReceiver((fileName, mimeType) -> {
            uploadBuffer.reset();
            return uploadBuffer;
        });
        upload.addSucceededListener(e -> {
            StreamResource resource = new StreamResource(e.getFileName(),
                    () -> new ByteArrayInputStream(uploadBuffer.toByteArray()));
            preview.setSrc(resource);
            preview.setVisible(true);
            if (this.sampleBook == null) {
                this.sampleBook = new Book();
            }
            this.sampleBook.setImage(uploadBuffer.toByteArray());
        });
        preview.setVisible(false);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Book value) {
        this.sampleBook = value;
        binder.readBean(this.sampleBook);
        this.imagePreview.setVisible(value != null);
        if (value == null || value.getImage() == null) {
            this.image.clearFileList();
            this.imagePreview.setSrc("");
        } else {
            this.imagePreview.setSrc("data:image;base64," + Base64.getEncoder().encodeToString(value.getImage()));
        }

    }
}
