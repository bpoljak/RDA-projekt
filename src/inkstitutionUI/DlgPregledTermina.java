package inkstitutionUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgPregledTermina extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextArea textTermini;
    DlgLoggedIn dlg;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DlgPregledTermina dialog = new DlgPregledTermina("username");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     * @param username 
     */
    public DlgPregledTermina(String username) {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        textTermini = new JTextArea();
        textTermini.setEditable(false); // Optional: make the text area non-editable

        JScrollPane scrollPane = new JScrollPane(textTermini);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton nazadButton = new JButton("Nazad");
        nazadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        buttonPane.add(nazadButton);

        getTermini(username);
    }

    private void getTermini(String username) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
                "user=aplese&password=11")) {
            String query = "SELECT Username, StudioName, AppointmentDate, AppointmentTime FROM Appointment WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    StringBuilder popisTermina = new StringBuilder();
                    while (resultSet.next()) {
                        popisTermina.append("Ime Korisnika: ").append(resultSet.getString("Username")).append("\n");
                        popisTermina.append("Ime Studija: ").append(resultSet.getString("StudioName")).append("\n");
                        popisTermina.append("Datum termina: ").append(resultSet.getString("AppointmentDate")).append("\n");
                        popisTermina.append("Vrijeme Termina: ").append(resultSet.getString("AppointmentTime")).append("\n\n");
                    }
                    if (popisTermina.length() == 0) {
                        textTermini.setText("Nema podataka o rezerviranim terminima.");
                    } else {
                        textTermini.setText(popisTermina.toString());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            textTermini.setText("Greška prilikom dohvaćanja podataka.");
        }
    }
}
