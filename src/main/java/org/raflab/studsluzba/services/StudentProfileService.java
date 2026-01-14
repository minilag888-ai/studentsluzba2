package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.mappers.PredmetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  // ✅ DODAJ IMPORT
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentProfileService {

    @Autowired
    private StudentIndeksRepository indeksRepo;

    @Autowired
    private PolozenPredmetRepository polozenPredmetRepo;

    @Autowired
    private SlusaPredmetRepository slusaPredmetRepo;

    @Autowired
    private UpisGodineRepository upisGodineRepo;

    @Autowired
    private ObnovaGodineRepository obnovaGodineRepo;

    @Autowired
    private UplataRepository uplataRepo;

    @Autowired
    private StudentPodaciRepository studentPodaciRepo;

    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepo;

    @Autowired
    private PredmetRepository predmetRepo;

    @Autowired
    private PredmetMapper predmetMapper;

    private static final Double SKOLARINA_EUR = 3000.0;



    @Transactional(readOnly = true)
    public StudentProfileDTO getStudentProfile(Long studentIndeksId) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setId(indeks.getId());
        dto.setBroj(indeks.getBroj());
        dto.setGodina(indeks.getGodina());
        dto.setStudProgramOznaka(indeks.getStudProgramOznaka());
        dto.setAktivan(indeks.isAktivan());

        if (indeks.getStudent() != null) {
            dto.setIme(indeks.getStudent().getIme());
            dto.setPrezime(indeks.getStudent().getPrezime());
            dto.setSrednjeIme(indeks.getStudent().getSrednjeIme());
            dto.setEmail(indeks.getStudent().getEmail());
            dto.setBrojTelefona(indeks.getStudent().getBrojTelefonaMobilni());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<PolozenPredmetDTO> getPolozeniPredmeti(Long studentIndeksId, Pageable pageable) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        Page<PolozenPredmet> polozeni = polozenPredmetRepo.findByStudentIndeks(indeks, pageable);

        return polozeni.map(pp -> {
            PolozenPredmetDTO dto = new PolozenPredmetDTO();
            dto.setId(pp.getId());
            dto.setSifraPredmeta(pp.getPredmet().getSifra());
            dto.setNazivPredmeta(pp.getPredmet().getNaziv());
            dto.setEspb(pp.getPredmet().getEspb());
            dto.setOcena(pp.getOcena());
            dto.setDatumPolaganja(pp.getDatumPolaganja());


            dto.setTipPolaganja("Ispit");

            return dto;
        });
    }

    @Transactional(readOnly = true)
    public Page<NepolozenPredmetDTO> getNepolozeniPredmeti(Long studentIndeksId, String ime, String prezime, Pageable pageable) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        List<SlusaPredmet> slusaPredmeti = slusaPredmetRepo.findByStudentIndeks(indeks);
        List<PolozenPredmet> polozeniPredmeti = polozenPredmetRepo.findByStudentIndeks(indeks);

        List<Long> polozeniIds = polozeniPredmeti.stream()
                .map(pp -> pp.getPredmet().getId())
                .collect(Collectors.toList());

        List<NepolozenPredmetDTO> nepolozeni = slusaPredmeti.stream()
                .filter(sp -> {
                    Predmet predmet = sp.getDrziPredmet().getPredmet();
                    return !polozeniIds.contains(predmet.getId());
                })
                .map(sp -> {
                    Predmet predmet = sp.getDrziPredmet().getPredmet();

                    NepolozenPredmetDTO dto = new NepolozenPredmetDTO();
                    dto.setId(sp.getId());
                    dto.setSifraPredmeta(predmet.getSifra());
                    dto.setNazivPredmeta(predmet.getNaziv());
                    dto.setEspb(predmet.getEspb());

                    if (sp.getDrziPredmet().getNastavnik() != null) {
                        dto.setImeNastavnika(sp.getDrziPredmet().getNastavnik().getIme());
                        dto.setPrezimeNastavnika(sp.getDrziPredmet().getNastavnik().getPrezime());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), nepolozeni.size());
        List<NepolozenPredmetDTO> pageContent = nepolozeni.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, nepolozeni.size());
    }

    @Transactional(readOnly = true)
    public PreostaliIznosDTO getPreostaliIznosZaUplatu(Long studentIndeksId) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        List<Uplata> uplate = uplataRepo.findByStudent(indeks.getStudent());
        Double ukupnoUplaceno = uplate.stream()
                .mapToDouble(Uplata::getIznos)
                .sum();

        Double srednjiKurs = getSrednjiKursEUR();
        Double preostaloEur = SKOLARINA_EUR - ukupnoUplaceno;
        Double preostaloRsd = preostaloEur * srednjiKurs;

        PreostaliIznosDTO dto = new PreostaliIznosDTO();
        dto.setPreostaliIznosEur(preostaloEur);
        dto.setPreostaliIznosRsd(preostaloRsd);
        dto.setSrednjiKurs(srednjiKurs);
        dto.setSkolarinaNaCenu(SKOLARINA_EUR);

        return dto;
    }



    @Transactional(readOnly = true)
    public StudentProfileDTO getStudentByIndeks(Integer godina, Integer broj, String oznaka) {
        StudentIndeks indeks = indeksRepo.findByGodinaAndBrojAndStudProgramOznaka(godina, broj, oznaka)
                .orElseThrow(() -> new RuntimeException("Student sa datim indeksom ne postoji"));

        return getStudentProfile(indeks.getId());
    }

    @Transactional(readOnly = true)
    public List<UpisGodineDTO> getUpisaneGodine(Long studentIndeksId) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        List<UpisGodine> upisaneGodine = upisGodineRepo.findByStudentIndeksOrderByGodinaStudijaAsc(indeks);

        return upisaneGodine.stream().map(ug -> {
            UpisGodineDTO dto = new UpisGodineDTO();
            dto.setId(ug.getId());
            dto.setGodinaStudija(ug.getGodinaStudija());
            dto.setDatumUpisa(ug.getDatumUpisa());
            dto.setNapomena(ug.getNapomena());
            dto.setSkolskaGodina(ug.getSkolskaGodina().getNaziv());
            dto.setPredmeti(predmetMapper.toDTOList(ug.getPredmeti()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public UpisGodineDTO upisStudentaNaGodinu(Long studentIndeksId, UpisGodineRequestDTO request) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        SkolskaGodina skolskaGodina = skolskaGodinaRepo.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("Školska godina ne postoji"));

        List<Predmet> predmeti = new ArrayList<>();
        predmetRepo.findAllById(request.getPredmetIds()).forEach(predmeti::add);

        UpisGodine upisGodine = new UpisGodine();
        upisGodine.setStudentIndeks(indeks);
        upisGodine.setSkolskaGodina(skolskaGodina);
        upisGodine.setGodinaStudija(request.getGodinaStudija());
        upisGodine.setDatumUpisa(request.getDatumUpisa());
        upisGodine.setNapomena(request.getNapomena());
        upisGodine.setPredmeti(predmeti);

        upisGodine = upisGodineRepo.save(upisGodine);

        UpisGodineDTO dto = new UpisGodineDTO();
        dto.setId(upisGodine.getId());
        dto.setGodinaStudija(upisGodine.getGodinaStudija());
        dto.setDatumUpisa(upisGodine.getDatumUpisa());
        dto.setNapomena(upisGodine.getNapomena());
        dto.setSkolskaGodina(skolskaGodina.getNaziv());
        dto.setPredmeti(predmetMapper.toDTOList(predmeti));

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ObnovaGodineDTO> getObnovljeneGodine(Long studentIndeksId) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        List<ObnovaGodine> obnovljeneGodine = obnovaGodineRepo.findByStudentIndeksOrderByGodinaStudijaAsc(indeks);

        return obnovljeneGodine.stream().map(og -> {
            ObnovaGodineDTO dto = new ObnovaGodineDTO();
            dto.setId(og.getId());
            dto.setGodinaStudija(og.getGodinaStudija());
            dto.setDatumObnove(og.getDatumObnove());
            dto.setNapomena(og.getNapomena());
            dto.setSkolskaGodina(og.getSkolskaGodina().getNaziv());
            dto.setPredmeti(predmetMapper.toDTOList(og.getPredmeti()));

            // Izračunaj ESPB dinamički
            int ukupnoESPB = og.getPredmeti().stream().mapToInt(Predmet::getEspb).sum();
            dto.setUkupnoESPB(ukupnoESPB);

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ObnovaGodineDTO obnovaGodine(Long studentIndeksId, ObnovaGodineRequestDTO request) {
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        SkolskaGodina skolskaGodina = skolskaGodinaRepo.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("Školska godina ne postoji"));

        List<Predmet> predmeti = new ArrayList<>();
        predmetRepo.findAllById(request.getPredmetIds()).forEach(predmeti::add);

        int ukupnoESPB = predmeti.stream().mapToInt(Predmet::getEspb).sum();
        if (ukupnoESPB > 60) {
            throw new RuntimeException("Ukupan ESPB ne može biti veći od 60!");
        }

        ObnovaGodine obnovaGodine = new ObnovaGodine();
        obnovaGodine.setStudentIndeks(indeks);
        obnovaGodine.setSkolskaGodina(skolskaGodina);
        obnovaGodine.setGodinaStudija(request.getGodinaStudija());
        obnovaGodine.setDatumObnove(request.getDatumObnove());
        obnovaGodine.setNapomena(request.getNapomena());
        obnovaGodine.setPredmeti(predmeti);

        obnovaGodine = obnovaGodineRepo.save(obnovaGodine);

        ObnovaGodineDTO dto = new ObnovaGodineDTO();
        dto.setId(obnovaGodine.getId());
        dto.setGodinaStudija(obnovaGodine.getGodinaStudija());
        dto.setDatumObnove(obnovaGodine.getDatumObnove());
        dto.setNapomena(obnovaGodine.getNapomena());
        dto.setSkolskaGodina(skolskaGodina.getNaziv());
        dto.setPredmeti(predmetMapper.toDTOList(predmeti));
        dto.setUkupnoESPB(ukupnoESPB);

        return dto;
    }

    @Transactional
    public UplataDTO dodajUplatu(Long studentIndeksId, UplataRequestDTO request) {
        // VALIDACIJA - iznos ne sme biti negativan ili nula
        if (request.getIznosEur() == null || request.getIznosEur() <= 0) {
            throw new RuntimeException("Iznos uplate mora biti veći od 0");
        }
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("Student indeks ne postoji"));

        Double srednjiKurs = getSrednjiKursEUR();

        Uplata uplata = new Uplata();
        uplata.setStudent(indeks.getStudent());


        uplata.setDatumUplate(request.getDatumUplate());
        uplata.setIznosEur(request.getIznosEur());
        uplata.setSrednjiKurs(srednjiKurs);
        uplata.setIznosRsd(request.getIznosEur() * srednjiKurs);

        uplata = uplataRepo.save(uplata);


        UplataDTO dto = new UplataDTO();
        dto.setId(uplata.getId());
        dto.setDatumUplate(uplata.getDatumUplate());
        dto.setIznosEur(uplata.getIznosEur());
        dto.setSrednjiKurs(srednjiKurs);
        dto.setIznosRsd(uplata.getIznosRsd());

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<StudentPodaciDTO> getStudentsByImeIPrezime(String ime, String prezime, Pageable pageable) {
        Page<StudentPodaci> studenti;

        if (ime != null && prezime != null) {
            studenti = studentPodaciRepo.findByImeContainingIgnoreCaseAndPrezimeContainingIgnoreCase(ime, prezime, pageable);
        } else if (ime != null) {
            studenti = studentPodaciRepo.findByImeContainingIgnoreCase(ime, pageable);
        } else if (prezime != null) {
            studenti = studentPodaciRepo.findByPrezimeContainingIgnoreCase(prezime, pageable);
        } else {
            studenti = studentPodaciRepo.findAll(pageable);
        }

        return studenti.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)  //
    public List<StudentPodaciDTO> getStudentsBySrednjaSkola(Long srednjaSkolaId) {
        List<StudentPodaci> studenti = studentPodaciRepo.findBySrednjaSkolaId(srednjaSkolaId);
        return studenti.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // ========== HELPER METODE ==========

    private StudentPodaciDTO convertToDTO(StudentPodaci student) {
        StudentPodaciDTO dto = new StudentPodaciDTO();
        dto.setId(student.getId());
        dto.setIme(student.getIme());
        dto.setPrezime(student.getPrezime());
        dto.setSrednjeIme(student.getSrednjeIme());
        dto.setJmbg(student.getJmbg());
        dto.setDatumRodjenja(student.getDatumRodjenja());


        dto.setMestoRodjenja(student.getMestoRodjenja());
        dto.setDrzavaRodjenja(student.getDrzavaRodjenja());
        dto.setDrzavljanstvo(student.getDrzavljanstvo());


        if (student.getPol() != null) {
            dto.setPol(String.valueOf(student.getPol()));
        }

        dto.setEmail(student.getEmail());
        dto.setBrojTelefona(student.getBrojTelefonaMobilni());

        if (student.getSrednjaSkola() != null) {
            dto.setSrednjaSkolaNaziv(student.getSrednjaSkola().getNaziv());
        }

        return dto;
    }

    private Double getSrednjiKursEUR() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";
            KursResponse response = restTemplate.getForObject(url, KursResponse.class);

            if (response != null && response.getExchangeMiddle() != null) {
                return response.getExchangeMiddle();
            }
        } catch (Exception e) {
            System.err.println("Greška prilikom dohvatanja kursa: " + e.getMessage());
        }

        return 117.5;
    }

    private static class KursResponse {
        private Double exchangeMiddle;

        public Double getExchangeMiddle() {
            return exchangeMiddle;
        }

        public void setExchangeMiddle(Double exchangeMiddle) {
            this.exchangeMiddle = exchangeMiddle;
        }
    }
}