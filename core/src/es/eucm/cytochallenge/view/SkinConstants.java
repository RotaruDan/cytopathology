package es.eucm.cytochallenge.view;

import com.badlogic.gdx.graphics.Color;

/**
 * Class with constant for icons
 */
public interface SkinConstants {


    /**
     * This represents 0.76 cm (48px) in MDPI, the size for the buttons that we
     * want in all devices.
     */
    float UNIT_SIZE = 48;
    String DEFAULT_FONT = "Roboto-Regular-24";


    /**
     * Images
     */

    // Other
    String DRAWABLE_BG48 = "bg48";
    String DRAWABLE_BLANK = "blank";
    String DRAWABLE_TOUCH = "touch";
    String DRAWABLE_CLOCK = "clock";
    String DRAWABLE_LOGO = "logo";
    String DRAWABLE_CLOCK_AXIS = "clock_axis";
    String DRAWABLE_BOOKS_LEFT = "books_left";
    String DRAWABLE_BOOKS_RIGHT = "books_right";
    String DRAWABLE_DESK = "desk";
    String DRAWABLE_DOCTOR_BOARD = "doctor_board";
    String DRAWABLE_BOARD = "board";
    String DRAWABLE_STAR = "star";
    String DRAWABLE_SMALL_STAR = "small_star";

    // Buttons
    String DRAWABLE_BUTTON_CHECK_OFF = "button_check_off";
    String DRAWABLE_BUTTON_CHECK_OFF_FOCUSED = "button_check_off_focused";
    String DRAWABLE_BUTTON_CHECK_ON = "button_check_on";
    String DRAWABLE_BUTTON_CHECK_ON_FOCUSED = "button_check_on_focused";
    String DRAWABLE_BUTTON_RADIO_OFF = "button_radio_off";
    String DRAWABLE_BUTTON_RADIO_OFF_FOCUSED = "button_radio_off_focused";
    String DRAWABLE_BUTTON_RADIO_ON = "button_radio_on";
    String DRAWABLE_CIRCLE = "circle";


    /**
     * 9 Patches
     */

    // Other
    String DRAWABLE_9P_BLANK75 = "blank75";
    String DRAWABLE_9P_BLANK_GREY_BORDER = "blank_grey_border";
    String DRAWABLE_9P_PAGE_RIGHT = "page_right";
    String DRAWABLE_9P_PAGE_UP = "page_up";
    String DRAWABLE_9P_TOOLBAR = "toolbar";

    // Buttons
    String DRAWABLE_9P_BUTTON = "button";
    String DRAWABLE_9P_BUTTON_DOWN = "button_down";
    String DRAWABLE_9P_BUTTON_UP = "button_up";

    /**
     * Icons
     */

    String IC_ADD = "ic_add";
    String IC_ARROW = "ic_arrow";
    String IC_CHECK = "ic_check";
    String IC_CIRCLE = "ic_circle";
    String IC_CLOSE = "ic_close";
    String IC_CONVERSATION = "ic_conversation";
    String IC_ERROR = "ic_error";
    String IC_SETTINGS = "ic_settings";
    String IC_PLAY = "ic_play";
    String IC_PLAY_BIG = "ic_play_big";
    String IC_CELL_WITH_RECEPTORS = "ic_cell_with_receptors";
    String IC_REPLAY = "ic_replay";
    String IC_FIT = "ic_fit";
    String IC_UNDO = "ic_undo";
    String IC_UPP = "ic_upp";
    String IC_MCQ = "ic_mcq";
    String IC_FTB = "ic_ftb";
    String IC_DND = "ic_dnd";
    String IC_MICQ = "ic_micq";
    String IC_TABLET = "ic_tablet";
    String IC_DIFFICULTY = "ic_difficulty";
    String IC_MAPMARKER = "map-marker";


    /**
     * TODO clean these
     */
    String STYLE_DEFAULT = "default";
    String STYLE_CONTEXT = "context";
    String STYLE_EDITION = "edition";
    String STYLE_COMPONENT = "component";
    String STYLE_TOOLBAR = "toolbar";
    String STYLE_DROP_DOWN = "drop_down";
    String STYLE_ADD = "add";
    String STYLE_SCENE = "scene";
    String STYLE_CIRCLE = "circle";
    String STYLE_CELL_WITH_RECEPTORS = "cell_with_receptors";
    String STYLE_SECONDARY_CIRCLE = "secondary_circle";
    String STYLE_MULTIPLE_CHOICE = "multiple_choice";
    String STYLE_NAVIGATION_SCENE = "navigation_scene";
    String STYLE_NAVIGATION = "navigation";
    String STYLE_SLIDER_PAGES = "pages";
    String STYLE_CHECK = "check";
    String STYLE_GRAY = "gray";
    String STYLE_MARKER = "marker";
    String STYLE_TOAST = "toast";
    String STYLE_RADIO_CHECKBOX = "default_radio";
    String STYLE_CONTEXT_RADIO = "context_radio";
    String STYLE_PERFORMANCE = "performance";
    String STYLE_SELECTION = "selection";
    String STYLE_DIALOG = "dialog";
    String STYLE_ORANGE = "orange";
    String STYLE_TEMPLATE = "template";
    String STYLE_CATEGORY = "category";
    String STYLE_BIG = "big";
    String STYLE_DRAGANDDROP = "drag-n-drop";
    String STYLE_CHALLENGELIST = "challenge_list";
    String STYLE_COURSE = "course";

    /**
     *  Colors
     */
    Color COLOR_BACKGROUND = new Color(94f/255f, 190f/255f, 214f/255f, 1f);
    Color COLOR_BACKGROUND_LAB = new Color(161f/255f, 181f/255f, 188f/255f, 1f);
    Color COLOR_PANEL_RIGHT = new Color(100f/255f, 200f/255f, 235f/255f, 1f);
    Color COLOR_BUTTON = new Color(0.050980392f, 0.35682745f, 0.549019607f, 1f);
    Color COLOR_BUTTON_CLEAR = new Color(63f/255f, 145f/255f, 195f/255f, 1f);
    Color COLOR_TOOLBAR_TOP = COLOR_PANEL_RIGHT;
}