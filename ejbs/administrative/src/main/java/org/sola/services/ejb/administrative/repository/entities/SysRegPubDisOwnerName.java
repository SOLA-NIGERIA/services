package org.sola.services.ejb.administrative.repository.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.Localized;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 * Entity representing the administrative sys_reg_owner_name view.
 *
 *
 * @author soladev
 */
@Table(name = "sys_reg_owner_name", schema = "administrative")
public class SysRegPubDisOwnerName extends AbstractReadOnlyEntity {

    public static final String QUERY_PARAMETER_ID = "id";
    // Where clause
    public static final String QUERY_WHERE_BYID = "id = #{" + QUERY_PARAMETER_ID + "}";
    public static final String QUERY_WHERE_SEARCHBYPARTS = "name = #{search_string}";
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private String value;
    @Column(name = "name_firstpart")
    private String nameFirstpart;
    @Column(name = "name_lastpart")
    private String nameLastpart;
    @Column(name = "size")
    private BigDecimal size;
    @Localized
    @Column(name = "land_use_code")
    private String landUsecode;
    @Column(name = "ba_unit_id")
    private String baUnitId;
    @Column(insertable = false, updatable = false, name = "public_notification_duration")
    @AccessFunctions(onSelect = "system.get_setting('public-notification-duration')")
    private String publicNotificationDuration;
    @Column(insertable = false, updatable = false, name = "objections")
    @AccessFunctions(onSelect = "administrative.get_objections(name_lastpart)")
    private String objections;
    @Column(name = "location")
    private String location;
    @Column(name = "rrr")
    
    
    private String rrr;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }
   
    public String getPublicNotificationDuration() {
        return publicNotificationDuration;
    }

    public void setPublicNotificationDuration(String publicNotificationDuration) {
        this.publicNotificationDuration = publicNotificationDuration;
    }

    public SysRegPubDisOwnerName() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLandUsecode() {
        return landUsecode;
    }

    public void setLandUsecode(String landUsecode) {
        this.landUsecode = landUsecode;
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

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public String getObjections() {
        return objections;
    }

    public void setObjections(String objections) {
        this.objections = objections;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }
     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
