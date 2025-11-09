package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private NastavnikZvanjeRepository nastavnikZvanjeRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private GrupaRepository grupaRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private PredispitnaObavezaRepository predispitnaObavezaRepository;
    @Autowired
    private VrstaStudijaRepository vrstaStudijaRepository;

    @Override
    public void run(String... args) throws Exception {
        List<StudijskiProgram> spList = new ArrayList<>();

        // Prvo kreiraj vrstu studija (OAS)
        VrstaStudija oas = new VrstaStudija("OAS", "Osnovne akademske studije");
        vrstaStudijaRepository.save(oas);

        for (int i = 1; i <= 5; i++) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setOznaka("SP" + i);
            sp.setNaziv("Program " + i);
            sp.setGodinaAkreditacije(2020 + i);
            sp.setNazivZvanja("Bachelor");
            sp.setTrajanjeSemestara(8);
            sp.setVrstaStudija(oas);
            spList.add(studijskiProgramRepository.save(sp));
        }

        List<Predmet> predmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Predmet p = new Predmet();
            p.setSifra("PR" + i);
            p.setNaziv("Predmet " + i);
            p.setOpis("Opis predmeta " + i);
            p.setEspb(6 + i);
            p.setStudProgram(spList.get((i - 1) % spList.size()));
            p.setObavezan(i % 2 == 0);
            predmetList.add(predmetRepository.save(p));
        }

        List<Nastavnik> nastavnikList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme("Nastavnik" + i);
            n.setPrezime("Prezime" + i);
            n.setSrednjeIme("Srednje" + i);
            n.setEmail("nastavnik" + i + "@example.com");
            n.setBrojTelefona("06012345" + i);
            n.setAdresa("Adresa " + i);
            n.setDatumRodjenja(LocalDate.of(1980 + i, i, i));
            n.setPol(i % 2 == 0 ? 'M' : 'F');
            n.setJmbg("80010123456" + i);
            nastavnikList.add(nastavnikRepository.save(n));
        }

        List<NastavnikZvanje> zvanjeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            NastavnikZvanje nz = new NastavnikZvanje();
            nz.setDatumIzbora(LocalDate.of(2020 + i, i, i));
            nz.setNaucnaOblast("Oblast " + i);
            nz.setUzaNaucnaOblast("Uza oblast " + i);
            nz.setZvanje("Zvanje " + i);
            nz.setAktivno(i % 2 == 0);
            nz.setNastavnik(nastavnikList.get(i - 1));
            zvanjeList.add(nastavnikZvanjeRepository.save(nz));
        }

        List<StudentPodaci> studentPodaciList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentPodaci s = new StudentPodaci();
            s.setIme("Student" + i);
            s.setPrezime("Prezime" + i);
            s.setSrednjeIme("Srednje" + i);
            s.setJmbg("00101012345" + i);
            s.setDatumRodjenja(LocalDate.of(2000 + i, i, i));
            s.setMestoRodjenja("Mesto" + i);
            s.setMestoPrebivalista("Prebivaliste" + i);
            s.setDrzavaRodjenja("Srbija");
            s.setDrzavljanstvo("Srbija");
            s.setNacionalnost("Srpska");
            s.setPol(i % 2 == 0 ? 'F' : 'M');
            s.setAdresa("Adresa " + i);
            s.setBrojTelefonaMobilni("06123456" + i);
            s.setEmail("student" + i + "@example.com");
            studentPodaciList.add(studentPodaciRepository.save(s));
        }

        List<StudentIndeks> indeksList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentIndeks si = new StudentIndeks();
            si.setBroj(i);
            si.setGodina(2023);
            si.setStudProgramOznaka(spList.get(i - 1).getOznaka());
            si.setNacinFinansiranja(i % 2 == 0 ? "Budzet" : "Samofinansiranje");
            si.setAktivan(true);
            si.setVaziOd(LocalDate.of(2023, 10, i));
            si.setStudent(studentPodaciList.get(i - 1));
            si.setStudijskiProgram(spList.get(i - 1));
            si.setOstvarenoEspb(0);
            indeksList.add(studentIndeksRepository.save(si));
        }

        List<DrziPredmet> drziPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnikList.get(i - 1));
            dp.setPredmet(predmetList.get(i - 1));
            drziPredmetList.add(drziPredmetRepository.save(dp));
        }

        List<SlusaPredmet> slusaPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SlusaPredmet sl = new SlusaPredmet();
            sl.setStudentIndeks(indeksList.get(i - 1));
            sl.setDrziPredmet(drziPredmetList.get(i - 1));
            slusaPredmetList.add(slusaPredmetRepository.save(sl));
        }

        for (int i = 1; i <= 5; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(spList.get(i - 1));
            g.setPredmeti(Collections.singletonList(predmetList.get(i - 1)));
            grupaRepository.save(g);
        }

        // Školske godine
        SkolskaGodina godina1 = new SkolskaGodina();
        godina1.setNaziv("2023/2024");
        godina1.setPocetakZimskog(LocalDate.of(2023, 10, 1));
        godina1.setKrajZimskog(LocalDate.of(2024, 2, 15));
        godina1.setPocetakLetnjeg(LocalDate.of(2024, 2, 16));
        godina1.setKrajLetnjeg(LocalDate.of(2024, 9, 30));
        godina1.setAktivna(true);
        skolskaGodinaRepository.save(godina1);

        SkolskaGodina godina2 = new SkolskaGodina();
        godina2.setNaziv("2024/2025");
        godina2.setPocetakZimskog(LocalDate.of(2024, 10, 1));
        godina2.setKrajZimskog(LocalDate.of(2025, 2, 15));
        godina2.setPocetakLetnjeg(LocalDate.of(2025, 2, 16));
        godina2.setKrajLetnjeg(LocalDate.of(2025, 9, 30));
        godina2.setAktivna(false);
        skolskaGodinaRepository.save(godina2);

        // Predispitne obaveze
        List<Predmet> predmeti = new ArrayList<>();
        predmetRepository.findAll().forEach(predmeti::add);  // ← FIX!

        for (int i = 0; i < predmeti.size(); i++) {
            PredispitnaObaveza obaveza1 = new PredispitnaObaveza();
            obaveza1.setPredmet(predmeti.get(i));
            obaveza1.setSkolskaGodina(godina1);
            obaveza1.setVrsta("Kolokvijum 1");
            obaveza1.setMaxPoena(30);
            predispitnaObavezaRepository.save(obaveza1);

            PredispitnaObaveza obaveza2 = new PredispitnaObaveza();
            obaveza2.setPredmet(predmeti.get(i));
            obaveza2.setSkolskaGodina(godina1);
            obaveza2.setVrsta("Kolokvijum 2");
            obaveza2.setMaxPoena(30);
            predispitnaObavezaRepository.save(obaveza2);

            PredispitnaObaveza obaveza3 = new PredispitnaObaveza();
            obaveza3.setPredmet(predmeti.get(i));
            obaveza3.setSkolskaGodina(godina1);
            obaveza3.setVrsta("Projekat");
            obaveza3.setMaxPoena(40);
            predispitnaObavezaRepository.save(obaveza3);
        }

        System.out.println("Seeder completed: StudijskiProgram, Predmet, Nastavnik, Student, SkolskaGodina, PredispitnaObaveza");
    }
}