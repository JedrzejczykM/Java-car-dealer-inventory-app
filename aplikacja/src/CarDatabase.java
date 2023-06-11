import java.io.*;
import java.util.ArrayList;
import java.util.List;

class CarDatabase {
    private List<Car> cars;
    private String filename;

    public CarDatabase(String filename) {
        cars = new ArrayList<>();
        this.filename = filename;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Car car : cars) {
                writer.println(car.getBrand() + "," + car.getModel() + "," + car.getYear());
            }
            System.out.println("Baza danych została zapisana do pliku");
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        cars.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String brand = parts[0];
                    String model = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    Car car = new Car(brand, model, year);
                    cars.add(car);
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu z pliku: " + e.getMessage());
        }
    }

    public List<Car> searchByBrand(String brand) {
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if (car.getBrand().equalsIgnoreCase(brand)) {
                results.add(car);
            }
        }
        return results;
    }
}