/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class AddTransportRoute extends javax.swing.JFrame {

    /**
     * Creates new form AddTransportRoute
     */
    
    PreparedStatement preparedStatement ;
    Connection connection;
    ResultSet resultset;
    
    public AddTransportRoute() {
         Color color = new Color(186, 79, 84);
        getContentPane().setBackground(color);
        initComponents();
        fillcombobox();
        fillroutecombobox();
    }
    
    public void connect()
    {
            try
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager
               .getConnection(
               "jdbc:sqlserver://localhost:1433;databaseName=Transport_Enquiry;selectMethod=cursor", "sa", "123456");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }
    
    public void fillcombobox()
    {
        try
        {
            connect();
            String sql="select Transport_ID from TRANSPORTS";
            preparedStatement=connection.prepareStatement(sql);
            resultset=preparedStatement.executeQuery();
            
            while(resultset.next())
            {
                String id = resultset.getString("Transport_ID");
                jComboBox1.addItem(id);
                
            }
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void get_Type()
    {
        String id=jComboBox1.getSelectedItem().toString();
        if(id=="N/A")Type.setText("N/A");
        
        if(id!="N/A"){
            try
            {
                connect();

                String sql="select TransportType from TRANSPORTS where Transport_ID=?";
                preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,id);
                resultset=preparedStatement.executeQuery();

                while(resultset.next())
                {
                    Type.setText(resultset.getString("TransportType"));

                }


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void fillroutecombobox()
    {
        try
        {
            connect();
            String sql="SELECT ROUTENAME FROM ROUTEDETAILS";
            preparedStatement=connection.prepareStatement(sql);
            resultset=preparedStatement.executeQuery();
            
            while(resultset.next())
            {
                String id = resultset.getString("ROUTENAME");
                Route.addItem(id);
                
            }
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addRoute()
    {
        try
            {
                connect();
                
                
                String id=jComboBox1.getSelectedItem().toString();
                String route=Route.getSelectedItem().toString();
                if(id.isEmpty() || id.equals("") || route.isEmpty() || route.equals(""))
                    JOptionPane.showMessageDialog(null,"Please fill all the fields");
                else
                {
                    String sql="update TRANSPORTS set ROUTENAME=? where Transport_ID="+id;
                    preparedStatement=connection.prepareStatement(sql);
                    preparedStatement.setString(1,route);
                    preparedStatement.executeUpdate();
                    
                    sql="update TICKET set ROUTENAME=? where Transport_ID="+id;
                    preparedStatement=connection.prepareStatement(sql);
                    preparedStatement.setString(1,route);
                    preparedStatement.executeUpdate();

                    this.dispose();
                    JOptionPane.showMessageDialog(null,"Added");
                }

                /*while(resultset.next())
                {
                    JOptionPane.showMessageDialog(null,"Added");

                }*/


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        Route = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Type = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        Back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        Route.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transport_ID");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TransportType");

        Type.setText("N/A");

        jLabel4.setBackground(new java.awt.Color(153, 153, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Route");

        Add.setBackground(new java.awt.Color(255, 153, 153));
        Add.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Add.setText("Add");
        Add.setBorder(null);
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Back.setBackground(new java.awt.Color(255, 153, 153));
        Back.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home/back_30px.png"))); // NOI18N
        Back.setText("Back");
        Back.setBorder(null);
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Type)
                            .addComponent(Route, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(80, 80, 80))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(Back)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Type))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Route, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(34, 34, 34)
                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        get_Type();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void RouteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RouteActionPerformed

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        // TODO add your handling code here:
        addRoute();
    }//GEN-LAST:event_AddActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        // TODO add your handling code here:
         new Adminlogin().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddTransportRoute.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddTransportRoute.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddTransportRoute.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddTransportRoute.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddTransportRoute().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JButton Back;
    private javax.swing.JComboBox<String> Route;
    private javax.swing.JLabel Type;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
