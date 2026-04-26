package vehicles;
import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

public abstract class Vehicle implements Comparable<Vehicle> {
    private final String id;
    private final String model;
    private final double maxSpeed; // km/h
    private double currentMileage; // km

    protected Vehicle(String id, String model, double maxSpeed) throws InvalidOperationException {
        if (id == null || id.isBlank()) throw new InvalidOperationException("Vehicle id cannot be empty");
        if (model == null || model.isBlank()) throw new InvalidOperationException("Vehicle model cannot be empty");
        if (maxSpeed <= 0) throw new InvalidOperationException("maxSpeed must be > 0");
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;
    public abstract double calculateFuelEfficiency(); // km/L
    public abstract double estimateJourneyTime(double distance) throws InvalidOperationException; // hours

    public void displayInfo() {
        System.out.printf("%s[%s] model=%s maxSpeed=%.1f km/h mileage=%.1f km eff=%.2f km/L%n",
            this.getClass().getSimpleName(), id, model, maxSpeed, currentMileage, calculateFuelEfficiency());
    }

    public String getId() { return id; }
    public String getModel() { return model; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getCurrentMileage() { return currentMileage; }

    public void setCurrentMileage(double currentMileage) {
        this.currentMileage = currentMileage;
    }
    
    protected void addMileage(double distance) { this.currentMileage += distance; }

    //comparator for sorting vehicles order by fuelefficiency in desc order
    @Override
    public int compareTo(Vehicle other) { return -Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency()); }
}
