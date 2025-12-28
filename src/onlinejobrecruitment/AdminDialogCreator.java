/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;

public class AdminDialogCreator extends DialogCreator {
    @Override
    public JDialog createDialog(JFrame parent) {
        JOptionPane.showMessageDialog(parent,
            "Admin accounts cannot be self-registered.\n" +
            "Please contact the system administrator.",
            "Admin Registration",
            JOptionPane.INFORMATION_MESSAGE);
        return null;
    }
}
