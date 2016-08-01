package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

@Table(name = "lease_condition_template", schema = "administrative")
public class LeaseConditionTemplate extends AbstractVersionedEntity {
    @Id
    @Column
    private String id;
    @Column(name="template_name")
    private String templateName;
    @Column(name="rrr_type")
    private String rrrType;
    @Column(name="template_text")
    private String templateText;
    
    public LeaseConditionTemplate(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getRrrType() {
        return rrrType;
    }

    public void setRrrType(String rrrType) {
        this.rrrType = rrrType;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }
}
