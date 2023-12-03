/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

/**
 *
 * @author HP
 */
public class Userdata extends TicketBook{

     String Phone;
    
    public Userdata(String Phone)
    {
        this.Phone=Phone;
    }
    
    Userdata()
    {
        
    }
    
    public void setPhone(String Phone)
    {
        this.Phone=Phone;
    }
    
    public String getPhone()
    {
        return this.Phone;
    }
    
    
    
    
    
}
