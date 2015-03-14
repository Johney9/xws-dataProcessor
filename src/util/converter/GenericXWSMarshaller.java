package util.converter;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * 
 * @author Nikola
 * Convert Object->XML and serialize the result to {@code OutputStream out}
 * @param <T> object that is being marshalled
 */
public class GenericXWSMarshaller<T> {
	
	protected JAXBContext context;
	protected T marshallee;
	protected OutputStream out;
	protected final String MODEL_PATH="xws-model/xml/xsd/";
	protected String fileName;
	
	/**
	 * 
	 * @param marshalee object to be marshalled
	 * @param out OutputStream towards destination
	 * @throws JAXBException 
	 */
	public GenericXWSMarshaller(T marshalee, OutputStream out) {
		/**the context acutally requires the location of the ObjectFactory class, which this provides**/
		try {
			context=JAXBContext.newInstance(marshalee.getClass().getPackage().getName());
			this.marshallee=marshalee;
			this.out=out;
			
			//get the name of the youngest child in the package tree
			String fullPackageName = marshalee.getClass().getPackage().getName();
			this.fileName = fullPackageName.substring(fullPackageName.lastIndexOf(".")+1);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts Object->XML the object passed to the constructor and serializes it to {@code OutputStream out}
	 * @throws JAXBException 
	 * @throws SAXException 
	 */
	public void marshall() throws JAXBException, SAXException {
		marshall(marshallee);
	}
	
	/**
	 * Converts Object->XML and serializes it to {@code out}
	 * @param marshallee object to be marshalled
	 * @throws JAXBException 
	 * @throws SAXException 
	 */
	private void marshall(T marshallee) throws JAXBException, SAXException {
		Marshaller marshaller = null;
		marshaller=context.createMarshaller();
		//set which prefix is using which namespace
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NSPrefixMapper());
		//do indentation
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		setValidation(marshaller);
		marshaller.marshal(marshallee, out);
	}
	
	private void setValidation(Marshaller marshaller) throws SAXException {
		//postavljanje validacije
		//W3C sema
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		Schema schema = null;
		
		Path workspacePath = Paths.get(System.getProperty("user.dir"), "");
		workspacePath = workspacePath.getParent();
		Path schemaPath = workspacePath.resolve(MODEL_PATH+fileName+".xsd");

		//lokacija seme
		schema = schemaFactory.newSchema(schemaPath.toFile());
		marshaller.setSchema(schema);
	}
	public JAXBContext getContext() {
		return context;
	}

	public void setContext(JAXBContext context) {
		this.context = context;
	}

	public T getMarshallee() {
		return marshallee;
	}

	public void setMarshallee(T marshallee) {
		this.marshallee = marshallee;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
}
