/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.ejb.cadastre.repository.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;
import java.io.Serializable;
import java.util.HashMap;
import org.sola.services.common.repository.RepositoryUtility;
import org.sola.services.ejb.system.br.Result;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;

/**
 * Entity representing the cadastre.spatial_unit_group table.
 */
@Table(name = "spatial_unit_group", schema = "cadastre")

/**
 *
 * @author rizzom
 */
public class SpatialUnitGroup extends AbstractVersionedEntity {
 

    /**
     * WHERE clause to return current&pending CO's based on search string
     * compared to first part and last part
     */
    public static final String QUERY_WHERE_SEARCHBYALLPARTS = " compare_strings(#{search_string}, name";
    /**
     * WHERE clause to return current CO's based on search string compared to
     * first part and last part
     */
    public static final String QUERY_WHERE_SEARCHBYPARTS = 
            " compare_strings(#{search_string}, name)"
                  + " and hierarchy_level = 4 ";

    public static final String QUERY_ORDER_BY_SEARCHBYPARTS =
            "name";
    
     public static String WHERE_CONDITION = "hierarchy_level = #{hierarchy_level} "
            + " and ST_Intersects(st_transform(geom, #{srid}), ST_SetSRID(ST_GeomFromWKB(#{filtering_geometry}), #{srid}))";
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "label")
    private String label;
    @Column(name = "found_in_spatial_unit_group_id")
    private String foundInSpatialId;
    @Column(name = "geom")
    @AccessFunctions(onSelect = "st_asewkb(geom)",
    onChange = "get_geometry_with_srid(#{geom})")
    private byte[] geom;

    
    @Column(name = "reference_point")
    @AccessFunctions(onSelect = "st_asewkb(geom)")
    private byte[] referencePoint;
    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;
    @Column(name = "seq_nr")
    private Integer seqNr;

     /**
     * No-arg constructor
     */
    public SpatialUnitGroup() {
        super();
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) { //NOSONAR
       this.geom = geom.clone();
    }
    
    public String getFoundInSpatialId() {
        return foundInSpatialId;
    }

    public void setFoundInSpatialId(String foundInSpatialId) {
        this.foundInSpatialId = foundInSpatialId;
    }

    public Integer getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(byte[] referencePoint) {
        this.referencePoint = referencePoint;
    }

    public Integer getSeqNr() {
        return seqNr;
    }

    public void setSeqNr(Integer seqNr) {
        this.seqNr = seqNr;
    }
   
 @Override
    public void preSave() {
        setName(generateName());
        super.preSave();
    }

    private String generateName() {
        String result = "";
        SystemEJBLocal systemEJB = RepositoryUtility.tryGetEJB(SystemEJBLocal.class);
        if (systemEJB != null) {
            HashMap<String, Serializable> parameters = new HashMap<String, Serializable>();
            parameters.put("geom_v", getGeom());
            parameters.put("hierarchy_level_v", getHierarchyLevel());
            parameters.put("label_v", getLabel());
            Result newNumberResult = systemEJB.checkRuleGetResultSingle(
                    "generate-spatial-unit-group-name", parameters);
            if (newNumberResult != null && newNumberResult.getValue() != null) {
                result = newNumberResult.getValue().toString();
            }
        }
        return result;
    }
}

