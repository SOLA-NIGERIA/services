package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

@Table(name = "condition_for_rrr", schema = "administrative")
public class ConditionForRrr extends AbstractVersionedEntity {
    
    @Id
    @Column
    private String id;
    
    @Column(name="rrr_id")
    private String rrrId;
    
    @Column(name="lease_condition_code")
    private String RrrConditionCode;
    
    @Column(name="custom_condition_text")
    private String customConditionText;
  
    public ConditionForRrr(){
        super();
    }

    public String getCustomConditionText() {
        return customConditionText;
    }

    public void setCustomConditionText(String customConditionText) {
        this.customConditionText = customConditionText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRrrConditionCode() {
        return RrrConditionCode;
    }

    public void setRrrConditionCode(String RrrConditionCode) {
        this.RrrConditionCode = RrrConditionCode;
    }

    public String getRrrId() {
        return rrrId;
    }

    public void setRrrId(String rrrId) {
        this.rrrId = rrrId;
    }
}
