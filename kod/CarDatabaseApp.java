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
    public void addCarManually() {
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
            database = new CarDatabase("C:\\Users\\Michał\\Desktop\\STUDIA\\4 SEM\\PROO\\projekt\\kod\\database.txt");
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                database.loadFromFile();
            }
        };
        Thread thread1 = new Thread(runnable1);
        thread1.start();

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
                Runnable runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        addCarManually();
                    }
                };
                Thread thread2 = new Thread(runnable2);
                thread2.start();
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
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                database.loadFromFile();
            }
        };
        Thread thread3 = new Thread(runnable3);
        thread3.start();
        try {
            thread1.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
