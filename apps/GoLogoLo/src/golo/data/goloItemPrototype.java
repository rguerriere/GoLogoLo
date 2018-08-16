package golo.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloItemPrototype implements Cloneable {
    public static final String DEFAULT_NAME = "?";
    public static final String DEFAULT_TYPE = "?";
    
    Node attachedNode;
    final StringProperty name;
    final StringProperty type;
       
    public goloItemPrototype() {
        name = new SimpleStringProperty();
        type = new SimpleStringProperty();
        attachedNode =  null;
    }
    
    public goloItemPrototype(String initName, String initType) {
        this();
        name.set(initName);
        type.set(initType);
        attachedNode = null;
    }
    
    
    public goloItemPrototype(String initName, String initType,Node initNode) {
        this();
        name.set(initName);
        type.set(initType);
        attachedNode = initNode;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String value) {
        type.set(value);
    }
    
    public StringProperty typeProperty() {
        return type;
    }
    
    public Node getNode() {
        return attachedNode;
    }
    
    public void setNode(Node node) {
        attachedNode = node;
    }
    
    public void reset() {
        setName(DEFAULT_NAME);
        setType(DEFAULT_TYPE);
    }

    public Object clone() {
        goloItemPrototype golo = new goloItemPrototype(name.getValue(),type.getValue());
        Node item = (Node)((Drag)attachedNode).clone();
        ((Drag)item).setPrototype(golo);
        golo.setNode(item);
        return golo;
    }
    
    public boolean equals(Object obj) {
        return this == obj;
    }
}