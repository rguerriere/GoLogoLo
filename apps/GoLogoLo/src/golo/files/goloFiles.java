package golo.files;

import static djf.AppPropertyType.APP_EXPORT_PAGE;
import static djf.AppPropertyType.APP_PATH_EXPORT;
import static djf.AppPropertyType.EXPORT_TITLE;
import static djf.AppPropertyType.GOLO_CANVAS_PANE;
import static djf.AppTemplate.PATH_WORK;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import golo.data.Drag;
import golo.data.DragRectangle;
import golo.data.DragText;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import golo.data.goloData;
import golo.data.goloItemPrototype;
import java.awt.image.RenderedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 * @author Richie
 */
public class goloFiles implements AppFileComponent {
    // FOR JSON SAVING AND LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_COMPONENTS = "shapes";
    static final String JSON_COMPONENT = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_NAME = "name";
    static final String JSON_IMAGE = "image";
    static final String JSON_TEXT = "text";
    static final String JSON_TEXT_FONT = "font";
    static final String JSON_TEXT_SIZE = "size";
    static final String JSON_TEXT_BOLD = "bold";
    static final String JSON_TEXT_ITALIC = "italic";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    // FOR EXPORTING TO HTML
    static final String TABLE_DATA_TAG = "gologolo_table_data";

