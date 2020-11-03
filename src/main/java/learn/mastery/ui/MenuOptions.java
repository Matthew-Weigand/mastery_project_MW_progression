package learn.mastery.ui;

public enum MenuOptions {

    EXIT(0, "Exit", false),
    VIEW_RESERVATIONS_BY_HOST(1, "View Reservations for Host", false),
    MAKE_RESERVATION(2, "Make a Reservation", false),
    EDIT_RESERVATION(3, "Edit a Reservation", false),
    CANCEL_RESERVATION(4, "Cancel a Reservation", false);

    private int value;
    private String message;
    private boolean hidden;


    MenuOptions(int value, String message, boolean hidden) {
        this.value = value;
        this.message = message;
        this.hidden = hidden;
    }

    public static MenuOptions fromValue(int value) {
        for (MenuOptions option : MenuOptions.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHidden() {
        return hidden;
    }
}