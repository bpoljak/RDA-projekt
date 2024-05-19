package inkstitutionUI;

import java.awt.BorderLayout;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;


public class DlgAdminLogIn extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldADMIN;
	private JPasswordField passwordFieldADMIN;
	DlgPregledKorisnika dlg;

	
	public static void main(String[] args) {
		try {
			DlgAdminLogIn dialog = new DlgAdminLogIn();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public DlgAdminLogIn() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Potrebna su \r\nadministratorska prava \r\nza nastavak");
			lblNewLabel.setBounds(34, 10, 375, 31);
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
			contentPanel.add(lblNewLabel);
		}
		{
			textFieldADMIN = new JTextField();
			textFieldADMIN.setColumns(10);
			textFieldADMIN.setBounds(145, 55, 148, 25);
			contentPanel.add(textFieldADMIN);
		}
		{
			JLabel ADMINusername = new JLabel("Username:");
			ADMINusername.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
			ADMINusername.setBounds(84, 61, 52, 13);
			contentPanel.add(ADMINusername);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Password:");
			lblNewLabel_3.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
			lblNewLabel_3.setBounds(84, 110, 52, 13);
			contentPanel.add(lblNewLabel_3);
		}
		{
			passwordFieldADMIN = new JPasswordField();
			passwordFieldADMIN.setBounds(145, 107, 148, 25);
			contentPanel.add(passwordFieldADMIN);
		}
		{
			JButton adminlogin = new JButton("LOG IN");
		    adminlogin.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	String loginUsername = textFieldADMIN.getText();
		            char[] loginPass = passwordFieldADMIN.getPassword();
		            String loginPassString = new String(loginPass); // Pretvaranje niza znakova u string
		            
		            try {
		                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		                Connection conn = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
		                        "user=aplese&password=11");
		                String strQuery = "SELECT * FROM User WHERE (Username = ? AND UPassword = ? AND AdminRights = 1)";
		                PreparedStatement statement = conn.prepareStatement(strQuery);
		                statement.setString(1, loginUsername);
		                statement.setString(2, loginPassString); 
		                ResultSet rs = statement.executeQuery(); 
		                if (!rs.next()) { // Provjera
		                    JOptionPane.showMessageDialog(null, "Nepostojeća administratorska prava!");
		                } else {
		                    JOptionPane.showMessageDialog(null, "Autentikacija uspješna!");
		        	
		        	
		            dlg = new DlgPregledKorisnika();
		            dlg.setVisible(true);
		                }
		                rs.close(); 
		                statement.close();
		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(null, "Greška :" + ex.toString());
		            }
		        }
		    });
			adminlogin.setBounds(160, 201, 85, 21);
			contentPanel.add(adminlogin);
		}
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
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
