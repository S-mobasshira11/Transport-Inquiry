/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author HP
 */
public class TransportType extends JFrame implements ActionListener {
    
    JComboBox comboBox;
    
    TransportType()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        
        String[] transports={"Bus","Train"};
        comboBox = new JComboBox(transports);
        comboBox.addActionListener(this);
        
        this.add(comboBox);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==comboBox)
        {
            System.out.println(comboBox.getSelectedItem());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
