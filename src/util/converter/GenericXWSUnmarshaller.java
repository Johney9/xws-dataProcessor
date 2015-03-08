package util.converter;

import java.io.File;
import java.io.InputStream;

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
	protected final String MODEL_PATH="../xws-model/xml/xsd/";
	protected String filename;
	protected InputStream in;
	protected T type;
	
	/**
	 * Constructor
	 * @param schemaFilename name and file extension of the validation schema
	 * @param in InputStream toward the source
	 * @param type class type
	 */
	public GenericXWSUnmarshaller(String schemaFilename, InputStream in, T type) {
		try {
			this.context=JAXBContext.newInstance(type.getClass().getPackage().getName());
			this.filename=schemaFilename.trim().toLowerCase();
			this.type=type;
			this.in=in;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts XML->Object
	 * @return converted object
	 */
	public T unmarshall() {
		return unmarshall(in);
	}
	
	/**
	 * Converts XML->Object deserializing it from a source
	 * @param in InputStream toward the source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T unmarshall(InputStream in) {
		T retVal=null;
		try {
			//Definisemo kontekst, tj. paket(e) u kome se nalaze bean-ovi
			//Klasa za konverziju XML-a u objektni model
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			//postavljanje validacije
			//W3C sema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			Schema schema;
			//lokacija seme
			schema = schemaFactory.newSchema(new File(MODEL_PATH+filename));
            //setuje se sema
			unmarshaller.setSchema(schema);
						//EventHandler, koji obradjuje greske, ako se dese prilikom validacije
            unmarshaller.setEventHandler(new MyValidationEventHandler());
			
            //ucitava se objektni model, a da se pri tome radi i validacija
            retVal = (T) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public JAXBContext getContext() {
		return context;
	}

	public void setContext(JAXBContext context) {
		this.context = context;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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
