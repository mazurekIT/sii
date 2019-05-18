package pl.mazurekIT.sii.ui;


import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mazurekIT.sii.model.User;
import pl.mazurekIT.sii.service.UserService;


@SpringUI
@Title("Hello Sii")
public class HelloSii extends UI {

    Logger logger = LoggerFactory.getLogger("Hello Sii");

    @Autowired
    private UserService userService;




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

        Grid<User> grid = new Grid<>();
        grid.addColumn(User::getId).setCaption("ID");
        grid.addColumn(User::getName).setCaption("Nazwa");
        grid.addColumn(User::getEmail).setCaption("Email");


        btnSubmit.addClickListener(clic -> {
            if (isValid(tfName)){
                User savedUser = userService.saveUser(new User(tfName.getValue(), tfEmail.getValue()));
                Notification.show("Zapisano użytkownika o ID: "+ savedUser.getId());
                logger.info("Zapisano użytkownika z ID: ",savedUser.getId());
                //TODO make write to file a registration message
                grid.setItems(userService.getAllUsers());
            } else {
                Notification.show("Niepoprawne dane");
            }
        });

        mainLayout.addComponent(grid);
    }

    private boolean isValid(TextField tfName) {
        boolean isValid=true;

        String nameValue = tfName.getValue();
        if(nameValue.isEmpty()){
            isValid = false;
        }


        //TODO make validation email

        return isValid;
    }


    private Label addLabel(String text, String className) {
        Label label = new Label(text);
        label.setStyleName(className);
        return label;
    }

}
