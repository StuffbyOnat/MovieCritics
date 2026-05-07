/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author onatu
 */
public enum UserType {
    Father(1),
    Mother(2),
    Son(3),
    Daughter(4);
    
    private final int value;
    
    UserType(int value){
    this.value=value;
    }
    
    public static UserType fromInt(int id) {
        for (UserType type : values()) {
            if (type.value == id) {
                return type;
            }
        }
        return Son; 
    }
}
