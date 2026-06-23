package n1.hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Reservation {

    public enum StatutReservation {
        EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
    }

    public enum TypeChambre {
        SIMPLE, DOUBLE, SUITE, PRESIDENTIELLE;

        public double getPrixParNuit() {
            return switch (this) {
                case SIMPLE          -> 60.0;
                case DOUBLE          -> 100.0;
                case SUITE           -> 200.0;
                case PRESIDENTIELLE  -> 500.0;
            };
        }
    }

    // ─── Attributs ──────────────────────────────────────────────────
    private final String idReservation;
    private final Client client;        
    private Manager manager;            
    private TypeChambre typeChambre;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private int nombrePersonnes;
    private StatutReservation statut;
    private Paiement paiement;             

    public Reservation(Client client, TypeChambre typeChambre,
                       LocalDate dateArrivee, LocalDate dateDepart,
                       int nombrePersonnes) {
        validerDates(dateArrivee, dateDepart);
        if (nombrePersonnes <= 0) throw new IllegalArgumentException("Nombre de personnes invalide.");

        this.idReservation  = "RES-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.client         = client;
        this.typeChambre    = typeChambre;
        this.dateArrivee    = dateArrivee;
        this.dateDepart     = dateDepart;
        this.nombrePersonnes = nombrePersonnes;
        this.statut         = StatutReservation.EN_ATTENTE;
        this.paiement       = null;
        System.out.printf("✔ Réservation créée [%s] — %s du %s au %s%n",
                idReservation, typeChambre, dateArrivee, dateDepart);
    }

    public long getNombreNuits() {
        return ChronoUnit.DAYS.between(dateArrivee, dateDepart);
    }

    public double calculerMontantTotal() {
        return typeChambre.getPrixParNuit() * getNombreNuits();
    }

    public boolean confirmer(Paiement.ModePaiement modePaiement) {
        if (statut != StatutReservation.EN_ATTENTE) {
            System.out.println("✗ La réservation ne peut pas être confirmée (statut : " + statut + ").");
            return false;
        }
        double montant = calculerMontantTotal();
        this.paiement = new Paiement(montant, modePaiement);
        this.statut   = StatutReservation.CONFIRMEE;
        System.out.printf("✔ Réservation [%s] confirmée — Montant à payer : %.2f€%n", idReservation, montant);
        return true;
    }

    public boolean annuler() {
        if (statut == StatutReservation.ANNULEE || statut == StatutReservation.TERMINEE) {
            System.out.println("✗ Réservation déjà " + statut);
            return false;
        }
        if (paiement != null && paiement.estPaye()) {
            paiement.rembourser();
        }
        this.statut = StatutReservation.ANNULEE;
        System.out.printf("↩ Réservation [%s] annulée.%n", idReservation);
        return true;
    }

    public boolean terminer() {
        if (statut != StatutReservation.CONFIRMEE) {
            System.out.println("✗ La réservation doit être confirmée pour être terminée.");
            return false;
        }
        if (paiement == null || !paiement.estPaye()) {
            System.out.println("✗ Le paiement doit être validé avant le check-out.");
            return false;
        }
        this.statut = StatutReservation.TERMINEE;
        System.out.printf("✔ Check-out effectué pour la réservation [%s].%n", idReservation);
        return true;
    }

    public boolean modifierDates(LocalDate nouvelleDateArrivee, LocalDate nouvelleDateDepart) {
        if (statut == StatutReservation.ANNULEE || statut == StatutReservation.TERMINEE) {
            System.out.println("✗ Impossible de modifier une réservation " + statut);
            return false;
        }
        validerDates(nouvelleDateArrivee, nouvelleDateDepart);
        this.dateArrivee = nouvelleDateArrivee;
        this.dateDepart  = nouvelleDateDepart;
        if (paiement != null && !paiement.estPaye()) {
            paiement.setMontant(calculerMontantTotal());
        }
        System.out.printf("✔ Dates modifiées : %s → %s (nouveau montant : %.2f€)%n",
                dateArrivee, dateDepart, calculerMontantTotal());
        return true;
    }

    private void validerDates(LocalDate arrivee, LocalDate depart) {
        if (arrivee == null || depart == null) throw new IllegalArgumentException("Dates invalides.");
        if (!depart.isAfter(arrivee)) throw new IllegalArgumentException("La date de départ doit être après l'arrivée.");
    }

    public String getIdReservation()        { return idReservation; }
    public Client getClient()               { return client; }
    public Manager getManager()             { return manager; }
    public TypeChambre getTypeChambre()     { return typeChambre; }
    public LocalDate getDateArrivee()       { return dateArrivee; }
    public LocalDate getDateDepart()        { return dateDepart; }
    public int getNombrePersonnes()         { return nombrePersonnes; }
    public StatutReservation getStatut()    { return statut; }
    public Paiement getPaiement()           { return paiement; }
    
    public void setManager(Manager manager)             { this.manager = manager; }
    public void setTypeChambre(TypeChambre typeChambre) { this.typeChambre = typeChambre; }
    public void setNombrePersonnes(int n)               {
        if (n <= 0) throw new IllegalArgumentException("Nombre invalide.");
        this.nombrePersonnes = n;
    }

    @Override
    public String toString() {
        return String.format(
            "Reservation[id=%s | client=%s | chambre=%s | %s→%s (%d nuits) | %d pers. | statut=%s | montant=%.2f€ | paiement=%s]",
            idReservation,
            client != null ? client.getNom() : "—",  
            typeChambre, dateArrivee, dateDepart,
            getNombreNuits(), nombrePersonnes, statut,
            calculerMontantTotal(),
            paiement != null ? paiement.getStatut() : "aucun"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        return this.idReservation.equals(((Reservation) o).idReservation);
    }

    @Override
    public int hashCode() { return idReservation.hashCode(); }
}