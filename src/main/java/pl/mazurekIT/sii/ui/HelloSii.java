package pl.mazurekIT.sii.ui;


import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mazurekIT.sii.model.Reservation;
import pl.mazurekIT.sii.model.User;
import pl.mazurekIT.sii.service.ReservationService;
import pl.mazurekIT.sii.service.UserService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringUI
@Title("Hello Sii")
@StyleSheet("vaadin://style.css")
public class HelloSii extends UI {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();
        setContent(mainLayout);

        /*
        user select and update email - login panel
         */
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        NativeSelect<String> select =
                new NativeSelect<>("Zaloguj się");
        List<String> userNames = new ArrayList<>();
        for (User x : userService.getAllUsers()) {
            userNames.add(x.getName());
        }
        select.setItems(userNames);
        horizontalLayout.addComponent(select);


        TextField emailOfUser = new TextField("Email");
        emailOfUser.setValue("");
        horizontalLayout.addComponent(emailOfUser);

        Button btnUpdateEmail = new Button("Zapisz zmiany");
        horizontalLayout.addComponent(btnUpdateEmail);

        mainLayout.addComponent(horizontalLayout);


        /*
         conference plan view
         */
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


        List<Button> buttonsReservation = new ArrayList<>();
        buttonsReservation.add(new Button("1-10-A"));
        buttonsReservation.add(new Button("1-10-B"));
        buttonsReservation.add(new Button("1-10-C"));
        buttonsReservation.add(new Button("1-12-A"));
        buttonsReservation.add(new Button("1-12-B"));
        buttonsReservation.add(new Button("1-12-C"));
        buttonsReservation.add(new Button("2-10-A"));
        buttonsReservation.add(new Button("2-10-B"));
        buttonsReservation.add(new Button("2-10-C"));
        buttonsReservation.add(new Button("2-12-A"));
        buttonsReservation.add(new Button("2-12-B"));
        buttonsReservation.add(new Button("2-12-C"));


        //TODO set visibility after 5 reservations
        //TODO set disable topic on the same hour after reserved one
        for (Button button : buttonsReservation) {
            gridConferencePlan.addComponent(button);

            button.addClickListener(click -> {
                String s = select.getValue();
                String btnCaption = button.getCaption();
                if (isSomeoneLogged(s)) {
                    Notification.show("Użytkownik " + s + " zapisał się na wykład");
                    addLogToFile(LocalDateTime.now().toString() + " - " + s + " - zapisano się na wykład " + btnCaption );
                    User user = userService.getUserByName(s);
                    Reservation reservation = new Reservation(btnCaption, user.getId());
                    reservationService.saveReservation(reservation);
                } else {
                    Notification.show("Zaloguj się");
                }
            });
        }


        conferencePlan.setContent(gridConferencePlan);
        mainLayout.addComponent(conferencePlan);


        /*
        registration panel with save to DB
         */
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
//
//        Grid<Reservation> gridReser = new Grid<>();
//        gridReser.addColumn(Reservation::getId).setCaption("ID");
//        gridReser.addColumn(Reservation::getCode).setCaption("Code");
//        gridReser.addColumn(Reservation::getUserId).setCaption("userid");
//        gridReser.setItems(reservationService.getAllReservations());
//        mainLayout.addComponent(gridReser);
        /*
        buttons events
         */

        btnSubmit.addClickListener(click -> {
            if (isValid(tfName)) {
                User savedUser = userService.saveUser(new User(tfName.getValue(), tfEmail.getValue()));
                Notification.show("Zapisano użytkownika " + savedUser.getName() + " o ID: " + savedUser.getId());
                //TODO make write to file a registration message
                addLogToFile(LocalDateTime.now().toString() + " - " + savedUser.getName() + " - zapisano użytkownika " + savedUser.getName());
                grid.setItems(userService.getAllUsers());
            } else {
                Notification.show("Niepoprawne dane");
            }

        });

        select.addValueChangeListener(click -> {
            String userName = select.getValue();
            User user = userService.getUserByName(userName);
            emailOfUser.setValue(user.getEmail());
        });

        btnUpdateEmail.addClickListener(click -> {
            User user = userService.getUserByName(select.getValue());
            user.setEmail(emailOfUser.getValue());
            userService.saveUser(user);

            grid.setItems(userService.getAllUsers());

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


    private void addLogToFile(String logg) {
        try {
            FileWriter saveLog = new FileWriter("powiadomienia.txt", true);
            saveLog.append(logg);
            saveLog.append(System.getProperty("line.separator"));
            saveLog.close();
        } catch (IOException ex) {
            System.out.println("Bład zapisu");
        }
    }
}
