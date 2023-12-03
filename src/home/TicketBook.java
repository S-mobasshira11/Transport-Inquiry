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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class TicketBook extends javax.swing.JFrame {

    /**
     * Creates new form TicketBook
     */
    
    Connection connection=null;
    ResultSet resultset=null;
    PreparedStatement preparedstatement=null;
    private String route;
    private String phone;
    int seat;
    
    
    public TicketBook() {
        Color color = new Color(186, 79, 84);
        getContentPane().setBackground(color);
        initComponents();
        fillcombobox();
    }
    
    public void adduser()
    {
        if(FirstName.getText().equals("")|| LastName.getText().equals("") || Phone.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please fill all the fields");
        }
        else{
        String sql="Select * from CLIENT where Firstname=? and Lastname=? and Phone=?";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager
            .getConnection(
            "jdbc:sqlserver://localhost:1433;databaseName=Transport_Enquiry;selectMethod=cursor", "sa", "123456");
             preparedstatement=connection.prepareStatement(sql);
             preparedstatement.setString(1,FirstName.getText());
             preparedstatement.setString(2,LastName.getText());
             preparedstatement.setString(3,Phone.getText());
             resultset=preparedstatement.executeQuery();
             
             phone=Phone.getText();
             
             if(resultset.next())
             {
                 JOptionPane.showMessageDialog(null,"Success!");
                 new TicketBook().setVisible(true);
                 this.dispose();
             }
             else
             {
         
                String insert="Insert into CLIENT (Firstname,Lastname,Gender,Phone) values (?,?,?,?)";
                preparedstatement=connection.prepareStatement(insert);
                preparedstatement.setString(1,FirstName.getText());
                preparedstatement.setString(2,LastName.getText());
                preparedstatement.setString(3,Gender.getSelectedItem().toString());
                preparedstatement.setString(4,Phone.getText());
                
                phone=Phone.getText();
                
                int n=preparedstatement.executeUpdate();
                
                if(n>0)
                {
                    JOptionPane.showMessageDialog(null,"Success!");
                    new TicketBook().setVisible(true);
                    this.dispose();
                }
                    
             }
            
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
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
            String sql="SELECT ROUTENAME FROM ROUTEDETAILS";
            preparedstatement=connection.prepareStatement(sql);
            resultset=preparedstatement.executeQuery();
            
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
    
    public void showInfo()
    {
        DefaultTableModel dm = (DefaultTableModel) TicketTable.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount-1 ; i >= 0; i--) {
            if(i>=0)
            dm.removeRow(i);
        }
        
        
        SimpleDateFormat date_format=new SimpleDateFormat("yyyy-MM-dd");
        String date = date_format.format(jDateChooser1.getDate());
        if(date.toString().isEmpty() || date.toString().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Pick a date.");
        }
        else
        {
        try {
            connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet2 = statement
            .executeQuery("SELECT * FROM TICKET WHERE ROUTENAME='"+route+"'");
            boolean f2=resultSet2.next();
            
             ResultSet resultSet = statement
            .executeQuery("SELECT * FROM TICKET WHERE TravelDate='"+date+"'");
             
            boolean f1=resultSet.next();
            
            if(f1==false || f2==false)
            {
                JOptionPane.showMessageDialog(null,"Transport not available for this date in this route.");
            }
           
            else{
                resultSet = statement
            .executeQuery("SELECT * FROM TICKET WHERE TravelDate='"+date+"'");
            while (resultSet.next()) {
            

                    String TicketNo = String.valueOf(resultSet.getInt("TicketNo"));
                    String Transport_ID = resultSet.getString("Transport_ID");
                    String TicketPrice = resultSet.getString("TicketPrice");
                    String BookingDate = resultSet.getString("BookingDate");
                    String TravelDate = resultSet.getString("TravelDate");
                    String ROUTENAME = resultSet.getString("ROUTENAME");
                    String SeatNo = String.valueOf(resultSet.getInt("SeatNo"));
                   
                    
                    String tabledata[]={TicketNo,Transport_ID,TicketPrice,BookingDate,TravelDate,ROUTENAME,SeatNo};
                    DefaultTableModel tablemodel = (DefaultTableModel)TicketTable.getModel();
                    tablemodel.addRow(tabledata);
                    
                    //System.out.println(CustomerID+"     "+FirstName+"     "+LastName+"     "+CustomerAddress);
  
            }
            }
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }
    
    public void getUID()
    {
        connect();
        try
        {
            String id=null;
            String ticketno=null;
            connect();
            String sql="SELECT User_ID FROM CLIENT where Phone="+phone;
            preparedstatement=connection.prepareStatement(sql);
            resultset=preparedstatement.executeQuery();
            
            while(resultset.next()){
                id=resultset.getString("User_ID");
            }
            
            
            sql="SELECT TicketNo FROM TICKET where SeatNo="+String.valueOf(seat);
            preparedstatement=connection.prepareStatement(sql);
            resultset=preparedstatement.executeQuery();
            
             while(resultset.next()){
                ticketno=resultset.getString("TicketNo");
             }
            
            if(id!=null || ticketno!=null)
            {
                sql="INSERT INTO TICKETBOOKED (User_ID,TicketNo) values(?,?);";
                preparedstatement=connection.prepareStatement(sql);
                
                preparedstatement.setString(1,id);
                preparedstatement.setString(2,ticketno);
                
                preparedstatement.executeUpdate();
                
            }
            
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void booking()
    {
        connect();
        
        SimpleDateFormat date_format=new SimpleDateFormat("yyyy-MM-dd");
        String date1 = date_format.format(jDateChooser1.getDate());
        String date2 = date_format.format(new Date());
        
        if(date1.compareTo(date2) < 0) 
            JOptionPane.showMessageDialog(null,"Please Select Correct booking date");
        else
        {
        
            String sql;
            String Bookingdate=date_format.format(new Date());
            String bookingdate;
            int seatNo;
            boolean f=false;
            for(int i=1;i<=32;i++)
            {
                try
                {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement
                    .executeQuery("SELECT BookingDate,SeatNo,TicketNo FROM TICKET WHERE SeatNo="+String.valueOf(i));
                    
                    while(resultSet.next())
                    {
                        bookingdate=resultSet.getString("BookingDate");
                        if(bookingdate==null)
                        {
                            System.out.print(bookingdate+" "+i);
                            seatNo=i;
                             seat=i;
                             sql="update TICKET set BookingDate=?,TicketPrice=650 where SeatNo="+String.valueOf(seatNo);
                             preparedstatement=connection.prepareStatement(sql);
                             preparedstatement.setString(1,Bookingdate);
                             preparedstatement.executeUpdate();
                            f=true;
                        }
                        
                    }
                    if(f)
                       break;
                        
                        
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
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

        jLabel1 = new javax.swing.JLabel();
        TransportType = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        Route = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TicketPrice = new javax.swing.JLabel();
        ShowPrice = new javax.swing.JLabel();
        Okay = new javax.swing.JButton();
        FirstName = new javax.swing.JTextField();
        LastName = new javax.swing.JTextField();
        Phone = new javax.swing.JTextField();
        Gender = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        TicketTable = new javax.swing.JTable();
        show = new javax.swing.JButton();
        Back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Travel Date");

        TransportType.setForeground(new java.awt.Color(51, 51, 255));
        TransportType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bus" }));
        TransportType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransportTypeActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Transport Type");

        Route.setBackground(new java.awt.Color(204, 255, 204));
        Route.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RouteActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Route");

        TicketPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TicketPrice.setForeground(new java.awt.Color(255, 255, 255));
        TicketPrice.setText("Ticket Price");

        ShowPrice.setBackground(new java.awt.Color(204, 255, 204));
        ShowPrice.setText("0");

        Okay.setBackground(new java.awt.Color(255, 153, 153));
        Okay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Okay.setForeground(new java.awt.Color(51, 51, 255));
        Okay.setText("Book");
        Okay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkayActionPerformed(evt);
            }
        });

        FirstName.setBackground(new java.awt.Color(204, 255, 204));
        FirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstNameActionPerformed(evt);
            }
        });

        LastName.setBackground(new java.awt.Color(204, 255, 204));
        LastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameActionPerformed(evt);
            }
        });

        Phone.setBackground(new java.awt.Color(204, 255, 204));
        Phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PhoneActionPerformed(evt);
            }
        });

        Gender.setForeground(new java.awt.Color(51, 51, 255));
        Gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "Others" }));
        Gender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenderActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("First Name");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Last Name");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Contact Information");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Gender");

        jDateChooser1.setBackground(new java.awt.Color(204, 255, 204));

        TicketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TicketNo", "Transport_ID", "TicketPrice", "BookingDate", "TravelDate", "ROUTENAME", "SeatNo"
            }
        ));
        jScrollPane1.setViewportView(TicketTable);

        show.setBackground(new java.awt.Color(255, 153, 153));
        show.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        show.setForeground(new java.awt.Color(0, 102, 102));
        show.setText("Show");
        show.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 204), 1, true));
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });

        Back.setBackground(new java.awt.Color(255, 153, 153));
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
            .addGroup(layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(TicketPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TransportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LastName, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Route, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ShowPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(show, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(236, 236, 236))
            .addGroup(layout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(Okay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(show, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(LastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(TransportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(Route, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TicketPrice)
                            .addComponent(ShowPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Okay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TransportTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TransportTypeActionPerformed
        // TODO add your handling code here:
        String type = TransportType.getSelectedItem().toString();
        System.out.println(type);
    }//GEN-LAST:event_TransportTypeActionPerformed

    private void RouteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RouteActionPerformed
        // TODO add your handling code here:
        route="Dhaka-Khulna";
        route=Route.getSelectedItem().toString();
        ShowPrice.setText("650Tk");
    }//GEN-LAST:event_RouteActionPerformed

    private void OkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkayActionPerformed
        // TODO add your handling code here:
        
        adduser();
        booking();
        getUID();
        this.dispose();
        
    }//GEN-LAST:event_OkayActionPerformed

    private void FirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FirstNameActionPerformed

    private void LastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LastNameActionPerformed

    private void PhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PhoneActionPerformed

    private void GenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GenderActionPerformed

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed
        // TODO add your handling code here:
        showInfo();
    }//GEN-LAST:event_showActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        // TODO add your handling code here:
         new Accountantlogin().setVisible(true);
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
            java.util.logging.Logger.getLogger(TicketBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicketBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicketBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TicketBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JTextField FirstName;
    private javax.swing.JComboBox<String> Gender;
    private javax.swing.JTextField LastName;
    private javax.swing.JButton Okay;
    private javax.swing.JTextField Phone;
    private javax.swing.JComboBox<String> Route;
    private javax.swing.JLabel ShowPrice;
    private javax.swing.JLabel TicketPrice;
    private javax.swing.JTable TicketTable;
    private javax.swing.JComboBox<String> TransportType;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton show;
    // End of variables declaration//GEN-END:variables
}
