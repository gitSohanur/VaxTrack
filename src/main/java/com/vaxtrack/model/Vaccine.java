package com.vaxtrack.model;

/**
 * Represents a vaccine type in the system.
 * Demonstrates: Encapsulation, Inheritance
 */
public class Vaccine extends BaseEntity {

    private String vaccineName;
    private String manufacturer;
    private int    dosesRequired;
    private String description;

    // --- Constructors ---

    public Vaccine() {
        super();
    }

    public Vaccine(int id, String vaccineName, String manufacturer,
                   int dosesRequired, String description) {
        super(id);
        this.vaccineName   = vaccineName;
        this.manufacturer  = manufacturer;
        this.dosesRequired = dosesRequired;
        this.description   = description;
    }

    // --- Getters & Setters ---

    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public int getDosesRequired() { return dosesRequired; }
    public void setDosesRequired(int dosesRequired) { this.dosesRequired = dosesRequired; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // --- Displayable implementation ---

    @Override
    public String getDisplayInfo() {
        return "Vaccine[" + getId() + "] " + vaccineName +
                " by " + manufacturer +
                " | Doses: " + dosesRequired;
    }
}
