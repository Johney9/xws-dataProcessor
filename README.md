# xws-dataProcessor
data processing module
<br>
using <b>Java 1.7</b> and the <b>xws-dbConnector</b>, <b>xws-dbFacade</b> & <b>xws-model</b> modules
<br>
<br>
Worked on by: johney9, pyromaniackeca
<br>
<br>
Used by: web services (firma, banka, centralna banka)
<br>
<br>
Napomena: molim te ako sam nesto propustio a ti skontas, dodaj. ako mislis da nesto moze bolje da se uradi, slobodno, ali me samo obavesti
<br>
<br>
Napomena 2: napravljen ti je konverter u oba smera (XML <=> Object), koristi ga, trebace ti.
<br>
<br>
Napomena 3: kada hoces nesto da sacuvas u bazu ili procitas iz baze, imas facade klase u xws-dbFacade. one rade marshalling/unmarshalling za tebe.
<br>
<br>
TODO: napravi poslovnu logiku i interface sa kojim cu ja komunicirati iz web servisa (u stilu koristeci neku klasu iz tvog modula, posaljem objekat koji mi je stigao i ti mi vratis rezultat u zavisnosti od objekta koji sam ti ja poslao)
<ul>
<li><b>1.</b> procitaj project-xws-2014.pdf, pogotovo sekciju "3. test scenariji"</li>
<li><b>2.</b> procitaj ove stavke</li>
</ul>
<br>
<b>Firma</b>
<ul>
  <li>
    procesiranje fakture:
    <br>
    //*kontekst: faktura je nacin komunikacije izmedju 2 firme, ona moze biti: daj mi predmete ili daj mi pare. 
    <ul>
      <li>odredjivanje da li je faktura racun ili "zahtev" za kupovinu</li>
      <li>ako je zahtev za kupovinu, obraditi ga i vratiti kao povratnu vrednost fakturu koja je racun</li>
      <li>validacija sadrzaja fakture (npr. da ukupna cena odgovara sumi pojedinacnih cena itd., zavisi od konteksta) ** ova validacija je u razlicitom obliku potrebna za sve poruke koje se razmenjuju(kod banke, centralne banke)</li>      
      <li>validacija da li je poslato dobroj firmi(poredjenje sa stringom koji se dodatno salje pri pozivu metode i odgovara PIB-u u Fakturi; ili necim drugim ako mislis da je bolje) ** i ova validacija je potrebna u razlicitom obliku kod ostalih poruka</li>    
    </ul>
  </li>
</ul>
<br>
<b>Banka</b>
<ul>
  <li>
    procesiranje zahteva za izvod:
    <br>
    //*kontekst: firma svojoj banci salje zahtev za izvod (stanje para na odredjen datum) i ona mu vraca izvod u presecima (delicima, odnosno u nasem projektu stavkama)
    <ul>
      <li>validacija sadrzaja (izmedju ostalog to je: validacija da li je poslato dobroj banci, validacija racuna)</li>
      <li>dobavljanje izvoda iz baze i vracanje ga nazad kao povratnu vrednost</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje izvoda
    <br>
    //*kontekst: izvod koji banka posalje firmi, nista posebno osim za to da ide u presecima (delovima)
    <ul>
      <li>validacija sadrzaja</li>
      <li>ne mogu trenutno da smislim, skontaj</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje naloga za placanje
    <br>
    //*kontekst: nalog za placanje firma salje svojoj banci, nakon toga banka komunicira sa bankom druge firme
    <ul>
      <li>validacija sadrzaja (poklapanje racuna, bla bla)</li>
      <li>da li kupac ima dovoljno sredstava na racunu</li>
      <li>ako su kupac i prodavac u istoj banci, odmah skini sa racuna kupca (ako kupac ima dovoljno sredstava ofc)</li>
      <li>
        ako nisu u istoj banci:
        <ul>
          <li>rezervisi sredstva na racunu kupca</li>
          <li>ako je iznos manji od 250.000 i nalog nije oznacen kao hitan, vrati (generisi) popunjen MT102</li>
          <li>ako je iznos veci od 250.000 ili je nalog oznacen kao hitan, vrati popunjen MT103</li>
        </ul>
      </li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje MT102
    <br>
    //*kontekst: banka prima ovo i MT910 od centralne banke i koristi ih za odobravanje sredstava prodavcu (prebacuje prodavcu pare na racun)
    <ul>
      <li>validacija sadrzaja</li>
      <li>svi pojedinacni nalozi (stavke u MT102) moraju biti upuceni klijentima banke koja ih prima</li>
      <li>vrati mi ok/nije ok</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje MT103
    <br>
    //*kontekst: banka prima ovo i MT900 od centralne banke i koristi ih za skidanje para sa raucna kupca
    <ul>
      <li>validacija sadrzaja</li>
      <li>vrati mi ok/nije ok</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje MT900 (poruka o zaduzenju)
    <br>
    //*kontekst: ovo i MT102/MT103 se prima od centralne banke i koristi se za odobrenje transfera sredstava
    <ul>
      <li>validacija sadrzaja</li>
      <li>skidas klijentu pare sa racuna (ha haha ha haha haha)</li>
      <li>vrati mi ok/nije ok</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje MT910 (poruka o odobrenju)
    <br>
    //*kontekst: ovo i MT102/MT103 se prima od centralne banke i koristi se za odobrenje transfera sredstava
    <ul>
      <li>validacija sadrzaja</li>
      <li>uplacujes klijentu pare na racun</li>
      <li>vrati mi ok/nije ok</li>
    </ul>
  </li>
</ul>
<br>
<b>Centralna banka</b>
<ul>
  <li>
    procesiranje MT103
    <br>
    //*kontekst: centralna banka prima MT103 od banke A (banka prodavca) i koristi je da prebaci pare iz banke B (banka kupca) u banku A
    <ul>
      <li>validacija sadrzaja</li>
      <li>generisi popunjen MT900 (poruka o zaduzenju) za banku B</li>
      <li>generisi popunjen MT910 (porukao odobrenju) za banku A</li>
      <li>vratis mi ta 2 nekako</li>
    </ul>
  </li>
  <br>
  <li>
    procesiranje MT102
    <br>
    //*kontekst: centralna banka prima vise komada MT102 tokom nekog vremenskog perioda, sortira ih po bankama i grupise u jedan grupni MT102 za svaku banku.
    To znaci da, npr. centralnoj banci stigne 3 MT102 za banku Intesu, neka im je vrednosti u novcima (+10,-5,-10). Centralna banka onda grupise ta 3 MT102 u jedan grupni MT102 i nakon nekog vremena posalje ih skupa banci Intesi.
    <ul>
      <li>validacija sadrzaja</li>
      <li>grupises vise MT102 (liste?) u jedan MT102 i vratis ga kao povratnu vrednost (sa proverom da li su upuceni istoj banci)</li>
      <li>moze da se desi da ti stigne i grupni MT102 i pojedinacni MT102 (oba su ista klasa), treba oba da mozes da obradis</li>
      <li>generises popunjen MT900 (poruka o zaduzenju) na osnovu grupnog ili pojedinacnog MT102</li>
      <li>generises popunjen MT910 (poruka o odobrenju) na osnovu grupnog ili pojedinacnog MT102</li>
      <li>vratis mi ta 2 nekako
    </ul>
  </li>
</ul>
<br>
Stigli smo na kraj, napokon. Ako imas pitanja, slobodno pitaj.
<br>
Medo se ne javlja, verovatno cu ja raditi na xws-dbConnector i xws-dbFacade
