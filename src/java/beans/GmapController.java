/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import bean.InsMarker;
import faces.InstitutionFacade;
import entity.Institution;
import entity.Preference;
import entity.Transfer;
import faces.PreferenceFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

/**
 *
 * @author User
 */
@ManagedBean
@SessionScoped
public class GmapController implements Serializable {
    @EJB
    PreferenceFacade preferenceFacade;

    private MapModel emptyModel;
    private InsMarker marker;
    Preference preference;

    private String title;

    private double lat;

    private double lng;

    List<Transfer> selectedInTransfers;
    List<Transfer> selectedOutTransfers;

    @EJB
    InstitutionFacade institutionFacade;

    public void addLinesForTransfers() {
        System.out.println("addLinesForTransfers");
        removeLines();
        if (selectedInTransfers != null) {
            for (Transfer txin : selectedInTransfers) {
                addLineForTransfer(txin.getFromInstitution(), txin.getToInstitution(), "#0d7a25");
                System.out.println("txin = " + txin);
            }
        }
        if (selectedOutTransfers != null) {
            for (Transfer txOut : selectedOutTransfers) {
                System.out.println("txOut = " + txOut);
                addLineForTransfer(txOut.getFromInstitution(), txOut.getToInstitution(),"#FF0000");
            }
        }
    }

    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();
        getPreference();
        preference.setZoom(zoomLevel);
        preference.setLat(event.getCenter().getLat());
        preference.setLng(event.getCenter().getLng());
        preferenceFacade.edit(preference);

    }
    
    public void savePreferences(){
        preferenceFacade.edit(getPreference());
    }

    public void addLineForTransfer(Institution fromInstitution, Institution toInstitution, String colourHex) {
        System.out.println("add line for transfer");
        LatLng coord1 = new LatLng(fromInstitution.getLat(), fromInstitution.getLng());
        LatLng coord2 = new LatLng(toInstitution.getLat(), toInstitution.getLng());
        Polyline pl = new Polyline();
        pl.getPaths().add(coord1);
        pl.getPaths().add(coord2);
        pl.setStrokeWeight(4);
        pl.setStrokeColor(colourHex);
        pl.setStrokeOpacity(1);
        System.out.println("pl = " + pl);
        emptyModel.addOverlay(pl);
    }

    public void removeLines() {
        System.out.println("removing lines");
        emptyModel = new DefaultMapModel();
        listInstitutions();
    }

    public String listInstitutions() {
        System.out.println("listing institutions");
        List<Institution> inss = institutionFacade.findAll();
        emptyModel = new DefaultMapModel();

        init();
        for (Institution ins : inss) {
            InsMarker temMarker = new InsMarker(new LatLng(ins.getLat(), ins.getLng()), ins.getName());
            temMarker.setInstitution(ins);
            System.out.println("temMarker = " + temMarker);
            System.out.println("temMarker.getClass() = " + temMarker.getClass());
            emptyModel.addOverlay(temMarker);
        }
        System.out.println("emptyModel = " + emptyModel);
        return "/institution_list";
    }

    public GmapController() {
    }

    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void onMarkerSelect(OverlaySelectEvent event) {
        System.out.println("event = " + event);
        Marker m1 = (Marker) event.getOverlay();
        System.out.println("m1 = " + m1);
        if (m1 instanceof InsMarker) {
            marker = (InsMarker) m1;
            System.out.println("marker = " + marker);
            System.out.println("marker.getInstitution().getTransfersIn().size() = " + marker.getInstitution().getTransfersIn().size());
            System.out.println("marker.getInstitution().getTransfersOut().size() = " + marker.getInstitution().getTransfersOut().size());

        } else {
            System.out.println("not insMarker");
        }
        System.out.println("m1 = " + m1);
    }

    public void addMarker() {
        InsMarker temMarker = new InsMarker(new LatLng(lat, lng), title);
        Institution ins = new Institution();
        ins.setName(title);
        ins.setLat(lat);
        ins.setLng(lng);
        institutionFacade.create(ins);
        temMarker.setInstitution(ins);
        emptyModel.addOverlay(temMarker);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
    }

    public InsMarker getMarker() {
        return marker;
    }

    public void setMarker(InsMarker marker) {
        this.marker = marker;
    }

    public List<Transfer> getSelectedInTransfers() {
        return selectedInTransfers;
    }

    public void setSelectedInTransfers(List<Transfer> selectedInTransfers) {
        this.selectedInTransfers = selectedInTransfers;
    }

    public List<Transfer> getSelectedOutTransfers() {
        return selectedOutTransfers;
    }

    public void setSelectedOutTransfers(List<Transfer> selectedOutTransfers) {
        this.selectedOutTransfers = selectedOutTransfers;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    private void addMessage(FacesMessage facesMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Preference getPreference() {
        System.out.println("preference = " + preference);
        if(preference==null){
          List<Preference> ps =  preferenceFacade.findAll();
            System.out.println("ps = " + ps);
          if(ps==null || ps.isEmpty()){
              Preference p=new Preference();
              p.setZoom(10);
              p.setLat(6.06);
              p.setLng(80.5);
              preferenceFacade.create(p);
              preference=p;
          }else{
              preference = ps.get(0);
          }
        }
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    
}
