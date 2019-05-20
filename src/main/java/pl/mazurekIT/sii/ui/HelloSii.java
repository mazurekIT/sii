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
        HorizontalLayout loginPanel = new HorizontalLayout();

        NativeSelect<String> select = new NativeSelect<>("Zaloguj się");
        select.setItems(userService.getAllUsersNames());
        loginPanel.addComponent(select);

        TextField emailOfUser = new TextField("Email");
        emailOfUser.setValue("");
        loginPanel.addComponent(emailOfUser);

        Button btnUpdateEmail = new Button("Zapisz zmiany");
        loginPanel.addComponent(btnUpdateEmail);

        mainLayout.addComponent(loginPanel);


        /*
        user reservations
         */
        Grid<Reservation> listOfReservationsGrid = new Grid<>();
        listOfReservationsGrid.setHeight("200");
        listOfReservationsGrid.addColumn(Reservation::getId).setCaption("Id").setWidth(70);
        listOfReservationsGrid.addColumn(Reservation::getCode).setCaption("Rezerwacje użytkownika");
        mainLayout.addComponent(listOfReservationsGrid);

        listOfReservationsGrid.setSelectionMode(Grid.SelectionMode.NONE);



        /*
         conference plan view
         */

        GridLayout gridConferencePlan = new GridLayout(5, 6);
        gridConferencePlan.setHeight("260px");
        gridConferencePlan.setWidth("510px");
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


        mainLayout.addComponent(gridConferencePlan);


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

        /*
        list of all users
         */

        Grid<User> grid = new Grid<>();
        grid.setHeight("300px");
        grid.addColumn(User::getId).setCaption("ID");
        grid.addColumn(User::getName).setCaption("Nazwa");
        grid.addColumn(User::getEmail).setCaption("Email");
        grid.setItems(userService.getAllUsers());
        mainLayout.addComponent(grid);

        /*
        buttons events
         */
        for (Button button : buttonsReservation) {
            gridConferencePlan.addComponent(button);
            String btnCaption = button.getCaption();
            buttonVisibilityAfterFiveReservations(button);

            button.addClickListener(click -> {
                String s = select.getValue();
                if (isSomeoneLogged(s)) {
                    Notification.show("Użytkownik " + s + " zapisał się na wykład");
                    addLogToFile(LocalDateTime.now().toString() + " - " + s + " - zapisano się na wykład " + btnCaption);
                    User user = userService.getUserByName(s);
                    Reservation reservation = new Reservation(btnCaption, user.getId());
                    reservationService.saveReservation(reservation);

                    buttonVisibilityAfterFiveReservations(button);
                    listOfReservationsGrid.setItems(reservationService.getAllReservationsWhereUserId(getUserId(select.getValue())));
                } else {
                    Notification.show("Zaloguj się");
                }
            });


            select.addValueChangeListener(click -> {
                //TODO set disable topic on the same hour after reserved one
                buttonVisibilityAfterReservationInOneHour(button, select.getValue());

                //this is ok
                buttonVisibilityAfterFiveReservations(button);
            });
        }


        listOfReservationsGrid.addItemClickListener(click -> {
            reservationService.deleteReservation(click.getItem().getId());
            Notification.show("Usunięto rezerwację");
            addLogToFile(LocalDateTime.now().toString() + " - " + select.getValue() + " - usunięto rezerwację na wykład " + click.getItem().getCode());
            listOfReservationsGrid.setItems(reservationService.getAllReservationsWhereUserId(getUserId(select.getValue())));

            for (Button button : buttonsReservation) {
                buttonVisibilityAfterFiveReservations(button);
            }
        });


        btnSubmit.addClickListener(click -> {
            if (isValid(tfName.getValue())) {
                User savedUser = userService.saveUser(new User(tfName.getValue(), tfEmail.getValue()));
                Notification.show("Zapisano użytkownika " + savedUser.getName() + " o ID: " + savedUser.getId());
                addLogToFile(LocalDateTime.now().toString() + " - " + savedUser.getName() + " - zapisano użytkownika " + savedUser.getName());
                grid.setItems(userService.getAllUsers());
                select.setItems(userService.getAllUsersNames());
            } else {
                Notification.show("Niepoprawne dane");
            }
        });


        select.addValueChangeListener(click -> {
            String userName = select.getValue();
            User user = userService.getUserByName(userName);
            if (userName.isEmpty()) {
                System.out.println("wybrano puste pole");
            } else {
                emailOfUser.setValue(user.getEmail());
                listOfReservationsGrid.setItems(reservationService.getAllReservationsWhereUserId(getUserId(userName)));
            }
        });


        btnUpdateEmail.addClickListener(click -> {
            User user = userService.getUserByName(select.getValue());
            user.setEmail(emailOfUser.getValue());
            userService.saveUser(user);

            grid.setItems(userService.getAllUsers());

        });

    }

    private void buttonVisibilityAfterReservationInOneHour(Button button, String selectValue) {
        List<Reservation> allReservationsWhereUserId = reservationService.getAllReservationsWhereUserId(getUserId(selectValue));
        List<String> codeTrimName = new ArrayList<>();

        for (Reservation reservation : allReservationsWhereUserId) {
            codeTrimName.add(reservation.getCode().substring(0, 5));
        }
        String buttonSubstring = button.getCaption().substring(0, 5);

        boolean flag = true;
        for (String x : codeTrimName) {
            if (x.equals(buttonSubstring)) {
                flag = false;
            }
        }
        button.setEnabled(flag);
    }

    private void buttonVisibilityAfterFiveReservations(Button button) {
        Long numberOfReservations = reservationService.countReservationByCode(button.getCaption());
        if (numberOfReservations > 4L) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    private Long getUserId(String userName) {
        User userByName = userService.getUserByName(userName);
        return userByName.getId();
    }

    private boolean isSomeoneLogged(String s) {
        boolean isLogged = true;
        if (s.equals("null")) {
            isLogged = false;
        }
        return isLogged;
    }

    private boolean isValid(String tfName) {
        boolean isValid = true;
        if (tfName.isEmpty()) {
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
