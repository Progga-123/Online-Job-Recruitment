/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;





public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public abstract boolean register();
    public abstract boolean login();
    
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    
}