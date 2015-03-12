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
 * @param <T> object that is being marshalled
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
			//the context acutally requires the location of the ObjectFactory class, which this provides
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
	
	/**
	 * Converts Object->XML and serializes it to {@code out}
	 * @param marshallee object to be marshalled
	 */
	private void marshall(T marshallee) {
		Marshaller marshaller = null;
		try {
			marshaller=context.createMarshaller();
			//set which prefix is using which namespace
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NSPrefixMapper());
			//do indentation
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
