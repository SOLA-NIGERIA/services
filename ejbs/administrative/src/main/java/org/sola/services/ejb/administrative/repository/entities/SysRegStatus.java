/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations (FAO).
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
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import java.math.BigDecimal;
import javax.persistence.Column;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author RizzoM
 */
public class SysRegStatus extends AbstractReadOnlyEntity {

    public static final String PARAMETER_FROM = "fromDate";
    public static final String PARAMETER_TO = "toDate";
    public static final String QUERY_PARAMETER_LASTPART = "nameLastpart";
    public static final String QUERY_GETQUERY = "select * from administrative.getsysregstatus(#{"
            + PARAMETER_FROM + "}, #{" + PARAMETER_TO + "}, #{" + QUERY_PARAMETER_LASTPART + "}) "
            + " as SysRegStatusReport("
            + " srwu                    varchar,"
            + "	estimatedparcel  	decimal,"
            + " recordedparcel  	decimal,"
            + "	recordedclaims  	decimal,"
            + "	scanneddemarcation  	decimal,"
            + "	scannedclaims  		decimal,"
            + "	digitizedparcels  	decimal,"
            + "	claimsentered     	decimal,"
            + "	parcelsreadyPD  	decimal,"
            + "	parcelsPD  		decimal,"
            + "	parcelscompletedPD  	decimal,"
            + "	unsolveddisputes  	decimal,"
            + " generatedcertificates	decimal,"
            + " distributedcertificates decimal )";
       
		
    
     
    @Column(name = "srwu")
    private String srwu;
    @Column(name = "estimatedparcel")
    private BigDecimal estimatedparcel;
    @Column(name = "recordedparcel")
    private BigDecimal recordedparcel;
    @Column(name = "recordedclaims")
    private BigDecimal recordedclaims;
    @Column(name = "scanneddemarcation")
    private BigDecimal scanneddemarcation;
    @Column(name = "scannedclaims")
    private BigDecimal scannedclaims;
    @Column(name = "digitizedparcels")
    private BigDecimal digitizedparcels;
    @Column(name = "claimsentered")
    private BigDecimal claimsentered;
    @Column(name = "parcelsreadyPD")
    private BigDecimal parcelsreadyPD;
    @Column(name = "parcelsPD")
    private BigDecimal parcelsPD;
    @Column(name = "parcelscompletedPD")
    private BigDecimal 	parcelscompletedPD;
    @Column(name = "unsolveddisputes")
    private BigDecimal unsolveddisputes;
    @Column(name = "generatedcertificates")
    private BigDecimal generatedcertificates;
    @Column(name = "distributedcertificates")
    private BigDecimal distributedcertificates;
    
    public SysRegStatus() {
        super();
    }

    public BigDecimal getClaimsentered() {
        return claimsentered;
    }

    public void setClaimsentered(BigDecimal claimsentered) {
        this.claimsentered = claimsentered;
    }

    public BigDecimal getDigitizedparcels() {
        return digitizedparcels;
    }

    public void setDigitizedparcels(BigDecimal digitizedparcels) {
        this.digitizedparcels = digitizedparcels;
    }

    public BigDecimal getDistributedcertificates() {
        return distributedcertificates;
    }

    public void setDistributedcertificates(BigDecimal distributedcertificates) {
        this.distributedcertificates = distributedcertificates;
    }

    public BigDecimal getEstimatedparcel() {
        return estimatedparcel;
    }

    public void setEstimatedparcel(BigDecimal estimatedparcel) {
        this.estimatedparcel = estimatedparcel;
    }

    public BigDecimal getGeneratedcertificates() {
        return generatedcertificates;
    }

    public void setGeneratedcertificates(BigDecimal generatedcertificates) {
        this.generatedcertificates = generatedcertificates;
    }

    public BigDecimal getParcelsPD() {
        return parcelsPD;
    }

    public void setParcelsPD(BigDecimal parcelsPD) {
        this.parcelsPD = parcelsPD;
    }

    public BigDecimal getParcelscompletedPD() {
        return parcelscompletedPD;
    }

    public void setParcelscompletedPD(BigDecimal parcelscompletedPD) {
        this.parcelscompletedPD = parcelscompletedPD;
    }

    public BigDecimal getParcelsreadyPD() {
        return parcelsreadyPD;
    }

    public void setParcelsreadyPD(BigDecimal parcelsreadyPD) {
        this.parcelsreadyPD = parcelsreadyPD;
    }

    public BigDecimal getRecordedclaims() {
        return recordedclaims;
    }

    public void setRecordedclaims(BigDecimal recordedclaims) {
        this.recordedclaims = recordedclaims;
    }

    public BigDecimal getRecordedparcel() {
        return recordedparcel;
    }

    public void setRecordedparcel(BigDecimal recordedparcel) {
        this.recordedparcel = recordedparcel;
    }

    public BigDecimal getScannedclaims() {
        return scannedclaims;
    }

    public void setScannedclaims(BigDecimal scannedclaims) {
        this.scannedclaims = scannedclaims;
    }

    public BigDecimal getScanneddemarcation() {
        return scanneddemarcation;
    }

    public void setScanneddemarcation(BigDecimal scanneddemarcation) {
        this.scanneddemarcation = scanneddemarcation;
    }

    public String getSrwu() {
        return srwu;
    }

    public void setSrwu(String srwu) {
        this.srwu = srwu;
    }

    public BigDecimal getUnsolveddisputes() {
        return unsolveddisputes;
    }

    public void setUnsolveddisputes(BigDecimal unsolveddisputes) {
        this.unsolveddisputes = unsolveddisputes;
    }
}
