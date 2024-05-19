package inkstitutionUI;

import java.awt.BorderLayout;


import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgPregledKorisnika extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    JTextArea textAreaPregled; 
    ArrayList<korisnik> korisnici = new ArrayList<korisnik>();
    public static void main(String[] args) {
        try {
            DlgPregledKorisnika dialog = new DlgPregledKorisnika();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DlgPregledKorisnika() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 416, 137);
        contentPanel.add(scrollPane);

        
        textAreaPregled = new JTextArea();
        scrollPane.setViewportView(textAreaPregled);

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton nazadButton = new JButton("Nazad");
                nazadButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		dispose();
                	}
                });
                nazadButton.setActionCommand("Cancel");
                buttonPane.add(nazadButton);
            }
        }
        selectPregledKorisnika();
    }

    private void selectPregledKorisnika() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection conn =
                DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
                    "user=aplese&password=11");
            String sql = "SELECT Username,Email,AdminRights FROM User";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String tekst = "";
            while (rs.next()) {
                tekst += "Username: " + rs.getString("Username") + "\t";
                tekst += "E-mail " + rs.getString("Email") + "\n";
                tekst += "Admin prava " + rs.getString("AdminRights") + "\n";
                korisnik korisnik=new korisnik(rs.getString("Username"));
                korisnici.add(korisnik);
            }
            textAreaPregled.setText(tekst);
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    ArrayList<korisnik> getKorisnici(){
    	return korisnici;
    }
}

