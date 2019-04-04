
package main.java.Home;

public class Set_type_brand {
    String type_ID = "";
    String brand_ID = "";
    
    public String set_type_ID(String typename){
        switch(typename){
            case "Laptop":
                type_ID = "001";
                break;
            case "Phone":
                type_ID = "002";
                break;
            case "Keybourd":
                type_ID = "003";
                break;
            case "Mouse":
                type_ID = "004";
                break; 
            case "Pen_Drive":
                type_ID = "005";
                break;
           
                
        }
        return type_ID;
    }
    
    
    public String set_Brand_ID(String brandname){
        switch(brandname){
            case "HP":
                brand_ID = "001";
                break;
            case "Dell":
                brand_ID = "002";
                break;
            case "Abanse":
                brand_ID = "003";
                break;
            case "Acer":
                brand_ID = "004";
                break;                   
            case "AGC":
                brand_ID = "005";
                break;
            case "Microsoft":
                brand_ID = "006";
                break;
            case "Lenovo":
                brand_ID = "007";
                break;
                
                
                
            case "Lava":
                brand_ID = "008";
                break; 
            case "Apple":
                brand_ID = "009";
                break;
            case "LES3":
                brand_ID = "010";
                break;
            case "Nokia":
                brand_ID = "011";
                break;                  
            case "Bird":
                brand_ID = "012";
                break;   
                
            case "Yamaha":
                brand_ID = "013";
                break; 
            case "Casio":
                brand_ID = "014";
                break;
            case "Roland":
                brand_ID = "015";
                break;
            case "Kawai":
                brand_ID = "016";
                break;                  
            case "Korg":
                brand_ID = "017";
                break;      
                
            case "Kingston":
                brand_ID = "018";
                break; 
            case "SanDisk":
                brand_ID = "019";
                break;
            case "Samsung":
                brand_ID = "020";
                break;
            case "PNY":
                brand_ID = "021";
                break;                  
            case "Patrot":
                brand_ID = "022";
                break;    
        }
        return brand_ID;
    }
    
}
