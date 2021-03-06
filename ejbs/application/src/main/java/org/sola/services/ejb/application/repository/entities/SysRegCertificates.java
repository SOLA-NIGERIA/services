/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 *//*
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
    public static final String QUERY_WHERE_BYNR = "nr = #{" + QUERY_PARAMETER_NR  + "} ";
//            + "AND #{search_string}= name";

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
    @Column(name = "term")
    private Integer term;
    @Column(name = "rent")
    @AccessFunctions(onSelect = "administrative.get_yearly_rent(ba_unit_id)")
    private BigDecimal rent;
    @AccessFunctions(onSelect = "administrative.get_stamp_duty(ba_unit_id)")
    @Column(name = "stamp_duty")
    private BigDecimal stampDuty;
    @AccessFunctions(onSelect = "administrative.get_improvement_premium(ba_unit_id)")
    @Column(name = "premium")
    private BigDecimal premium;
    @Column(name = "estate")
    private String estate;
    @Column(name = "tarrif_type")
    private String tarrifType;
    
//    @Column(name = "premium_state_land")
//    private Integer premiumStateLand;
    //    @AccessFunctions(onSelect = "cadastre.premium(ba_unit_id)")
//    @Column(name = "premium_non_state")
//    private Integer premiumNonState;
//    public Integer getPremiumStateLand() {
//        return premiumStateLand;
//    }
//
//    public void setPremiumStateLand(Integer premiumStateLand) {
//        this.premiumStateLand = premiumStateLand;
//    }
//
//    public Integer getPremiumNonState() {
//        return premiumNonState;
//    }
//
//    public void setPremiumNonState(Integer premiumNonState) {
//        this.premiumNonState = premiumNonState;
//    }
//

    public BigDecimal getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        this.stampDuty = stampDuty;
    }
    
    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    
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

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getTarrifType() {
        return tarrifType;
    }

    public void setTarrifType(String tarrifType) {
        this.tarrifType = tarrifType;
    }     
     
}
