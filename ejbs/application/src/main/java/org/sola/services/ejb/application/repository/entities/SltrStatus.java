/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.application.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author rizzom
 */
@Table(name = "sltr_status", schema = "application")
public class SltrStatus extends AbstractReadOnlyEntity {
    public static final String QUERY_PARAMETER_ID = "id";
    // Where clause
    public static final String QUERY_WHERE_BYID = "#{search_string}= appid";
    @Column(name = "appid")
    private String appid;
    @Column(name = "sltr_status")
    private String sltrStatus;

    public String getappId() {
        return appid;
    }

    public void setappId(String appid) {
        this.appid = appid;
    }

    public String getSltrStatus() {
        return sltrStatus;
    }

    public void setSltrStatus(String sltrStatus) {
        this.sltrStatus = sltrStatus;
    }
    
    
}
