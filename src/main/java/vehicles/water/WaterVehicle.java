package vehicles.water;
import vehicles.Vehicle;
import exceptions.InvalidOperationException;

public abstract class WaterVehicle extends Vehicle {
    private final boolean hasSail;
    protected WaterVehicle(String id, String model, double maxSpeed, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed);
        this.hasSail = hasSail;
    }
    public boolean hasSail() { return hasSail; }
    @Override
    public double estimateJourneyTime(double distance) throws InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative");
        if (distance == 0) return 0.0;
        double base = distance / getMaxSpeed();
        return base * 1.15;
    }
}
