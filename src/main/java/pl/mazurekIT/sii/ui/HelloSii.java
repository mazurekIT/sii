package pl.mazurekIT.sii.ui;


import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mazurekIT.sii.model.User;
import pl.mazurekIT.sii.service.UserService;

import java.util.ArrayList;
import java.util.List;


@SpringUI
@Title("Hello Sii")
@StyleSheet("vaadin://style.css")
public class HelloSii extends UI {

    Logger logger = LoggerFactory.getLogger("LOGGER INFO");


    @Autowired
    private UserService userService;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);


        //user select
        NativeSelect<String> select =
                new NativeSelect<>("Zaloguj się");

        List<String> userNames = new ArrayList<>();
        for (User x : userService.getAllUsers()) {
            userNames.add(x.getName());
        }

        select.setItems(userNames);

        mainLayout.addComponent(select);


        // conference plan view
        Panel conferencePlan = new Panel("Plan Konferencji");
        conferencePlan.addStyleName("register-panel");
        conferencePlan.setSizeUndefined();
        mainLayout.addComponent(conferencePlan);

        GridLayout gridConferencePlan = new GridLayout(5, 6);
        gridConferencePlan.setSpacing(true);
        gridConferencePlan.addStyleName("grid-plan");
        gridConferencePlan.setSizeUndefined();

        gridConferencePlan.addComponent(new Label("Data"), 0, 0, 0, 1);
        gridConferencePlan.addComponent(new Label("Godzina"), 1, 0, 1, 1);
        gridConferencePlan.addComponent(new Label("Tematy wykładów"), 2, 0, 4, 0);
        gridConferencePlan.addComponent(new Label("Temat A"), 2, 1, 2, 1);
        gridConferencePlan.addComponent(new Label("Temat B"), 3, 1, 3, 1);
        gridConferencePlan.addComponent(new Label("Temat C"), 4, 1, 4, 1);


        gridConferencePlan.addComponent(new Label("1 czerwca"), 0, 2, 0, 3);
        gridConferencePlan.addComponent(new Label("2 czerwca"), 0, 4, 0, 5);

        gridConferencePlan.addComponent(new Label("10:00-11:45"), 1, 2, 1, 2);
        gridConferencePlan.addComponent(new Label("12:00-13:45"), 1, 3, 1, 3);
        gridConferencePlan.addComponent(new Label("10:00-11:45"), 1, 4, 1, 4);
        gridConferencePlan.addComponent(new Label("12:00-13:45"), 1, 5, 1, 5);

        List<String> buttonsNames = new ArrayList<>();
        buttonsNames.add("1-10-A");
        buttonsNames.add("1-10-B");
        buttonsNames.add("1-10-C");
        buttonsNames.add("1-12-A");
        buttonsNames.add("1-12-B");
        buttonsNames.add("1-12-C");
        buttonsNames.add("2-10-A");
        buttonsNames.add("2-10-B");
        buttonsNames.add("2-10-C");
        buttonsNames.add("2-12-A");
        buttonsNames.add("2-12-B");
        buttonsNames.add("2-12-C");

        //TODO add events to all buttons
        for (String button : buttonsNames) {
            gridConferencePlan.addComponent(new Button(button, clickEvent -> {
                String s = String.valueOf(select.getValue());
                if (isSomeoneLogged(s)) {
                    Notification.show("Użytkownik " + s + " zapisał się na wykład");
                    logger.info("zapisano się na wykład " + button);
                    //TODO zapis do pliku wszystkich rejestracji
                } else {
                    Notification.show("Zaloguj się");
                }
            }));
        }


//        gridConferencePlan.addComponent(new Button("1-10-B"));
//        gridConferencePlan.addComponent(new Button("1-10-C"));
//
//        gridConferencePlan.addComponent(new Button("1-12-A"));
//        gridConferencePlan.addComponent(new Button("1-12-B"));
//        gridConferencePlan.addComponent(new Button("1-12-C"));
//
//        gridConferencePlan.addComponent(new Button("2-10-A"));
//        gridConferencePlan.addComponent(new Button("2-10-B"));
//        gridConferencePlan.addComponent(new Button("2-10-C"));
//
//        gridConferencePlan.addComponent(new Button("2-12-A"));
//        gridConferencePlan.addComponent(new Button("2-12-B"));
//        gridConferencePlan.addComponent(new Button("2-12-C"));


        conferencePlan.setContent(gridConferencePlan);
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
        grid.setItems(userService.getAllUsers());
        mainLayout.addComponent(grid);


        btnSubmit.addClickListener(clic -> {


            if (isValid(tfName)) {
                User savedUser = userService.saveUser(new User(tfName.getValue(), tfEmail.getValue()));
                Notification.show("Zapisano użytkownika o ID: " + savedUser.getId());
                logger.info("Zapisano użytkownika z ID: " + savedUser.getId());
                //TODO make write to file a registration message
                grid.setItems(userService.getAllUsers());
            } else {
                Notification.show("Niepoprawne dane");
            }

        });


    }

    private boolean isSomeoneLogged(String s) {
        boolean isLogged = true;
        if (s.equals("null")) {
            isLogged = false;
        }
        return isLogged;
    }

    private boolean isValid(TextField tfName) {
        boolean isValid = true;

        String nameValue = tfName.getValue();
        if (nameValue.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }


}
