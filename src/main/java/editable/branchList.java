package main.java.editable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class branchList {
   ObservableList<String> listbranch = FXCollections.observableArrayList("Horana","Colombo","Kelaniya","Gampaha","Anuradhapura","Galle","Hambantota","Jaffna","Kandy","Kurunegala","Matara","Monaragala","Nuwara Eliya");
    
    public ObservableList<String> getbranch(){
        return listbranch;
    }
    
}
