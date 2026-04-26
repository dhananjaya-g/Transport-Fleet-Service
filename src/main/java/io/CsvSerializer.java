package io;
import vehicles.Vehicle;
import vehicles.land.vehicle.Car;
import vehicles.land.vehicle.Truck;
import vehicles.land.vehicle.Bus;
import vehicles.air.vehicle.Airplane;
import vehicles.water.vehicle.CargoShip;
import java.io.*;
import java.util.*;
public class CsvSerializer {
    public void saveToFile(List<Vehicle> fleet, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            out.println("# Transport fleet MetaData #");
            for (Vehicle v : fleet) out.println(toCsv(v));
        }
    }
    private String toCsv(Vehicle v) {
        if (v instanceof Car c) {
            return String.join(",", "Car", c.getId(), c.getModel(),
                String.valueOf(c.getMaxSpeed()), String.valueOf(c.getNumWheels()),
                String.valueOf(c.getPassengerCapacity()), String.valueOf(c.getCurrentPassengers()),
                String.valueOf(c.getFuelLevel()), String.valueOf(c.getCurrentMileage()),
                String.valueOf(c.needsMaintenance()));
        } else if (v instanceof Truck t) {
            return String.join(",", "Truck", t.getId(), t.getModel(),
                String.valueOf(t.getMaxSpeed()), String.valueOf(t.getNumWheels()),
                String.valueOf(t.getCargoCapacity()), String.valueOf(t.getCurrentCargo()),
                String.valueOf(t.getFuelLevel()), String.valueOf(t.getCurrentMileage()),
                String.valueOf(t.needsMaintenance()));
        } else if (v instanceof Bus b) {
            return String.join(",", "Bus", b.getId(), b.getModel(),
                String.valueOf(b.getMaxSpeed()), String.valueOf(b.getNumWheels()),
                String.valueOf(b.getPassengerCapacity()), String.valueOf(b.getCurrentPassengers()),
                String.valueOf(b.getCargoCapacity()), String.valueOf(b.getCurrentCargo()),
                String.valueOf(b.getFuelLevel()), String.valueOf(b.getCurrentMileage()),
                String.valueOf(b.needsMaintenance()));
        } else if (v instanceof Airplane a) {
            return String.join(",", "Airplane", a.getId(), a.getModel(),
                String.valueOf(a.getMaxSpeed()), String.valueOf(a.getMaxAltitude()),
                String.valueOf(a.getPassengerCapacity()), String.valueOf(a.getCurrentPassengers()),
                String.valueOf(a.getCargoCapacity()), String.valueOf(a.getCurrentCargo()),
                String.valueOf(a.getFuelLevel()), String.valueOf(a.getCurrentMileage()),
                String.valueOf(a.needsMaintenance()));
        } else if (v instanceof CargoShip s) {
            return String.join(",", "CargoShip", s.getId(), s.getModel(),
                String.valueOf(s.getMaxSpeed()), String.valueOf(s.hasSail()),
                String.valueOf(s.getCargoCapacity()), String.valueOf(s.getCurrentCargo()),
                String.valueOf(s.getFuelLevel()), String.valueOf(s.getCurrentMileage()),
                String.valueOf(s.needsMaintenance()));
        }
        throw new IllegalArgumentException("Unknown vehicle type " + v.getClass().getSimpleName());
    }
    public List<Vehicle> loadFromFile(String filename) throws IOException, exceptions.InvalidOperationException {
        List<Vehicle> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                out.add(VehicleFactory.createFromCsv(line));
            }
        }
        return out;
    }
}
