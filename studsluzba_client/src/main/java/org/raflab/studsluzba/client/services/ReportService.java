package org.raflab.studsluzba.client.services;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzba.client.api.response.IspitResponse;
import org.raflab.studsluzba.client.api.response.RezultatIspitaResponse;
import org.raflab.studsluzba.shared.dtos.PolozenPredmetDTO;
import org.raflab.studsluzba.shared.dtos.StudentProfileDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class ReportService {

    /**
     * Generiši uverenje o studiranju
     */
    public void generateUverenjeOStudiranju(StudentProfileDTO student) throws JRException {
        log.info("Generating uverenje o studiranju for student: {}", student.getId());

        //  DODAJ DEBUG
        log.info("🔍 DEBUG - Student data:");
        log.info("   ime: {}", student.getIme());
        log.info("   prezime: {}", student.getPrezime());
        log.info("   brojIndeksa: {}", student.getBroj());
        log.info("   godinaIndeksa: {}", student.getGodina());
        log.info("   studijskiProgram: {}", student.getStudProgramOznaka());

        // Učitaj .jrxml template
        InputStream templateStream = getClass().getResourceAsStream("/reports/uverenje_o_studiranju.jrxml");
        if (templateStream == null) {
            throw new JRException("Template not found: uverenje_o_studiranju.jrxml");
        }

        log.info(" Template loaded successfully");

        // Kompajliraj report
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
        log.info(" Report compiled successfully");

        // Parametri
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ime", student.getIme());
        parameters.put("prezime", student.getPrezime());
        parameters.put("brojIndeksa", student.getBroj());
        parameters.put("godinaIndeksa", student.getGodina());
        parameters.put("studijskiProgram", student.getStudProgramOznaka());
        parameters.put("datumIzdavanja", LocalDate.now());

        //  DODAJ DEBUG
        log.info("🔍 Parameters map:");
        parameters.forEach((key, value) -> log.info("   {} = {}", key, value));

        // Prazna kolekcija (report nema detail band)
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.emptyList());

        // Popuni report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        log.info(" Report filled successfully, pages: {}", jasperPrint.getPages().size());

        // Eksportuj u PDF
        String outputPath = System.getProperty("user.home") + "/Desktop/uverenje_o_studiranju.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        log.info("Report generated: {}", outputPath);

        // Otvori PDF
        openPdf(outputPath);
    }

    /**
     * Generiši uverenje o položenim ispitima
     */
    public void generateUverenjeOPolozenimIspitima(
            StudentProfileDTO student,
            List<PolozenPredmetDTO> polozeniPredmeti) throws JRException {

        log.info("Generating uverenje o polozenim ispitima for student: {}", student.getId());

        InputStream templateStream = getClass().getResourceAsStream("/reports/uverenje_o_polozenim_ispitima.jrxml");
        if (templateStream == null) {
            throw new JRException("Template not found: uverenje_o_polozenim_ispitima.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        // Grupisanje po godini studija (za prikaz)
        // Ovo je simplifikovano - u realnosti bi se dohvatilo sa servera
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ime", student.getIme());
        parameters.put("prezime", student.getPrezime());
        parameters.put("brojIndeksa", student.getBroj());
        parameters.put("godinaIndeksa", student.getGodina());
        parameters.put("studijskiProgram", student.getStudProgramOznaka());
        parameters.put("datumIzdavanja", LocalDate.now());

        // Data source - položeni predmeti
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(polozeniPredmeti);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        String outputPath = System.getProperty("user.home") + "/Desktop/uverenje_o_polozenim_ispitima.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        log.info("Report generated: {}", outputPath);
        openPdf(outputPath);
    }

    /**
     * Generiši zapisnik sa ispita
     */
    public void generateZapisnikSaIspita(
            IspitResponse ispit,
            List<RezultatIspitaResponse> rezultati) throws JRException {

        log.info("Generating zapisnik sa ispita for ispit ID: {}", ispit.getId());

        InputStream templateStream = getClass().getResourceAsStream("/reports/zapisnik_sa_ispita.jrxml");
        if (templateStream == null) {
            throw new JRException("Template not found: zapisnik_sa_ispita.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("predmet", ispit.getPredmetNaziv());
        parameters.put("sifraPredmeta", ispit.getPredmetSifra());
        parameters.put("ispitniRok", ispit.getIspitniRokNaziv());
        parameters.put("datumOdrzavanja", ispit.getDatumOdrzavanja());
        parameters.put("nastavnik", ispit.getNastavnikIme() + " " + ispit.getNastavnikPrezime());
        parameters.put("datumIzdavanja", LocalDate.now());

        // Data source - rezultati (sortirani studenti)
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rezultati);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        String outputPath = System.getProperty("user.home") + "/Desktop/zapisnik_sa_ispita.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        log.info("Report generated: {}", outputPath);
        openPdf(outputPath);
    }

    /**
     * Otvori PDF u defaultnom pregledaču
     */
    private void openPdf(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(pdfFile);
                    log.info("Opened PDF: {}", filePath);
                }
            }
        } catch (Exception e) {
            log.error("Failed to open PDF", e);
        }
    }
}