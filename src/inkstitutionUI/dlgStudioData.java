package inkstitutionUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.Font;

public class dlgStudioData extends JDialog {
    private String studioName;
    private String username;
    
    public dlgStudioData(final String studioName, final String username) {
        this.studioName = studioName;
        this.username = username;
        setTitle("Detalji studija: " + studioName);
        setBounds(100, 100, 400, 345);
        getContentPane().setLayout(null);

        JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsTextArea);
        scrollPane.setBounds(0, 0, 386, 163);
        getContentPane().add(scrollPane);

        JLabel reservationLabel = new JLabel("Odaberite datum:");
        reservationLabel.setBounds(10, 173, 109, 30);
        getContentPane().add(reservationLabel);

        final JDateChooser datum = new JDateChooser();
        datum.setBounds(118, 173, 150, 30);
        getContentPane().add(datum);

        loadStudioDetails(detailsTextArea);

        JLabel lblOdaberiteVrijeme = new JLabel("Odaberite vrijeme:");
        lblOdaberiteVrijeme.setBounds(10, 213, 117, 30);
        getContentPane().add(lblOdaberiteVrijeme);

        final JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{" ", "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"}));
        comboBox.setBounds(118, 222, 150, 21);
        getContentPane().add(comboBox);

        JButton btnNewButton = new JButton("Rezervirajte!");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                long selectedDateInMillis = datum.getDate().getTime();
                String selectedTime = (String) comboBox.getSelectedItem();

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
                        "user=aplese&password=11")) {

                    String getUserIDQuery = "SELECT UserID FROM User WHERE Username = ?";
                    int userID = 0;
                    try (PreparedStatement userIDStatement = connection.prepareStatement(getUserIDQuery)) {
                        userIDStatement.setString(1, username);
                        try (ResultSet userIDResultSet = userIDStatement.executeQuery()) {
                            if (userIDResultSet.next()) {
                                userID = userIDResultSet.getInt("UserID");
                            } else {
                                JOptionPane.showMessageDialog(null, "Korisnik nije pronađen.");
                                return;
                            }
                        }
                    }

                    String getStudioIDQuery = "SELECT StudioID FROM TattooStudio WHERE StudioName = ?";
                    int studioID = 0;
                    try (PreparedStatement studioIDStatement = connection.prepareStatement(getStudioIDQuery)) {
                        studioIDStatement.setString(1, studioName);
                        try (ResultSet studioIDResultSet = studioIDStatement.executeQuery()) {
                            if (studioIDResultSet.next()) {
                                studioID = studioIDResultSet.getInt("StudioID");
                            } else {
                                JOptionPane.showMessageDialog(null, "Studio nije pronađen.");
                                return;
                            }
                        }
                    }

                    // Insert the new appointment with UserID and StudioID
                    String insertAppointmentQuery = "INSERT INTO Appointment (StudioName, AppointmentDate, AppointmentTime, Username, UserID, StudioID) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertAppointmentStatement = connection.prepareStatement(insertAppointmentQuery)) {
                        insertAppointmentStatement.setString(1, studioName);
                        java.sql.Date selectedDate = new java.sql.Date(selectedDateInMillis);
                        insertAppointmentStatement.setDate(2, selectedDate);
                        insertAppointmentStatement.setString(3, selectedTime);
                        insertAppointmentStatement.setString(4, username);
                        insertAppointmentStatement.setInt(5, userID);
                        insertAppointmentStatement.setInt(6, studioID);
                        
                        int rowsAffected = insertAppointmentStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Rezervacija uspješno spremljena u bazu podataka!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Greška pri spremanju rezervacije.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Greška pri spremanju rezervacije u bazu podataka: " + ex.getMessage());
                }
            }
        });
        btnNewButton.setBounds(101, 263, 192, 30);
        getContentPane().add(btnNewButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadStudioDetails(JTextArea detailsTextArea) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
                "user=aplese&password=11")) {
            String query = "SELECT StudioName, StudioAddress, StudioContact FROM TattooStudio WHERE StudioName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, studioName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String studioDetails = "Ime: " + resultSet.getString("StudioName") + "\n";
                        studioDetails += "Adresa: " + resultSet.getString("StudioAddress") + "\n";
                        studioDetails += "Broj telefona: " + resultSet.getString("StudioContact") + "\n";

                        detailsTextArea.setText(studioDetails);
                    } else {
                        detailsTextArea.setText("Nema podataka za odabrani studio.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            detailsTextArea.setText("Greška prilikom dohvaćanja podataka.");
        }
    }
}
