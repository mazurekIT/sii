package pl.mazurekIT.sii.ui;


import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


@SpringUI
@Title("Hello Sii")
public class HelloSii extends UI {

    Logger logger = LoggerFactory.getLogger("Hello Sii");


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);


        // conference plan view
        Panel conferencePlan = new Panel("Plan Konferencji");
        conferencePlan.addStyleName("register-panel");
        conferencePlan.setSizeUndefined();
        mainLayout.addComponent(conferencePlan);


        // registration panel with save to DB
        Panel registrationPanel = new Panel("Rejestracja");
        registrationPanel.addStyleName("register-panel");
        registrationPanel.setSizeUndefined();
        mainLayout.addComponent(registrationPanel);
        // Create the content
        FormLayout registrationForm = new FormLayout();
        registrationForm.addStyleName("register-form");
        TextField tfName = new TextField("Nazwa użytkownika");
        tfName.setRequiredIndicatorVisible(true);
        tfName.setMaxLength(40);

        TextField tfEmail = new TextField("Email");
        tfEmail.setRequiredIndicatorVisible(true);

        Button btnSubmit = new Button("Zapisz");

        registrationForm.addComponent(tfName);
        registrationForm.addComponent(tfEmail);
        registrationForm.addComponent(btnSubmit);

        registrationPanel.setContent(registrationForm);





    }



    private Label addLabel(String text, String className) {
        Label label = new Label(text);
        label.setStyleName(className);
        return label;
    }

}
