import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

// Klasa główna aplikacji z interfejsem użytkownika
public class CarDatabaseApp extends JFrame {
    private CarDatabase database;
    private JTextArea outputArea;
    private JTextField brandField;
    private JButton searchButton;
    private void addCarManually() {
        String brand = JOptionPane.showInputDialog("Podaj markę samochodu:");
        String model = JOptionPane.showInputDialog("Podaj model samochodu:");
        String yearStr = JOptionPane.showInputDialog("Podaj rok produkcji samochodu:");
        int year = Integer.parseInt(yearStr);

        Car car = new Car(brand, model, year);
        database.addCar(car);
        database.saveToFile();
    }

    public CarDatabaseApp() {
        super("Baza Danych Samochodów");
        database = new CarDatabase("C:\\Users\\Michał\\Desktop\\STUDIA\\4 SEM\\PROO\\projekt\\CarDataBase\\src\\database.txt");
        database.loadFromFile();

        // Utworzenie interfejsu użytkownika
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JLabel brandLabel = new JLabel("Marka:");
        brandField = new JTextField(10);
        searchButton = new JButton("Szukaj");

        JPanel inputPanel = new JPanel();
        inputPanel.add(brandLabel);
        inputPanel.add(brandField);
        inputPanel.add(searchButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        JButton addButton = new JButton("Dodaj");

        // Dodanie obsługi zdarzenia przycisku "Dodaj"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCarManually();
            }
        });
        inputPanel.add(addButton);
        // Obsługa zdarzenia przycisku "Szukaj"
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String brand = brandField.getText().trim();
                if (!brand.isEmpty()) {
                    List<Car> searchResults = database.searchByBrand(brand);
                    displaySearchResults(searchResults);
                }
            }
        });

        // Ustawienia okna aplikacji
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Wczytanie bazy danych z pliku
        database.loadFromFile();
    }

    // Wyświetlanie wyników wyszukiwania w polu tekstowym
    private void displaySearchResults(List<Car> results) {
        outputArea.setText("");
        if (!results.isEmpty()) {
            for (Car car : results) {
                outputArea.append(car.toString() + "\n");
            }
        } else {
            outputArea.append("Brak samochodów o podanej marce.\n");
        }
        database.saveToFile();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CarDatabaseApp();
            }
        });
    }
}