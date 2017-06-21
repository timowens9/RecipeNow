package RecipeNow;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;

public class NewUserView extends JFrame {

    private JPanel contentPane; //declare variable
    private JTextField txtUser;
    private JButton btnSignup;
    private JTextField txtPassword;
    private NewUserCntl userCntl;
    private DatabaseHelper db = new DatabaseHelper();

    public NewUserView(NewUserCntl parentuserCntl) {

        userCntl = parentuserCntl;
        setTitle("New User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        txtUser = new JTextField();
        txtUser = new JTextField();
        btnSignup = new JButton("Sign Up");

        txtUser.setBounds(188, 51, 99, 20);
        contentPane.add(txtUser);
        txtUser.setColumns(10);
        txtPassword = new JTextField();
        txtPassword.setBounds(188, 106, 99, 20);

        contentPane.add(txtPassword);
        txtPassword.setColumns(10);
        btnSignup.setBounds(188, 165, 89, 23);
        contentPane.add(btnSignup);

        JLabel lblUserName = new JLabel("User Name");
        lblUserName.setBounds(70, 54, 86, 14);
        contentPane.add(lblUserName);
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(70, 109, 86, 14);
        contentPane.add(lblPassword);

        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String username = txtUser.getText();
                String password = txtPassword.getText();

                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(null, " name or password is Incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else 
                {
                    String IQuery = "INSERT INTO `recipe_users`(`username`,`password`) VALUES('" + username + "', '" + password + "')";
                    System.out.println(IQuery);//print on console
                    System.out.println("Connecting to a selected database...");
                    
                    try {
                        db.insertIntoTable(username, password);
                        String SMessage = "Record added for "+username;
                         JOptionPane.showMessageDialog(null,SMessage,"Message",JOptionPane.PLAIN_MESSAGE);
                    } catch (SQLException ex) {
                        Logger.getLogger(NewUserView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        db.closeConnection();
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    } catch (SQLException ex) {
                        Logger.getLogger(NewUserView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });

    }
}
