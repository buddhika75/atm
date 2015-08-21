/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 *
 * @author User
 */
public enum InstitutionType {
    MohSl,
    DohSl,
    Pmoh,
    Pdoh,
    Rdhs,
    Nh,
    Th,
    Dgh,
    Bh,
    Dh,
    Pcu,
    Cd,
    Moh;
    
    public String getLabel(){
        return this.toString();
    }
}
