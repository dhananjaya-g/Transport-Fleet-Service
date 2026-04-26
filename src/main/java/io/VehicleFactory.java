package io;
import exceptions.InvalidOperationException;
import vehicles.Vehicle;
import vehicles.land.vehicle.Car;
import vehicles.land.vehicle.Truck;
import vehicles.land.vehicle.Bus;
import vehicles.air.vehicle.Airplane;
import vehicles.water.vehicle.CargoShip;
public class VehicleFactory {
    public static Vehicle createFromCsv(String line) throws InvalidOperationException {
        String[] t = line.split(",");
        if (t.length < 1) throw new InvalidOperationException("Empty CSV line");
        try {
            return switch (t[0].trim()) {
                case "Car" -> createCar(t);
                case "Truck" -> createTruck(t);
                case "Bus" -> createBus(t);
                case "Airplane" -> createAirplane(t);
                case "CargoShip" -> createCargoShip(t);
                default -> throw new InvalidOperationException("Unknown type: " + t[0]);
            };
        } catch (NumberFormatException e) {
            throw new InvalidOperationException("Bad numeric in line: " + line);
        }
    }
    private static Vehicle  createCar(String[] t) throws InvalidOperationException {
        if (t.length != 10) throw new InvalidOperationException("Car needs 10 tokens");
        Car c = new Car(t[1], t[2], Double.parseDouble(t[3]), Integer.parseInt(t[4]), Integer.parseInt(t[5]));
        int pax = Integer.parseInt(t[6]); double fuel = Double.parseDouble(t[7]);
        double mileage = Double.parseDouble(t[8]); boolean maint = Boolean.parseBoolean(t[9]);
        try { if (pax > 0) c.boardPassengers(pax); if (fuel > 0) c.refuel(fuel); } catch (Exception e) { throw new InvalidOperationException(e.getMessage()); }
        if (mileage > 0) c.setCurrentMileage(mileage); if (maint) c.scheduleMaintenance();
        return c;
    }
    private static Vehicle createTruck(String[] t) throws InvalidOperationException {
        if (t.length != 10) throw new InvalidOperationException("Truck needs 10 tokens");
        Truck tr = new Truck(t[1], t[2], Double.parseDouble(t[3]), Integer.parseInt(t[4]), Double.parseDouble(t[5]));
        double cargo = Double.parseDouble(t[6]); double fuel = Double.parseDouble(t[7]);
        double mileage = Double.parseDouble(t[8]); boolean maint = Boolean.parseBoolean(t[9]);
        try { if (cargo > 0) tr.loadCargo(cargo); if (fuel > 0) tr.refuel(fuel); } catch (Exception e) { throw new InvalidOperationException(e.getMessage()); }
        if (mileage > 0) tr.setCurrentMileage(mileage); if (maint) tr.scheduleMaintenance();
        return tr;
    }
    private static Vehicle createBus(String[] t) throws InvalidOperationException {
        if (t.length != 12) throw new InvalidOperationException("Bus needs 12 tokens");
        Bus b = new Bus(t[1], t[2], Double.parseDouble(t[3]), Integer.parseInt(t[4]), Integer.parseInt(t[5]), Double.parseDouble(t[7]));
        int pax = Integer.parseInt(t[6]); double cargo = Double.parseDouble(t[8]);
        double fuel = Double.parseDouble(t[9]); double mileage = Double.parseDouble(t[10]); boolean maint = Boolean.parseBoolean(t[11]);
        try { if (pax > 0) b.boardPassengers(pax); if (cargo > 0) b.loadCargo(cargo); if (fuel > 0) b.refuel(fuel); } catch (Exception e) { throw new InvalidOperationException(e.getMessage()); }
        if (mileage > 0) b.setCurrentMileage(mileage); if (maint) b.scheduleMaintenance();
        return b;
    }
    private static Vehicle createAirplane(String[] t) throws InvalidOperationException {
        if (t.length != 12) throw new InvalidOperationException("Airplane needs 12 tokens");
        Airplane a = new Airplane(t[1], t[2], Double.parseDouble(t[3]), Double.parseDouble(t[4]), Integer.parseInt(t[5]), Double.parseDouble(t[7]));
        int pax = Integer.parseInt(t[6]); double cargo = Double.parseDouble(t[8]); double fuel = Double.parseDouble(t[9]);
        double mileage = Double.parseDouble(t[10]); boolean maint = Boolean.parseBoolean(t[11]);
        try { if (pax > 0) a.boardPassengers(pax); if (cargo > 0) a.loadCargo(cargo); if (fuel > 0) a.refuel(fuel); } catch (Exception e) { throw new InvalidOperationException(e.getMessage()); }
        if (mileage > 0) a.setCurrentMileage(mileage); if (maint) a.scheduleMaintenance();
        return a;
    }
    private static Vehicle createCargoShip(String[] t) throws InvalidOperationException {
        if (t.length != 10) throw new InvalidOperationException("CargoShip needs 10 tokens");
        boolean sail = Boolean.parseBoolean(t[4]);
        CargoShip s = new CargoShip(t[1], t[2], Double.parseDouble(t[3]), sail, Double.parseDouble(t[5]));
        double cargo = Double.parseDouble(t[6]); double fuel = Double.parseDouble(t[7]); double mileage = Double.parseDouble(t[8]); boolean maint = Boolean.parseBoolean(t[9]);
        try { if (cargo > 0) s.loadCargo(cargo); if (!sail && fuel > 0) s.refuel(fuel); } catch (Exception e) { throw new InvalidOperationException(e.getMessage()); }
        if (mileage > 0) s.setCurrentMileage(mileage); if (maint) s.scheduleMaintenance();
        return s;
    }
}
