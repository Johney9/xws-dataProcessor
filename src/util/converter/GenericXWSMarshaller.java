package util.converter;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

/**
 * 
 * @author Nikola
 * Convert Object->XML and serialize the result to {@code OutputStream out}
 * @param <T> class that is being marshalled
 */
public class GenericXWSMarshaller<T> {
	
	protected JAXBContext context;
	protected T marshallee;
	protected OutputStream out;
	
	/**
	 * 
	 * @param marshalee object to be marshalled
	 * @param out OutputStream towards destination
	 */
	public GenericXWSMarshaller(T marshalee, OutputStream out) {
		try {
			context=JAXBContext.newInstance(marshalee.getClass().getPackage().getName());
			this.marshallee=marshalee;
			this.out=out;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts Object->XML the object passed to the constructor and serializes it to {@code OutputStream out}
	 */
	public void marshall() {
		marshall(marshallee);
	}
	
	//TODO: change marshaller output stream to target the database, instead of a file
	/**
	 * Converts Object->XML and stores it in the database
	 * @param marshallee object to be marshalled
	 */
	private void marshall(T marshallee) {
		Marshaller marshaller = null;
		try {
			marshaller=context.createMarshaller();
			//na ovaj naci se setuje koji prefiks se koristi za koji namespace
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NSPrefixMapper());
			//da se radi identacija
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(marshallee, out);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
