/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import bean.InsMarker;
import bean.TxPolyline;
import faces.InstitutionFacade;
import entity.Institution;
import entity.Preference;
import entity.Transfer;
import faces.PreferenceFacade;
import faces.TransferFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.model.map.Overlay;
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
    @EJB
    TransferFacade transferFacade;

    private MapModel emptyModel;
    private InsMarker marker;
    private TxPolyline polyline;
    Preference preference;

    private String title;

    private double lat;

    private double lng;

    List<Transfer> upsPending;
    List<Transfer> downsPending;
    List<Transfer> upsCompleted;
    List<Transfer> downsCompleted;

    List<Transfer> selectedInTransfers;
    List<Transfer> selectedOutTransfers;
    List<Transfer> selectedInCompletedTransfers;
    List<Transfer> selectedOutCompletedTransfers;

    @EJB
    InstitutionFacade institutionFacade;

    public void addLinesForTransfers() {
        System.out.println("addLinesForTransfers");
        removeLines();

        if (selectedInTransfers != null) {
            System.out.println("selectedInTransfers.size() = " + selectedInTransfers.size());
            for (Transfer txin : selectedInTransfers) {
                addLineForTransfer(txin, "#0d7a25");
                System.out.println("txin = " + txin);
            }
        }
        if (selectedOutTransfers != null) {
            System.out.println("selectedOutTransfers.size() = " + selectedOutTransfers.size());
            for (Transfer txOut : selectedOutTransfers) {
                System.out.println("txOut = " + txOut);
                addLineForTransfer(txOut, "#FF0000");
            }
        }
        if (selectedInCompletedTransfers != null) {
            System.out.println("selectedInCompletedTransfers.size() = " + selectedInCompletedTransfers.size());
            for (Transfer txInCom : selectedInCompletedTransfers) {
                System.out.println("txInCom = " + txInCom);
                addLineForTransfer(txInCom, "#99FF66");
            }
        }
        if (selectedOutCompletedTransfers != null) {
            System.out.println("selectedOutCompletedTransfers.size() = " + selectedOutCompletedTransfers.size());
            for (Transfer txOutCom : selectedOutCompletedTransfers) {
                System.out.println("txOutCom = " + txOutCom);
                addLineForTransfer(txOutCom, "#FFCCFF");
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

    public void savePreferences() {
        preferenceFacade.edit(getPreference());
    }

    public void addLineForTransfer(Transfer tx, String colourHex) {
        System.out.println("add line for transfer");
        LatLng coord1 = new LatLng(tx.getFromInstitution().getLat(), tx.getFromInstitution().getLng());
        LatLng coord2 = new LatLng(tx.getToInstitution().getLat(), tx.getToInstitution().getLng());
        TxPolyline pl = new TxPolyline();
        pl.getPaths().add(coord1);
        pl.getPaths().add(coord2);
        pl.setStrokeWeight(4);
        pl.setStrokeColor(colourHex);
        pl.setStrokeOpacity(1);
        pl.setTransfer(tx);
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
        Overlay ol = event.getOverlay();
        Marker m;
        Polyline p;

        marker = null;
        polyline = null;
        selectedInTransfers = null;
        selectedOutTransfers = null;
        selectedInCompletedTransfers = null;
        selectedOutCompletedTransfers = null;
        upsCompleted=null;
        upsPending=null;
        downsCompleted=null;
        downsPending=null;

        if (ol instanceof Marker) {
            m = (Marker) event.getOverlay();
            System.out.println("m1 = " + m);
            if (m instanceof InsMarker) {
                marker = (InsMarker) m;
            } else {
                System.out.println("not insMarker");
            }
            System.out.println("m1 = " + m);
        } else if (ol instanceof Polyline) {
            p = (Polyline) event.getOverlay();
            System.out.println("p = " + p);
            if (p instanceof TxPolyline) {
                polyline = (TxPolyline) p;
                String j;
                Map mp = new HashMap();
                mp.put("fi", polyline.getTransfer().getFromInstitution());
                mp.put("ti", polyline.getTransfer().getToInstitution());
                j = "select t "
                        + " from Transfer t "
                        + " where t.fromInstitution=:fi and t.toInstitution=:ti "
                        + " and t.completed=true "
                        + " order by t.transferNo";
                upsCompleted = transferFacade.findBySQL(j, mp);
                
                j = "select t "
                        + " from Transfer t "
                        + " where t.fromInstitution=:fi and t.toInstitution=:ti "
                        + " and (t.completed=false  or t.completed is null) "
                        + " order by t.transferNo";
                upsPending = transferFacade.findBySQL(j, mp);
                
                j = "select t "
                        + " from Transfer t "
                        + " where t.fromInstitution=:ti and t.toInstitution=:fi"
                        + " and t.completed=true  "
                        + " order by t.transferNo";
                downsCompleted = transferFacade.findBySQL(j, mp);
                
                j = "select t "
                        + " from Transfer t "
                        + " where t.fromInstitution=:ti and t.toInstitution=:fi"
                        + " and (t.completed=false  or t.completed is null) "
                        + " order by t.transferNo";
                downsPending = transferFacade.findBySQL(j, mp);
                
                
            } else {

                System.out.println("not TxPolyline");
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nothing Selected", null));
        }
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
        if (preference == null) {
            List<Preference> ps = preferenceFacade.findAll();
            System.out.println("ps = " + ps);
            if (ps == null || ps.isEmpty()) {
                Preference p = new Preference();
                p.setZoom(10);
                p.setLat(6.06);
                p.setLng(80.5);
                preferenceFacade.create(p);
                preference = p;
            } else {
                preference = ps.get(0);
            }
        }
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public List<Transfer> getSelectedInCompletedTransfers() {
        return selectedInCompletedTransfers;
    }

    public void setSelectedInCompletedTransfers(List<Transfer> selectedInCompletedTransfers) {
        this.selectedInCompletedTransfers = selectedInCompletedTransfers;
    }

    public List<Transfer> getSelectedOutCompletedTransfers() {
        return selectedOutCompletedTransfers;
    }

    public void setSelectedOutCompletedTransfers(List<Transfer> selectedOutCompletedTransfers) {
        this.selectedOutCompletedTransfers = selectedOutCompletedTransfers;
    }

    public TxPolyline getPolyline() {
        return polyline;
    }

    public void setPolyline(TxPolyline polyline) {
        this.polyline = polyline;
    }

    public PreferenceFacade getPreferenceFacade() {
        return preferenceFacade;
    }

    public void setPreferenceFacade(PreferenceFacade preferenceFacade) {
        this.preferenceFacade = preferenceFacade;
    }

    public List<Transfer> getUpsPending() {
        return upsPending;
    }

    public void setUpsPending(List<Transfer> upsPending) {
        this.upsPending = upsPending;
    }

    public List<Transfer> getDownsPending() {
        return downsPending;
    }

    public void setDownsPending(List<Transfer> downsPending) {
        this.downsPending = downsPending;
    }

    public List<Transfer> getUpsCompleted() {
        return upsCompleted;
    }

    public void setUpsCompleted(List<Transfer> upsCompleted) {
        this.upsCompleted = upsCompleted;
    }

    public List<Transfer> getDownsCompleted() {
        return downsCompleted;
    }

    public void setDownsCompleted(List<Transfer> downsCompleted) {
        this.downsCompleted = downsCompleted;
    }

}
