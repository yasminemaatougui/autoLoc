package tn.esprit.autoloc;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    private void addAgence(CrudRepository<Agence, Long> repository) {
        int suffixe = (int) System.currentTimeMillis();

        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis Centre");
        agence.setAdresse("10 Avenue Habib Bourguiba");
        agence.setVille("Tunis");
        agence.setTelephone("71000000");
        agence.setEmail("tunis@autoloc.tn");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("123 TU 4567 - " + suffixe);
        v1.setMarque("Peugeot");
        v1.setModele("208");
        v1.setCategorie(CategorieVehicule.CITADINE);
        v1.setTarifJournalier(new BigDecimal("80.00"));
        v1.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("234 TU 5678 - " + suffixe);
        v2.setMarque("Renault");
        v2.setModele("Clio");
        v2.setCategorie(CategorieVehicule.CITADINE);
        v2.setTarifJournalier(new BigDecimal("75.00"));
        v2.setStatut(StatutVehicule.DISPONIBLE);

        agence.ajouterVehicule(v1);
        agence.ajouterVehicule(v2);

        repository.save(agence);
    }

    @Test
    void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    private void loadAgence(CrudRepository<Agence, Long> repository, String type) {
        StringBuilder sb = new StringBuilder("Depot utilise : " + type + "\n");
        for (Agence agence : repository.findAll()) {
            sb.append("Agence ").append(agence.getIdAgence())
              .append(" : ").append(agence.getNom())
              .append(" (").append(agence.getVehicules().size()).append(" vehicules)\n");
            for (Vehicule v : agence.getVehicules()) {
                sb.append("    - Vehicule ").append(v.getIdVehicule())
                  .append(" : ").append(v.getImmatriculation()).append("\n");
            }
        }
        fail(sb.toString());
    }

    @Test
    void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "CrudRepository");
    }

    @Test
    void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "JpaRepository");
    }

    @Test
    void loadSortedAgences() {
        StringBuilder sb = new StringBuilder();
        for (Agence agence : fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "idAgence"))) {
            sb.append("Agence ").append(agence.getIdAgence())
              .append(" : ").append(agence.getNom())
              .append(" - ").append(agence.getVille()).append("\n");
        }
        fail(sb.toString());
    }

    @Test
    void loadPagedAgences() {
        Page<Agence> page = fullAgenceRepository.findAll(
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "idAgence")));
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre total de pages : ").append(page.getTotalPages()).append("\n");
        sb.append("Page en cours : ").append(page.getNumber()).append("\n");
        for (Agence agence : page.getContent()) {
            sb.append("Agence ").append(agence.getIdAgence())
              .append(" : ").append(agence.getNom())
              .append(" - ").append(agence.getVille()).append("\n");
        }
        fail(sb.toString());
    }
}

@Repository
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}
