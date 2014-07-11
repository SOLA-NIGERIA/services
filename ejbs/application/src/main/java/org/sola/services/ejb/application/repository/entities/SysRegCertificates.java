/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.application.repository.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.Localized;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 * Entity representing the application systematic_registration_certificates
 * view.
 *
 * @author soladev
 */
@Table(name = "systematic_registration_certificates", schema = "application")
public class SysRegCertificates extends AbstractReadOnlyEntity {

    public static final String QUERY_PARAMETER_NR = "nr";
    // Where clause
    public static final String QUERY_WHERE_BYNR = "nr = #{" + QUERY_PARAMETER_NR  + "} "
            + "AND #{search_string}= name";

    /**
     * WHERE clause to return current CO's based on search string compared to
     * last part
     */
    public static final String QUERY_WHERE_SEARCHBYPARTS = "#{search_string}= name";
    
    @Column(name = "id")
    private String id;
    @Column(name = "nr")
    private String nr;
    @Column(name = "name_firstpart")
    private String nameFirstpart;
    @Column(name = "name_lastpart")
    private String nameLastpart;
    @Column(name = "ba_unit_id")
    private String baUnitId;
    @Column(name = "name")
    private String name;
    @Column(name = "appId")
    private String appId;
    @Column(name = "commencingDate")
    private Date commencingDate;
    @Column(name = "landUse")
    private String landUse;
    @Column(name = "propLocation")
    private String propLocation;
    @Column(name = "size")
    private BigDecimal size;
    @Column(insertable=false, updatable=false, name = "ground_rent")
    @AccessFunctions(onSelect = "application.ground_rent(ba_unit_id)")
    private BigDecimal groundRent;
    @Column(name = "owners")
    private String owners;
    @Column(name = "title")
    private String title;
    @Column(name = "state")
    private String state;
    @Column(name = "ward")
    private String ward;
    @Column(name = "imageryDate")
    private String imageryDate;
    @Column(name = "CofO")
    private Integer CofO;
    
    @Column(name = "imageryResolution")
    private String imageryResolution;
    @Column(name = "imagerySource")
    private String imagerySource;
    @Column(name = "sheetNr")
    private String sheetNr;
    @Column(name = "surveyor")
    private String surveyor;
    @Column(name = "rank")
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(String surveyor) {
        this.surveyor = surveyor;
    }
          
    public String getImageryResolution() {
        return imageryResolution;
    }

    public void setImageryResolution(String imageryResolution) {
        this.imageryResolution = imageryResolution;
    }

    public String getImagerySource() {
        return imagerySource;
    }

    public void setImagerySource(String imagerySource) {
        this.imagerySource = imagerySource;
    }

    public String getSheetNr() {
        return sheetNr;
    }

    public void setSheetNr(String sheetNr) {
        this.sheetNr = sheetNr;
    }
    
    public Integer getCofO() {
        return CofO;
    }

    public void setCofO(Integer CofO) {
        this.CofO = CofO;
    }
    
    
    
    public String getImageryDate() {
        return imageryDate;
    }

    public void setImageryDate(String imageryDate) {
        this.imageryDate = imageryDate;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
    
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }
    
    
    public BigDecimal getGroundRent() {
        return groundRent;
    }

    public void setGroundRent(BigDecimal groundRent) {
        this.groundRent = groundRent;
    }
    
    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getPropLocation() {
        return propLocation;
    }

    public void setPropLocation(String propLocation) {
        this.propLocation = propLocation;
    }

    public String getLandUse() {
        return landUse;
    }

    public void setLandUse(String landUse) {
        this.landUse = landUse;
    }
       
    
    public Date getCommencingDate() {
        return commencingDate;
    }

    public void setCommencingDate(Date commencingDate) {
        this.commencingDate = commencingDate;
    }
    
    

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    
    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public String getNameFirstpart() {
        return nameFirstpart;
    }

    public void setNameFirstpart(String nameFirstpart) {
        this.nameFirstpart = nameFirstpart;
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        this.nameLastpart = nameLastpart;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
     
}
