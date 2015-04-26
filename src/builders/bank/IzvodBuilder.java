package builders.bank;

import facades.DatabaseFacade;
import rs.ac.uns.ftn.xws.cbs.izvod.Izvod;
import rs.ac.uns.ftn.xws.cbs.zahtev_za_izvod.ZahtevZaIzvod;

public class IzvodBuilder {
	public static Izvod buildIzvod(ZahtevZaIzvod zahtev) {
		
		String brojRacuna = zahtev.getBrojRacuna();
		Izvod izvod = new Izvod();
		
		DatabaseFacade dbf = new DatabaseFacade();
		
		//TODO: pokupi sve transakcije (iz jedne seme ili nekako drugacije) i strpaj u izvestaj.
		//dve opcije: raditi to u bazi, raditi programski. u bazi podrazumeva sastavljanje upita.
		//programski podrazumeva menjanje nacina na koji se zapisuju podaci, od toga ima dve opcije:
		//1) sema ce da sadrzi id + datum (otezava ocitavanje svih transakcija u okviru jednog racuna npr)
		//2) fajl ce da sadrzi ime fajla + datum (otezava citanje fajlova)
		
		return null;
	}
}
