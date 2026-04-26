package fleet;
import vehicles.Vehicle;
import services.FuelConsumable;
import services.Maintainable;
import java.util.*;
import java.util.stream.Collectors;
import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

public class FleetManager {
    private final List<Vehicle> fleet = new ArrayList<>();
    private final Set<String> distinctModels = new HashSet<>();
    private final TreeSet<String> modelsAlphabetical = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private static String canon(String s) { return s == null ? null : s.toLowerCase(Locale.ROOT); }

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        if (v == null) throw new InvalidOperationException("Vehicle cannot be null");
        for (Vehicle existing : fleet) {
            if (existing.getId().equalsIgnoreCase(v.getId())) {
                throw new InvalidOperationException("Duplicate ID: " + v.getId());
            }
        }
        if (!distinctModels.add(v.getModel())) {
            // allowed, but we still maintain a set; we won't block add, just track uniqueness
        }
        modelsAlphabetical.add(v.getModel());
        fleet.add(v);
    }

    public boolean removeVehicle(String id) {
        Iterator<Vehicle> it = fleet.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            Vehicle v = it.next();
            if (v.getId().equalsIgnoreCase(id)) {
                it.remove();
                removed = true;
            }
        }
        // rebuild model sets (simpler and safe)
        rebuildModelSets();
        return removed;
    }

    private void rebuildModelSets() {
        distinctModels.clear();
        modelsAlphabetical.clear();
        for (Vehicle v : fleet) {
            distinctModels.add(v.getModel());
            modelsAlphabetical.add(v.getModel());
        }
    }

    public List<Vehicle> getFleet() { return Collections.unmodifiableList(fleet); }
    public Set<String> getDistinctModels() { return Collections.unmodifiableSet(distinctModels); }
    public TreeSet<String> getModelsAlphabetical() { return new TreeSet<>(modelsAlphabetical); }
    public List<Vehicle> sortByModel() {
        return fleet.stream()
                .sorted(
                        Comparator.comparing(
                                Vehicle::getModel,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                        ).thenComparing(v -> v.getId().toLowerCase(java.util.Locale.ROOT))
                )
                .toList();
    }


    public List<Vehicle> sortByMaxSpeed() {
        return fleet.stream()
                .sorted(Comparator.comparingDouble(Vehicle::getMaxSpeed).reversed())
                .collect(Collectors.toList());
    }

    public List<Vehicle> sortByEfficiency() {
        return fleet.stream()
                .sorted(Comparator.comparingDouble(Vehicle::calculateFuelEfficiency).reversed())
                .collect(Collectors.toList());
    }
    public Optional<Vehicle> getFastest() {
        return fleet.stream().max(Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public Optional<Vehicle> getSlowest() {
        return fleet.stream().min(Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }


//    public void clear() { fleet.clear(); }


    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Fleet Summary ===\n");
        sb.append("Total vehicles: ").append(fleet.size()).append('\n');
        sb.append("Distinct models: ").append(distinctModels.size()).append(" -> ").append(modelsAlphabetical).append('\n');
        getFastest().ifPresent(v -> sb.append("Fastest: ").append(v.getModel()).append(" (").append(v.getMaxSpeed()).append(" km/h)\n"));
        getSlowest().ifPresent(v -> sb.append("Slowest: ").append(v.getModel()).append(" (").append(v.getMaxSpeed()).append(" km/h)\n"));
        double avgEff = fleet.stream().mapToDouble(Vehicle::calculateFuelEfficiency).average().orElse(0.0);
        sb.append(String.format("Average efficiency: %.2f km/l%n", avgEff));
        return sb.toString();
    }
}
