package golo.workspace.style;


/**
 * This class lists all CSS style types for this application. These
 * are used by JavaFX to apply style properties to controls like
 * buttons, labels, and panes.

 * @author Richard McKenna
 * @author Richie
 * @version 1.0
 */
public class goloStyle {
    public static final String EMPTY_TEXT = "";
    public static final int BUTTON_TAG_WIDTH = 75;

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS M3Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    // NOTE THAT FOUR CLASS STYLES ALREADY EXIST:
    // top_toolbar, toolbar, toolbar_text_button, toolbar_icon_button
    
    public static final String CLASS_GOLO_PANE          = "golo_pane";
    public static final String CLASS_GOLO_BOX           = "golo_box";            
    public static final String CLASS_GOLO_BIG_HEADER    = "golo_big_header";
    public static final String CLASS_GOLO_SMALL_HEADER  = "golo_small_header";
    public static final String CLASS_GOLO_PROMPT        = "golo_prompt";
    public static final String CLASS_GOLO_TEXT_FIELD    = "golo_text_field";
    public static final String CLASS_GOLO_BUTTON        = "golo_button";
    public static final String CLASS_GOLO_TABLE         = "golo_table";
    public static final String CLASS_GOLO_COLUMN        = "golo_column";
    public static final String CLASS_GOLO_LIST          = "golo_list";
    public static final String CLASS_GOLO_LIST_BUTTONS  = "golo_list_buttons";
    public static final String CLASS_GOLO_EDITTOOLBAR  = "golo_editToolbar";
    public static final String CLASS_GOLO_COMBOBOX  = "golo_combobox";
    // STYLE CLASSES FOR THE ADD/EDIT ITEM DIALOG
    public static final String CLASS_GOLO_DIALOG_GRID           = "golo_dialog_grid";
    public static final String CLASS_GOLO_DIALOG_HEADER         = "golo_dialog_header";
    public static final String CLASS_GOLO_DIALOG_PROMPT         = "golo_dialog_prompt";
    public static final String CLASS_GOLO_DIALOG_TEXT_FIELD     = "golo_dialog_text_field";
    public static final String CLASS_GOLO_DIALOG_DATE_PICKER    = "golo_dialog_date_picker";
    public static final String CLASS_GOLO_DIALOG_CHECK_BOX      = "golo_dialog_check_box";
    public static final String CLASS_GOLO_DIALOG_BUTTON         = "golo_dialog_button";
    public static final String CLASS_GOLO_DIALOG_PANE           = "golo_dialog_pane";
}