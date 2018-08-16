package djf.components;

/**
 * This interface provides the structure for clipboard components in
 * our applications.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public interface AppClipboardComponent {
    public void cut();
    public void copy();    
    public void paste();
    public boolean hasSomethingToCut();
    public boolean hasSomethingToCopy();
    public boolean hasSomethingToPaste();
}
