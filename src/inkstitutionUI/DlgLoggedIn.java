package inkstitutionUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import inkstitutionUI.dlgStudioData;


public class DlgLoggedIn extends JDialog {

    private static final long serialVersionUID = 1L;
	protected static final DlgPregledTermina DlgPregledTermina = null;
    private final JPanel contentPanel = new JPanel();
    private DefaultListModel<String> listModel;
    private korisnik user;
    DlgPregledTermina dlg;

    
    
    
    public DlgLoggedIn(korisnik user) {
        this.user = user;
        initializeDialog();
    }

    
    public DlgLoggedIn() {
        initializeDialog();
    }

    
    private void initializeDialog() {
        setTitle("Dobrodošli " + user.username);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        this.user.username = user.username;

        // Initialize the list model
        listModel = new DefaultListModel<>();
        final JList<String> list = new JList<>(listModel);
        list.setBounds(10, 10, 416, 212);
        contentPanel.add(list);

        populateList();

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Nazad");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                {
                	JButton btnPregledTermina = new JButton("Pregled termina");
                	btnPregledTermina.addActionListener(new ActionListener() {
                		public void actionPerformed(ActionEvent arg0) {
                			dlg = new DlgPregledTermina(user.username);
                			dlg.setVisible(true);
                		}
                	});
                	buttonPane.add(btnPregledTermina);
                }
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
            {
                JButton redirectButton = new JButton("Idi na studio");
                redirectButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        String selectedStudio = list.getSelectedValue();
                        if (selectedStudio != null) {
                            new dlgStudioData(selectedStudio, user.username);
                        }
                    }
                });
                buttonPane.add(redirectButton);
            }
        }
    }

    private void populateList() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
                "user=aplese&password=11")) {
            String query = "SELECT StudioName FROM TattooStudio";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String studioName = resultSet.getString("StudioName");
                        listModel.addElement(studioName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
