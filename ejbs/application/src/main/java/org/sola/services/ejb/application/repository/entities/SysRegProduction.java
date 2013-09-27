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
 *
 * @author RizzoM
 */
@Table(name = "systematic_registration_production", schema = "application")
public class SysRegProduction  extends AbstractReadOnlyEntity {
    
    public static final String PARAMETER_FROM = "fromDate";
    public static final String PARAMETER_TO = "toDate";
    public static final String QUERY_GETQUERY = "select * from application.getsysregproduction(#{"
            + PARAMETER_FROM + "}, #{" + PARAMETER_TO + "}) "
            + " as SysRegProductionReport(ownerName varchar,"
            + " typeCode   varchar,"
            + "	monday     decimal,"
            + "	tuesday    decimal,"
            + "	wednesday  decimal,"
            + "	thursday   decimal,"
            + "	friday     decimal,"
            + "	saturday   decimal,"
            + " sunday     decimal )";
    
 @Column(name = "ownerName")
    private String ownerName;
 @Column(name = "typeCode")
    private String typeCode;
 @Column(name = "Monday")
    private BigDecimal monday;
 @Column(name = "Tuesday")
    private BigDecimal tuesday;
 @Column(name = "Wednesday")
    private BigDecimal wednesday;
 @Column(name = "Thursday")
    private BigDecimal thursday;
 @Column(name = "Friday")
    private BigDecimal friday;
 @Column(name = "Saturday")
    private BigDecimal saturday;
 @Column(name = "Sunday")
    private BigDecimal sunday;

    public BigDecimal getFriday() {
        return friday;
    }

    public void setFriday(BigDecimal friday) {
        this.friday = friday;
    }

    public BigDecimal getMonday() {
        return monday;
    }

    public void setMonday(BigDecimal monday) {
        this.monday = monday;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getSaturday() {
        return saturday;
    }

    public void setSaturday(BigDecimal saturday) {
        this.saturday = saturday;
    }

    public BigDecimal getSunday() {
        return sunday;
    }

    public void setSunday(BigDecimal sunday) {
        this.sunday = sunday;
    }

    public BigDecimal getThursday() {
        return thursday;
    }

    public void setThursday(BigDecimal thursday) {
        this.thursday = thursday;
    }

    public BigDecimal getTuesday() {
        return tuesday;
    }

    public void setTuesday(BigDecimal tuesday) {
        this.tuesday = tuesday;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public BigDecimal getWednesday() {
        return wednesday;
    }

    public void setWednesday(BigDecimal wednesday) {
        this.wednesday = wednesday;
    }
 
 
 
          
    
}    