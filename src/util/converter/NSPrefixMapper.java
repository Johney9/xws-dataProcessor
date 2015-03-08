package util.converter;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

//Klasa koja sadrzi mapiranja namespace-a na prefikse

@XmlTransient
public class NSPrefixMapper extends NamespacePrefixMapper {

	private HashMap<String, String> mappings;

	public NSPrefixMapper() { 
		//inicijalizacija
		mappings = new LinkedHashMap<String, String>(); 
		setDefaultMappings(); 
	}

	protected void setDefaultMappings() { 
		clear();
		//ovde se dodaju maparianja nemaspace prefix
		//Ako se umesto prefiksa "ex5" postavi "", onda ce to biti default namespace
		//addMapping("http://informatika.ftn.ns.ac.yu/xmlb/example5", "ex5"); 
		addMapping("http://www.w3.org/2001/XMLSchema-instance", "xsi"); 
		addMapping("http://java.sun.com/xml/ns/jaxb", "jaxb"); 
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/tipovi", "t");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/izvod","iz");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/banka","b");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/centralna_banka","cb");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/firma","f");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/zahtev_za_izvod","ziz");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/nalog_za_placanje","pl");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/mt102","mt102");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/mt103","mt103");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/mt900","mt900");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/mt910","mt910");
		addMapping("http://www.ftn.uns.ac.rs/xws/cbs/faktura","fkt");
	}

	public void addMapping(String uri, String prefix){
		mappings.put(uri, prefix);
	} 
	
	public String getMapping(String uri){
		return (String)mappings.get(uri);
	} 
	public HashMap<String, String> getMappings(){
		return mappings;
	} 
	public void clear(){
		mappings.clear();
	}

	public String getPreferredPrefix(String namespaceURI, String suggestion, boolean requirePrefix) { 
		//System.out.println("Pozvano za " + namespaceURI + " " + suggestion + " " + requirePrefix);
		String toReturn = getMapping(namespaceURI); 
		if(toReturn != null)
			return toReturn;
		return suggestion; 
	} 

}
