package util.converter;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * 
 * @author Nikola
 * Convert XML->Object from source and validate using a schema
 * @param <T> class that is being unmarshalled
 */
public class GenericXWSUnmarshaller<T> {
	
	protected JAXBContext context=null;
	protected final String MODEL_PATH="xws-model/xml/xsd/";
	protected String namespace;
	protected String fileName;
	protected InputStream in;
	protected T type;
	
	/**
	 * Constructor
	 * @param type class type
	 * @param in InputStream toward the source
	 */
	public GenericXWSUnmarshaller(T type, InputStream in) {
		try {
			this.namespace=type.getClass().getPackage().getName();
			this.context=JAXBContext.newInstance(namespace);
			this.type=type;
			this.in=in;
			
			//get the name of the youngest child in the package tree
			String fullPackageName = type.getClass().getPackage().getName();
			this.fileName = fullPackageName.substring(fullPackageName.lastIndexOf(".")+1);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts XML->Object
	 * @return converted object
	 * @throws SAXException 
	 * @throws JAXBException 
	 */
	public T unmarshall() throws JAXBException, SAXException {
		return unmarshall(in);
	}
	
	/**
	 * Converts XML->Object deserializing it from a source
	 * @param in InputStream toward the source
	 * @return
	 * @throws JAXBException 
	 * @throws SAXException 
	 */
	@SuppressWarnings("unchecked")
	private T unmarshall(InputStream in) throws JAXBException, SAXException {
		T retVal=null;
		//Definisemo kontekst, tj. paket(e) u kome se nalaze bean-ovi
		//Klasa za konverziju XML-a u objektni model
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		//postavljanje validacije
		//W3C sema
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		Schema schema;
		
		Path workspacePath = Paths.get(System.getProperty("user.dir"), "");
		workspacePath = workspacePath.getParent();
		Path schemaPath = workspacePath.resolve(MODEL_PATH+fileName+".xsd");

		//lokacija seme
		schema = schemaFactory.newSchema(schemaPath.toFile());
		//schema = schemaFactory.newSchema(new File(MODEL_PATH+schemaName+".xsd"));
        //setuje se sema
		unmarshaller.setSchema(schema);
					//EventHandler, koji obradjuje greske, ako se dese prilikom validacije
        unmarshaller.setEventHandler(new MyValidationEventHandler());
		
        //ucitava se objektni model, a da se pri tome radi i validacija
        retVal = (T) unmarshaller.unmarshal(in);
		return retVal;
	}

	public JAXBContext getContext() {
		return context;
	}

	public void setContext(JAXBContext context) {
		this.context = context;
	}

	public String getFileName() {
		return namespace;
	}

	public void setFileName(String schemaName) {
		if(!schemaName.isEmpty()) {
			if(schemaName.contains(".")) {
				schemaName=schemaName.substring(0,schemaName.lastIndexOf(".")).trim().toLowerCase();
			}
			this.namespace = schemaName;
		}
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
	
	public T getType() {
		return type;
	}

	public void setType(T type) {
		this.type = type;
	}

	public String getMODEL_PATH() {
		return MODEL_PATH;
	}
	
}
