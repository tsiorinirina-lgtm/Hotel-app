package main.java.n1.hotel;

import java.time.LocalDateTime;
import java.util.UUID;

public class Paiement {
    public enum StatutPaiement {
        EN_ATTENTE, VALIDE, REFUSE, REMBOURSE
    }
    public enum ModePaiement {
        CARTE_BANCAIRE, ESPECES, VIREMENT, CHEQUE
    }

    private final String idPaiement;
    private double montant;
    private double montantPaye;
    private ModePaiement modePaiement;
    private StatutPaiement statut;
    private LocalDateTime datePaiement;
    private String referenceTransaction;

    public Paiement(double montant, ModePaiement modePaiement) {
        if (montant <= 0) throw new IllegalArgumentException("The montant must be positive.");
        this.idPaiement         = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.montant            = montant;
        this.montantPaye        = 0.0;
        this.modePaiement       = modePaiement;
        this.statut             = StatutPaiement.EN_ATTENTE;
        this.datePaiement       = null;
        this.referenceTransaction = null;
    }

    public boolean effectuerPaiement(double montantRecu) {
        if (statut == StatutPaiement.VALIDE) {
            System.out.println("⚠ Paiement valid.");
            return false;
        }
        if (montantRecu < montant) {
            System.out.printf("✗ Montant not sufficient : got %.2f€ / waited %.2f€%n", montantRecu, montant);
            this.statut = StatutPaiement.REFUSE;
            return false;
        }
        this.montantPaye          = montantRecu;
        this.statut               = StatutPaiement.VALIDE;
        this.datePaiement         = LocalDateTime.now();
        this.referenceTransaction = "TXN-" + idPaiement + "-" + System.currentTimeMillis();
        System.out.printf("✔ Paiement valid : %.2f€ — Réf. %s%n", montant, referenceTransaction);
        return true;
    }
    
    public boolean rembourser() {
        if (statut != StatutPaiement.VALIDE) {
            System.out.println("✗ Impossible to refund : paiement not valid.");
            return false;
        }
        this.statut = StatutPaiement.REMBOURSE;
        System.out.printf("↩ Paiement refund : %.2f€ (Réf. %s)%n", montant, referenceTransaction);
        return true;
    }

    public double getMontantRestant() {
        return Math.max(0, montant - montantPaye);
    }

    public boolean estPaye() {
        return statut == StatutPaiement.VALIDE;
    }

    public String getIdPaiement()            { return idPaiement; }
    public double getMontant()               { return montant; }
    public double getMontantPaye()           { return montantPaye; }
    public ModePaiement getModePaiement()    { return modePaiement; }
    public StatutPaiement getStatut()        { return statut; }
    public LocalDateTime getDatePaiement()   { return datePaiement; }
    public String getReferenceTransaction()  { return referenceTransaction; }

    public void setMontant(double montant) {
        if (statut == StatutPaiement.VALIDE) throw new IllegalStateException("Impossible to modify a validated payment.");
        if (montant <= 0) throw new IllegalArgumentException("The amount must be positive.");
        this.montant = montant;
    }

    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }

    @Override
    public String toString() {
        return String.format(
            "Paiement[id=%s | montant=%.2f€ | payé=%.2f€ | mode=%s | statut=%s | date=%s]",
            idPaiement, montant, montantPaye, modePaiement, statut,
            datePaiement != null ? datePaiement.toString() : "—"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paiement)) return false;
        return this.idPaiement.equals(((Paiement) o).idPaiement);
    }

    @Override
    public int hashCode() { return idPaiement.hashCode(); }
}