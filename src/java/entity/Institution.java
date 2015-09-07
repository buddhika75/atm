/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enums.InstitutionType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author User
 */
@Entity
public class Institution implements Serializable {
    @OneToMany(mappedBy = "toInstitution")
    private List<Transfer> transfersIn;
    @OneToMany(mappedBy = "fromInstitution")
    private List<Transfer> transfersOut;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    InstitutionType type;
    String name;
    double lat;
    double lng;
    @Lob
    String comments;
    @ManyToOne
    Institution parentInstitution;

    public InstitutionType getType() {
        return type;
    }

    public void setType(InstitutionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Institution getParentInstitution() {
        return parentInstitution;
    }

    public void setParentInstitution(Institution parentInstitution) {
        this.parentInstitution = parentInstitution;
    }

    public List<Transfer> getTransfersIn() {
        return transfersIn;
    }

    public List<Transfer> getTransfersInCompleted() {
        List<Transfer> lst = new ArrayList<>();
        if(transfersIn!=null && !transfersIn.isEmpty())
        for(Transfer t:transfersIn){
            if(t.isCompleted()){
                lst.add(t);
            }
        }
        return lst;
    }
    
    public List<Transfer> getTransfersInPending() {
        List<Transfer> lst = new ArrayList<>();
        if(transfersIn!=null && !transfersIn.isEmpty())
        for(Transfer t:transfersIn){
            if(!t.isCompleted()){
                lst.add(t);
            }
        }
        return lst;
    }
    
    public List<Transfer> getTransfersOutCompleted() {
        List<Transfer> lst = new ArrayList<>();
        if(transfersOut!=null && !transfersOut.isEmpty())
        for(Transfer t:transfersOut){
            if(t.isCompleted()){
                lst.add(t);
            }
        }
        return lst;
    }
    
    public List<Transfer> getTransfersOutPending() {
        List<Transfer> lst = new ArrayList<>();
        if(transfersOut!=null && !transfersOut.isEmpty())
        for(Transfer t:transfersOut){
            if(!t.isCompleted()){
                lst.add(t);
            }
        }
        return lst;
    }
    
    
    
    
    public void setTransfersIn(List<Transfer> transfersIn) {
        this.transfersIn = transfersIn;
    }

    public List<Transfer> getTransfersOut() {
        return transfersOut;
    }

    public void setTransfersOut(List<Transfer> transfersOut) {
        this.transfersOut = transfersOut;
    }
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
