package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

@Table(name = "lease_condition_template", schema = "administrative")
public class LeaseConditionTemplateSearchResults extends AbstractReadOnlyEntity {

    public static final String PARAM_RRR_TYPE = "rrrType";
    public static final String PARAM_LANG = "lang";
    public static final String SELECT_QUERY = 
            "SELECT t.id, t.template_name, t.rrr_type, get_translation(rt.display_value, #{" + PARAM_LANG + "}) as rrr_type_name\n "
            + "FROM administrative.lease_condition_template t LEFT JOIN administrative.rrr_type rt ON t.rrr_type = rt.code\n "
            + "WHERE t.rrr_type = #{" + PARAM_RRR_TYPE + "} OR #{" + PARAM_RRR_TYPE + "} = '' OR t.rrr_type is null \n "
            + "ORDER BY t.template_name";

    @Id
    @Column
    private String id;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "rrr_type")
    private String rrrType;
    @Column(name="rrr_type_name")
    private String rrrTypeName;

    public LeaseConditionTemplateSearchResults() {

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

    public String getRrrTypeName() {
        return rrrTypeName;
    }

    public void setRrrTypeName(String rrrTypeName) {
        this.rrrTypeName = rrrTypeName;
    }
}
