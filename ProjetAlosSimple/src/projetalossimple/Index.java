/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetalossimple;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author DG
 */
public class Index implements Serializable{
    List<List<String>> index;
    public Index(){
       
    }
    
    public List<List<String>> getIndex(){
        return this.index;
    }
    public void setIndex(List<List<String>> index){
         this.index=index;
    }
    
}
