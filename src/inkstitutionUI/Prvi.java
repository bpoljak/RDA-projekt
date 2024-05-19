package inkstitutionUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.ResultSet;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JPasswordField;

public class Prvi {

	private JFrame frame;
	private JTextField textFieldUserLOGIN;
	private JTextField Username;
	private JTextField Email;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JButton btnNewButtonLOGIN;
	private JButton btnNewButton_1;
	private JPasswordField passwordFieldLOGIN;
	private JPasswordField Password;
	DlgPregledKorisnika dlg;
	DlgLoggedIn dlg1;
	DlgAdminLogIn dlg2;
	public static korisnik korisnik;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prvi window = new Prvi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Prvi() {
		initialize();
	}

	
	private void initialize() {
	    frame = new JFrame();
	    frame.setBounds(100, 100, 645, 438);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().setLayout(null);
	    
	    JLabel lblNewLabel = new JLabel("Inkstitution");
	    lblNewLabel.setFont(new Font("Old English Text MT", Font.PLAIN, 40));
	    lblNewLabel.setBounds(214, 10, 208, 47);
	    frame.getContentPane().add(lblNewLabel);
	    
	    textFieldUserLOGIN = new JTextField();
	    textFieldUserLOGIN.setBounds(71, 145, 148, 25);
	    frame.getContentPane().add(textFieldUserLOGIN);
	    textFieldUserLOGIN.setColumns(10);
	    
	    Username = new JTextField();
	    Username.setColumns(10);
	    Username.setBounds(437, 145, 148, 25);
	    frame.getContentPane().add(Username);
	    
	    Email = new JTextField();
	    Email.setColumns(10);
	    Email.setBounds(437, 240, 148, 25);
	    frame.getContentPane().add(Email);
	    
	    JLabel lblNewLabel_1 = new JLabel("Prijavite se");
	    lblNewLabel_1.setFont(new Font("Tw Cen MT", Font.PLAIN, 25));
	    lblNewLabel_1.setBounds(86, 76, 117, 40);
	    frame.getContentPane().add(lblNewLabel_1);
	    
	    JLabel lblNewLabel_1_1 = new JLabel("Registrirajte se");
	    lblNewLabel_1_1.setFont(new Font("Tw Cen MT", Font.PLAIN, 25));
	    lblNewLabel_1_1.setBounds(437, 76, 156, 40);
	    frame.getContentPane().add(lblNewLabel_1_1);
	    
	    JLabel lblNewLabel_2 = new JLabel("Username:");
	    lblNewLabel_2.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	    lblNewLabel_2.setBounds(10, 151, 52, 13);
	    frame.getContentPane().add(lblNewLabel_2);
	    
	    lblNewLabel_3 = new JLabel("Password:");
	    lblNewLabel_3.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	    lblNewLabel_3.setBounds(10, 200, 52, 13);
	    frame.getContentPane().add(lblNewLabel_3);
	    
	    lblNewLabel_4 = new JLabel("Username:");
	    lblNewLabel_4.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	    lblNewLabel_4.setBounds(387, 151, 52, 13);
	    frame.getContentPane().add(lblNewLabel_4);
	    
	    lblNewLabel_5 = new JLabel("Password:");
	    lblNewLabel_5.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	    lblNewLabel_5.setBounds(387, 200, 52, 13);
	    frame.getContentPane().add(lblNewLabel_5);
	    
	    lblNewLabel_6 = new JLabel("Email:");
	    lblNewLabel_6.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	    lblNewLabel_6.setBounds(387, 246, 52, 13);
	    frame.getContentPane().add(lblNewLabel_6);
	    
	    btnNewButtonLOGIN = new JButton("LOG IN");
	    btnNewButtonLOGIN.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String loginUsername = textFieldUserLOGIN.getText();
	            char[] loginPass = passwordFieldLOGIN.getPassword();
	            String loginPassString = new String(loginPass); // Pretvaranje niza znakova u string
	            
	            try {
	                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	                Connection conn = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
	                        "user=aplese&password=11");
	                String strQuery = "SELECT * FROM User WHERE (Username = ? AND UPassword= ?)";
	                PreparedStatement statement = conn.prepareStatement(strQuery);
	                statement.setString(1, loginUsername);
	                statement.setString(2, loginPassString); 
	                ResultSet rs = statement.executeQuery(); 
	                if (!rs.next()) { // Provjera
	                    JOptionPane.showMessageDialog(null, "Krivi username ili password!");
	                } else {
	                    JOptionPane.showMessageDialog(null, "Autentikacija uspješna!");
	                    korisnik = new korisnik(loginUsername);
	                    dlg1 = new DlgLoggedIn(korisnik);
	    	            dlg1.setVisible(true);
	    	            
	                }
	                rs.close(); 
	                statement.close();
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(null, "Greška :" + ex.toString());
	            }
	        }
	    });
	    btnNewButtonLOGIN.setBounds(86, 291, 85, 21);
	    frame.getContentPane().add(btnNewButtonLOGIN);
	    
	    btnNewButton_1 = new JButton("SIGN UP");
	    btnNewButton_1.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            
	            String connectionUrl = "jdbc:mysql://ucka.veleri.hr:3306/aplese";
	            String user = Username.getText();
	            char[] pass = Password.getPassword();
	            String passString = new String(pass);
	            String email = Email.getText();
	            try {
	                
	                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	                Connection conn = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr/aplese?" +
	                                                               "user=aplese&password=11");
	                String strQuery = "INSERT INTO User(Username,UPassword,Email,AdminRights) VALUES (?,?,?,0)";
	                PreparedStatement statement = conn.prepareStatement(strQuery);
	                statement.setString(1, user);
	                statement.setString(2, passString);
	                statement.setString(3, email);
	                statement.executeUpdate();
	                statement.close();
	                JOptionPane.showMessageDialog(null, "Korisnik uspješno dodan!");
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(null, "Greška pri dodavanju korisnika: " + ex.toString());
	            }
	        }
	    });

	    btnNewButton_1.setBounds(465, 291, 85, 21);
	    frame.getContentPane().add(btnNewButton_1);
	    
	    JSeparator separator = new JSeparator();
	    separator.setForeground(Color.BLACK);
	    separator.setOrientation(SwingConstants.VERTICAL);
	    separator.setBounds(308, 72, 16, 319);
	    frame.getContentPane().add(separator);
	    
	    passwordFieldLOGIN = new JPasswordField();
	    passwordFieldLOGIN.setBounds(71, 197, 148, 25);
	    frame.getContentPane().add(passwordFieldLOGIN);
	    
	    Password = new JPasswordField();
	    Password.setBounds(437, 197, 148, 25);
	    frame.getContentPane().add(Password);
	    
	    JButton btn_PregledKorisnika = new JButton("Pregled korisnika");
	    btn_PregledKorisnika.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            dlg2 = new DlgAdminLogIn();
	            dlg2.setVisible(true);
	            
	        }
	    });
	    btn_PregledKorisnika.setBounds(465, 10, 139, 21);
	    frame.getContentPane().add(btn_PregledKorisnika);
	}};
