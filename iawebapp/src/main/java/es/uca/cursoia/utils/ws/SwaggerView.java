package es.uca.cursoia.utils.ws;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;


@PageTitle("Servicios web")
@Route(value = "ws", layout = MainLayout.class)
public class SwaggerView extends VerticalLayout {

    public SwaggerView() {
        IFrame iFrame = new IFrame("./swagger-ui.html");
        iFrame.setSizeFull();
        add(iFrame);
        this.setSizeFull();
    }

}
