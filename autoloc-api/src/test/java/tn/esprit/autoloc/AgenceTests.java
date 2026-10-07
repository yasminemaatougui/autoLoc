package tn.esprit.autoloc;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock agenceRepository;

    @Test
    void addAgence() {
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis Centre");
        agence.setAdresse("10 Avenue Habib Bourguiba");
        agence.setVille("Tunis");
        agence.setTelephone("71000000");
        agence.setEmail("tunis@autoloc.tn");

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("123 TU 4567");
        v1.setMarque("Peugeot");
        v1.setModele("208");
        v1.setCategorie(CategorieVehicule.CITADINE);
        v1.setTarifJournalier(new BigDecimal("80.00"));
        v1.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("234 TU 5678");
        v2.setMarque("Renault");
        v2.setModele("Clio");
        v2.setCategorie(CategorieVehicule.CITADINE);
        v2.setTarifJournalier(new BigDecimal("75.00"));
        v2.setStatut(StatutVehicule.DISPONIBLE);

        agence.ajouterVehicule(v1);
        agence.ajouterVehicule(v2);

        agenceRepository.save(agence);
    }

    @Test
    void loadAgence() {
        StringBuilder sb = new StringBuilder();
        for (Agence agence : agenceRepository.findAll()) {
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
}

@Repository
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}
