/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.Institution;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Niluka
 */
public class InsMarker extends Marker {
    Institution institution;
    
    public InsMarker(LatLng latlng) {
        super(latlng);
    }

    
    public InsMarker(LatLng latlng, String title) {
        super(latlng, title);
    }

    public InsMarker(LatLng latlng, String title, Object data) {
        super(latlng, title, data);
    }

    public InsMarker(LatLng latlng, String title, Object data, String icon) {
        super(latlng, title, data, icon);
    }

    public InsMarker(LatLng latlng, String title, Object data, String icon, String shadow) {
        super(latlng, title, data, icon, shadow);
    }

    
    
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    
    
    
}
