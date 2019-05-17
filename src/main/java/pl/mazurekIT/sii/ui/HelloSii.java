package pl.mazurekIT.sii.ui;


import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SpringUI
@Title("Hello Sii")
public class HelloSii extends UI {


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();

        mainLayout.addComponent(addLabel("Plan Konferencji", "main-label"));

        registrationUserForm();


        setContent(mainLayout);


    }

    private void registrationUserForm() {
        new Panel("Registration Panel");

    }


    private Label addLabel(String text, String className) {
        Label label = new Label(text);
        label.setStyleName(className);
        return label;
    }

}
