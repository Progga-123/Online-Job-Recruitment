/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;
import javax.swing.*;

/**
 * Abstract Creator - defines the factory method
 */
public abstract class DialogCreator {
    
    // Factory Method - subclasses implement this
    public abstract JDialog createDialog(JFrame parent);
    
    // Common method that uses the factory method
    public void showDialog(JFrame parent) {
        JDialog dialog = createDialog(parent);
        if (dialog != null) {
            dialog.setVisible(true);
        }
    }
}