    /**
     * This method is for saving user work.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
     public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	goloData toDoData = (goloData)data;	
        
	// NOW BUILD THE JSON ARRAY FOR THE LIST
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Iterator<goloItemPrototype> itemsIt = toDoData.itemsIterator();
	while (itemsIt.hasNext()) {	
            goloItemPrototype item = itemsIt.next();
            Node component = item.getNode();
	    JsonObject itemJson; 
            if(component instanceof Rectangle){
            Rectangle rec = (Rectangle)component;
            itemJson = (JsonObject) Json.createObjectBuilder()
                    .add(JSON_NAME, item.getName())
                    .add(JSON_TYPE, item.getType())
                    .add(JSON_X, rec.getX())
                    .add(JSON_Y, rec.getY())
                    .add(JSON_WIDTH, rec.getWidth())
                    .add(JSON_HEIGHT, rec.getHeight())
                    .build();
	    arrayBuilder.add(itemJson);
            }
            else if(component instanceof Text){
            Text text = (Text)component;
            itemJson = (JsonObject) Json.createObjectBuilder()
                    .add(JSON_NAME, item.getName())
                    .add(JSON_TYPE, item.getType())
                    .add(JSON_X, text.getX())
                    .add(JSON_Y, text.getY())
                    .add(JSON_TEXT,text.getText())
                    .build();
	    arrayBuilder.add(itemJson);
            }
	}
	JsonArray itemsArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject toDoDataJSO = Json.createObjectBuilder()
		.add(JSON_COMPONENTS, itemsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(toDoDataJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(toDoDataJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error
     * reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException 
    {
	
        // CLEAR THE OLD DATA OUT
	goloData goloData = (goloData)data;
	goloData.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// AND NOW LOAD ALL THE ITEMS
	JsonArray jsonItemArray = json.getJsonArray(JSON_COMPONENTS);
	for (int i = 0; i < jsonItemArray.size(); i++) 
        {
	    JsonObject jsonItem = jsonItemArray.getJsonObject(i);
	    goloItemPrototype item = loadItem(jsonItem);
	    goloData.addItem(item);
            ((Drag)item.getNode()).setPrototype(item);
            goloData.addComponentAt(item.getNode(),0);
        }
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    public goloItemPrototype loadItem(JsonObject jsonItem) {
	// GET THE DATA
        String name = jsonItem.getString(JSON_NAME);
	String type = jsonItem.getString(JSON_TYPE);
        goloItemPrototype item = new goloItemPrototype(name, type);
        if(type.equals("Rectangle")){
            DragRectangle rec = new DragRectangle();
            rec.setX(getDataAsDouble(jsonItem, JSON_X));
            rec.setY(getDataAsDouble(jsonItem, JSON_Y));
            rec.setWidth(getDataAsDouble(jsonItem, JSON_WIDTH));
            rec.setHeight(getDataAsDouble(jsonItem, JSON_HEIGHT));
            item.setNode(rec);
        }
        else if(type.equals("Text")){
            DragText text = new DragText();
            text.setX(getDataAsDouble(jsonItem, JSON_X));
            text.setY(getDataAsDouble(jsonItem, JSON_Y));
            text.setText(jsonItem.getString(JSON_TEXT));
            item.setNode(text);
            item.setName(jsonItem.getString(JSON_TEXT));
        }
	// ALL DONE, RETURN IT
	return item;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method would be used to export data to another format,
     * which we're not doing in this assignment.
     
    @Override
    public void exportData(AppDataComponent data, String savedFileName) throws IOException {
        String toDoListName = savedFileName.substring(0, savedFileName.indexOf("."));
        String fileToExport = toDoListName + ".html";
        try {
            // GET THE ACTUAL DATA
            goloData toDoData = (goloData)data;
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String exportDirPath = props.getProperty(APP_PATH_EXPORT) + toDoListName + "/";
            File exportDir = new File(exportDirPath);
            if (!exportDir.exists()) {
                exportDir.mkdir();
            }

            // NOW LOAD THE TEMPLATE DOCUMENT
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            String htmlTemplatePath = props.getPropertiesDataPath() + props.getProperty(TDLM_EXPORT_TEMPLATE_FILE_NAME);
            File file = new File(htmlTemplatePath);
            System.out.println(file.getPath() + " exists? " + file.exists());
            URL templateURL = file.toURI().toURL();
            Document exportDoc = docBuilder.parse(templateURL.getPath());

            // SET THE WEB PAGE TITLE
            Node titleNode = exportDoc.getElementsByTagName(TITLE_TAG).item(0);
            titleNode.setTextContent("No Name List");

            // SET THE OWNER
            Node ownerNode = getNodeWithId(exportDoc, HTML.Tag.TD.toString(), OWNER_TAG);
            ownerNode.setTextContent(toDoData.getOwner());
            
            // ADD ALL THE ITEMS
            Node tDataNode = getNodeWithId(exportDoc, "tdata", TABLE_DATA_TAG);
            Iterator<goloItemPrototype> itemsIt = toDoData.itemsIterator();
            while (itemsIt.hasNext()) {
                goloItemPrototype item = itemsIt.next();
                Element trElement = exportDoc.createElement(HTML.Tag.TR.toString());
                tDataNode.appendChild(trElement);
                addCellToRow(exportDoc, trElement, item.getCategory());
                addCellToRow(exportDoc, trElement, item.getDescription());
                addCellToRow(exportDoc, trElement, item.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                addCellToRow(exportDoc, trElement, "" + item.isCompleted());
            }
            
            // CORRECT THE APP EXPORT PAGE
            props.addProperty(APP_EXPORT_PAGE, exportDirPath + fileToExport);

            // EXPORT THE WEB PAGE
            saveDocument(exportDoc, props.getProperty(APP_EXPORT_PAGE));
        }
        catch(SAXException | ParserConfigurationException
                | TransformerException exc) {
            throw new IOException("Error loading " + fileToExport);
        }
    }
    private void addCellToRow(Document doc, Node rowNode, String text) {
        Element tdElement = doc.createElement(HTML.Tag.TD.toString());
        tdElement.setTextContent(text);
        rowNode.appendChild(tdElement);
    }
    * 
    private Node getNodeWithId(Document doc, String tagType, String searchID) {
        NodeList testNodes = doc.getElementsByTagName(tagType);
        for (int i = 0; i < testNodes.getLength(); i++) {
            Node testNode = testNodes.item(i);
            Node testAttr = testNode.getAttributes().getNamedItem(HTML.Attribute.ID.toString());
            if ((testAttr != null) && testAttr.getNodeValue().equals(searchID)) {
                return testNode;
            }
        }
        return null;
    }
    */
    private void saveDocument(Document doc, String outputFilePath)
            throws TransformerException, TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Result result = new StreamResult(new File(outputFilePath));
        Source source = new DOMSource(doc);
        transformer.transform(source, result);
    }

    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        
    }

    @Override
    public void exportData(AppDataComponent dataManager, String filePath) throws IOException {
        goloData data = (goloData) dataManager;
        PropertiesManager propertyManager = PropertiesManager.getPropertiesManager();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(filePath+".png");
        fileChooser.setTitle(propertyManager.getProperty(EXPORT_TITLE));
        fileChooser.setInitialDirectory(new File(propertyManager.getProperty(APP_PATH_EXPORT)));
        Pane canvas = (Pane)data.getapp().getGUIModule().getGUINode(GOLO_CANVAS_PANE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                //Pad the capture area
                WritableImage image =  canvas.snapshot(new SnapshotParameters(), null);
                //Write the snapshot to the chosen file
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {}
        } 
    }
}
