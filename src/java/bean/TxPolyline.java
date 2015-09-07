/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.Transfer;
import java.util.List;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;

/**
 *
 * @author Niluka
 */
public class TxPolyline extends Polyline {
    Transfer transfer;

    public TxPolyline() {
    }

    public TxPolyline(List<LatLng> paths) {
        super(paths);
    }

    public TxPolyline(List<LatLng> paths, Object data) {
        super(paths, data);
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }
    
    
    
    
}
