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

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;

/**
 * Entity representing the cadastre.cadastre_object table.
 */
@Table(name = "cadastre_object", schema = "cadastre")
@DefaultSorter(sortString="name_lastpart,name_firstpart")
public class CadastreObject extends AbstractVersionedEntity {

    /**
     * WHERE clause to return current&pending CO's based on search string
     * compared to first part and last part
     */
    public static final String QUERY_WHERE_SEARCHBYALLPARTS = "(status_code= 'current' or status_code= 'pending') and "
            + "compare_strings(#{search_string}, name_firstpart || ' ' || name_lastpart)";
    /**
     * WHERE clause to return current CO's based on search string compared to
     * first part and last part
     */
    public static final String QUERY_WHERE_SEARCHBYPARTS = "status_code= 'current' and "
            + "compare_strings(#{search_string}, name_firstpart || ' ' || name_lastpart)";
    /**
     * WHERE clause to return current CO's intersecting the specified point
     */
    public static final String QUERY_WHERE_SEARCHBYPOINT = "type_code= #{type_code} "
            + "and status_code= 'current' and "
            + "ST_Intersects(st_transform(geom_polygon, #{srid}), ST_SetSRID(ST_Point(#{x}, #{y}), #{srid}))";
    /**
     * WHERE clause to return CO's linked to the specified ba_unit.id
     */
    public static final String QUERY_WHERE_SEARCHBYBAUNIT = "id in "
            + " (select spatial_unit_id from administrative.ba_unit_contains_spatial_unit "
            + "where ba_unit_id = #{ba_unit_id})";
    /**
     * WHERE clause to return current CO's linked to the specified service.id
     */
    public static final String QUERY_WHERE_SEARCHBYSERVICE = "status_code= 'current' "
            + "and transaction_id in "
            + " (select id from transaction.transaction where from_service_id = #{service_id}) ";
    /**
     * WHERE clause to return CO's linked to the specified transaction.id
     */
    public static final String QUERY_WHERE_SEARCHBYTRANSACTION =
            "transaction_id = #{transaction_id} and status_code = 'pending'";
    /**
     * WHERE clause to return current CO's matching type type_code and within
     * distance of the specified geometry
     */
    public static final String QUERY_WHERE_SEARCHBYGEOM = "type_code=#{type_code} "
            + "and status_code= 'current' and "
            + "ST_DWithin(st_transform(geom_polygon, #{srid}), st_transform(#{geom}, #{srid}), "
            + "system.get_setting('map-tolerance')::double precision)";
    /**
     * ORDER BY clause used to order search results for the Search by parts queries. 
     * Uses regex to order cadastre objects by lot number. 
     */
    public static final String QUERY_ORDER_BY_SEARCHBYPARTS =
            "lpad(regexp_replace(name_firstpart, '\\D*', '', 'g'), 5, '0') "
            + "|| name_firstpart || name_lastpart";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "approval_datetime")
    private Date approvalDatetime;
    @Column(name = "historic_datetime")
    private Date historicDatetime;
    @Column(name = "source_reference")
    private String sourceReference;
    @Column(name = "name_firstpart")
    private String nameFirstpart;
    @Column(name = "name_lastpart")
    private String nameLastpart;
    @Column(name = "status_code", insertable = false, updatable = false)
    private String statusCode;
    @Column(name = "transaction_id", updatable = false)
    private String transactionId;
    @Column(name = "geom_polygon")
    @AccessFunctions(onSelect = "st_asewkb(geom_polygon)",
    onChange = "get_geometry_with_srid(#{geomPolygon})")
    private byte[] geomPolygon;
    @ChildEntityList(parentIdField = "spatialUnitId")
    private List<SpatialValueArea> spatialValueAreaList;
    @Column(name = "land_use_code")
    private String landUseCode;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class, loadMethod = "getAddresses", saveMethod="saveAddress")
    @ChildEntityList(parentIdField = "cadastreObjectId", childIdField = "addressId",
    manyToManyClass = AddressForCadastreObject.class)
    private List<Address> addressList;
    @Column(name = "block")
    private String block;
    @Column(name = "plot_num")
    private String plotNum;
    @Column(name = "lga_code")
    private String lgaCode;
    @Column(name = "intell_map_sheet")
    private String intellMapSheet;
    
    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        this.landUseCode = landUseCode;
    }

    /**
     * No-arg constructor
     */
    public CadastreObject() {
        super();
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getApprovalDatetime() {
        return approvalDatetime;
    }

    public void setApprovalDatetime(Date approvalDatetime) {
        this.approvalDatetime = approvalDatetime;
    }

    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    public void setGeomPolygon(byte[] geomPolygon) { //NOSONAR
        this.geomPolygon = geomPolygon; //NOSONAR
    }

    public Date getHistoricDatetime() {
        return historicDatetime;
    }

    public void setHistoricDatetime(Date historicDatetime) {
        this.historicDatetime = historicDatetime;
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

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public List<SpatialValueArea> getSpatialValueAreaList() {
        // Loaded eagerly by the CommonRepository
        return spatialValueAreaList;
    }

    public void setSpatialValueAreaList(List<SpatialValueArea> spatialValueAreaList) {
        this.spatialValueAreaList = spatialValueAreaList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPlotNum() {
        return plotNum;
    }

    public void setPlotNum(String plotNum) {
        this.plotNum = plotNum;
    }

    public String getLgaCode() {
        return lgaCode;
    }

    public void setLgaCode(String lgaCode) {
        this.lgaCode = lgaCode;
    }

    public String getIntellMapSheet() {
        return intellMapSheet;
    }

    public void setIntellMapSheet(String intellMapSheet) {
        this.intellMapSheet = intellMapSheet;
    }

    /**
     * Sets the transaction Id on the entity prior to save.
     */
    @Override
    public void preSave() {
        if (this.isNew() && this.getTransactionId() == null) {
            setTransactionId(LocalInfo.getTransactionId());
        }

        super.preSave();
    }
}
