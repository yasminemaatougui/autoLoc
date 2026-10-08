package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {}

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    private void addAgence(CrudRepository<Agence, Long> repository) {
        int timestamp = (int) System.currentTimeMillis();

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("785414TU" + timestamp);
        v1.setMarque("Isuzu");
        v1.setModele("DMax");
        v1.setStatut(StatutVehicule.EN_MAINTENANCE);
        v1.setTarifJournalier(new BigDecimal("100"));
        v1.setCategorie(CategorieVehicule.SUV);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("785415TU" + timestamp);
        v2.setMarque("Toyota");
        v2.setModele("Yaris");
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setTarifJournalier(new BigDecimal("80"));
        v2.setCategorie(CategorieVehicule.UTILITAIRE);

        Set<Vehicule> vehicules = new HashSet<>();
        vehicules.add(v1);
        vehicules.add(v2);

        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");
        agence.setEmail("ariana@autoloc.tn");
        agence.setVehicules(vehicules);

        v1.setAgence(agence);
        v2.setAgence(agence);

        repository.save(agence);
    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    private void loadAgence(CrudRepository<Agence, Long> repository, String repoName) {
        StringBuilder result = new StringBuilder();
        result.append("=== Dépôt : ").append(repoName).append(" ===\n");

        repository.findAll().forEach(agence -> {
            result.append("Agence ")
                    .append(agence.getIdAgence())
                    .append(" : ")
                    .append(agence.getNom())
                    .append(" (")
                    .append(agence.getVehicules().size())
                    .append(" véhicules)\n");

            agence.getVehicules().forEach(vehicule -> result.append("  Véhicule ")
                    .append(vehicule.getIdVehicule())
                    .append(" : ")
                    .append(vehicule.getImmatriculation())
                    .append('\n'));
        });

        fail(result.toString());
    }

    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basicAgenceRepository");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "fullAgenceRepository");
    }

    @Test
    public void loadPagedAgences() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "idAgence"));
        Page<Agence> page = fullAgenceRepository.findAll(pageable);

        System.out.println("Nombre total de pages : " + page.getTotalPages());
        System.out.println("Page en cours : " + page.getNumber());

        page.getContent().forEach(agence ->
                System.out.println("Agence " + agence.getIdAgence() + " : " + agence.getNom()));
    }
}
