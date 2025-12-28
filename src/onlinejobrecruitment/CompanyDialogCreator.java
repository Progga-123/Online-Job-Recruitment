/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;

public class CompanyDialogCreator extends DialogCreator {
    @Override
    public JDialog createDialog(JFrame parent) {
        return new CompanyRegistrationDialog(parent);
    }
}