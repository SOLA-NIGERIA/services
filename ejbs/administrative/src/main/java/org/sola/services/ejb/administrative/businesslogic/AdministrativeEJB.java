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
package org.sola.services.ejb.administrative.businesslogic;

import java.io.Serializable;
import java.util.*;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.DateUtility;
import org.sola.common.RolesConstants;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.faults.SOLAValidationException;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.administrative.repository.entities.*;
import org.sola.services.ejb.cadastre.businesslogic.CadastreEJBLocal;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObject;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObjectOT;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObjectStatusChanger;
import org.sola.services.ejb.party.repository.entities.Party;
import org.sola.services.ejb.source.repository.entities.Source;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.system.repository.entities.BrValidation;
import org.sola.services.ejb.transaction.businesslogic.TransactionEJBLocal;
import org.sola.services.ejb.transaction.repository.entities.RegistrationStatusType;
import org.sola.services.ejb.transaction.repository.entities.Transaction;
import org.sola.services.ejb.transaction.repository.entities.TransactionBasic;
import org.sola.services.ejb.transaction.repository.entities.TransactionStatusChanger;
import org.sola.services.ejb.transaction.repository.entities.TransactionStatusType;

/**
 * EJB to manage data in the administrative schema. Supports retrieving and
 * saving BA Units and RRR. Also provides methods for retrieving reference codes
 * from the administrative schema.
 */
@Stateless
@EJB(name = "java:app/AdministrativeEJBLocal", beanInterface = AdministrativeEJBLocal.class)
public class AdministrativeEJB extends AbstractEJB
        implements AdministrativeEJBLocal {

    @EJB
    private SystemEJBLocal systemEJB;
    @EJB
    private TransactionEJBLocal transactionEJB;
    @EJB
    private CadastreEJBLocal cadastreEjb;

    /**
     * Sets the entity package for the EJB to
     * BaUnit.class.getPackage().getName(). This is used to restrict the save
     * and retrieval of Code Entities.
     *
     * @see AbstractEJB#getCodeEntity(java.lang.Class, java.lang.String,
     * java.lang.String) AbstractEJB.getCodeEntity
     * @see AbstractEJB#getCodeEntityList(java.lang.Class, java.lang.String)
     * AbstractEJB.getCodeEntityList
     * @see
     * AbstractEJB#saveCodeEntity(org.sola.services.common.repository.entities.AbstractCodeEntity)
     * AbstractEJB.saveCodeEntity
     */
    @Override
    protected void postConstruct() {
        setEntityPackage(BaUnit.class.getPackage().getName());
    }

    /**
     * Retrieves all administrative.change_status_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<ChangeStatusType> getChangeStatusTypes(String languageCode) {
        return getRepository().getCodeList(ChangeStatusType.class, languageCode);
    }

    /**
     * Retrieves all administrative.ba_unit_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<BaUnitType> getBaUnitTypes(String languageCode) {
        return getRepository().getCodeList(BaUnitType.class, languageCode);
    }

    /**
     * Retrieves all administrative.lease_condition code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<RrrCondition> getRrrConditions(String languageCode) {
        return getRepository().getCodeList(RrrCondition.class, languageCode);
    }

    /**
     * Retrieves all administrative.mortgage_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<MortgageType> getMortgageTypes(String languageCode) {
        return getRepository().getCodeList(MortgageType.class, languageCode);
    }

    /**
     * Retrieves all administrative.rrr_group_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     * @return
     */
    @Override
    public List<RrrGroupType> getRRRGroupTypes(String languageCode) {
        return getRepository().getCodeList(RrrGroupType.class, languageCode);
    }

    /**
     * Retrieves all administrative.rrr_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     * @return
     */
    @Override
    public List<RrrType> getRRRTypes(String languageCode) {
        return getRepository().getCodeList(RrrType.class, languageCode);
    }

    /**
     * Retrieves all administrative.source_ba_unit_rel_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     * @return
     */
    @Override
    public List<SourceBaUnitRelationType> getSourceBaUnitRelationTypes(String languageCode) {
        return getRepository().getCodeList(SourceBaUnitRelationType.class, languageCode);
    }

    /**
     * Locates a BA Unit using by matching the first part and last part of the
     * BA Unit name. First part and last part must be an exact match.
     *
     * @param nameFirstpart The first part of the BA Unit name
     * @param nameLastpart The last part of the BA Unit name
     * @return The BA Unit matching the name
     */
    @Override
    public BaUnit getBaUnitByCode(String nameFirstpart, String nameLastpart) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYPROPERTYCODE);
        params.put(BaUnit.QUERY_PARAMETER_FIRSTPART, nameFirstpart);
        params.put(BaUnit.QUERY_PARAMETER_LASTPART, nameLastpart);
        return getRepository().getEntity(BaUnit.class, params);
    }
    
      /**
     * Locates a BA Unit using by matching the first part and last part of the
     * BA Unit name. First part and last part must be an exact match.
     *
     * @param nameFirstpart The first part of the BA Unit name
     * @param nameLastpart The last part of the BA Unit name
     * @return The BA Unit matching the name
     */
    @Override
    public List <BaUnit> getBaUnitsByString(String searchString) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYPROPERTYSTRING);
        params.put(BaUnit.QUERY_PARAMETER_STRING, searchString);
         return getRepository().getEntityList(BaUnit.class, params);
    }

    /**
     * Creates a new BA Unit with a default status of pending and a default type
     * of basicPropertyUnit. Will also create a new Transaction record for the
     * BA Unit if the Service is not already associated to a Transaction.
     *
     * <p>Requires the {@linkplain RolesConstants#ADMINISTRATIVE_BA_UNIT_SAVE}
     * role.</p>
     *
     * @param serviceId The identifier of the Service the BA Unit is being
     * created as part of
     * @param baUnitTO The details of the BA Unit to create
     * @return The new BA Unit
     * @see #saveBaUnit(java.lang.String,
     * org.sola.services.ejb.administrative.repository.entities.BaUnit)
     * saveBaUnit
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit createBaUnit(String serviceId, BaUnit baUnit) {
        if (baUnit == null) {
            return null;
        }
        return saveBaUnit(serviceId, baUnit);
    }

    /**
     * Saves any updates to an existing BA Unit. Can also be used to create a
     * new BA Unit, however this method does not set any default values on the
     * BA Unit like
     * {@linkplain #createBaUnit(java.lang.String, org.sola.services.ejb.administrative.repository.entities.BaUnit)
     * createBaUnit}. Will also create a new Transaction record for the BA Unit
     * if the Service is not already associated to a Transaction.
     *
     * <p>Requires the {@linkplain RolesConstants#ADMINISTRATIVE_BA_UNIT_SAVE}
     * role</p>
     *
     * @param serviceId The identifier of the Service the BA Unit is being
     * created as part of
     * @param baUnitTO The details of the BA Unit to create
     * @return The updated BA Unit
     * @see #createBaUnit(java.lang.String,
     * org.sola.services.ejb.administrative.repository.entities.BaUnit)
     * createBaUnit
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit saveBaUnit(String serviceId, BaUnit baUnit) {
        if (baUnit == null) {
            return null;
        }
        TransactionBasic transaction =
                transactionEJB.getTransactionByServiceId(serviceId, true, TransactionBasic.class);
        LocalInfo.setTransactionId(transaction.getId());
        return getRepository().saveEntity(baUnit);
    }

    /**
     * Retrieves the BA Unit matching the supplied identifier.
     *
     * @param id The BA Unit identifier
     * @return The BA Unit details or null if the identifier is invalid.
     */
    @Override
    public BaUnit getBaUnitById(String id) {
        BaUnit result = null;
        if (id != null) {
            result = getRepository().getEntity(BaUnit.class, id);
        }
        return result;
    }

    /**
     * Applies the appropriate approval action to every BA Unit that is
     * associated to the specified transaction. This includes updating the
     * status of RRR and Notations associated with the BA Unit. <p>Can also be
     * used to test the outcome of the approval using the validateOnly flag.</p>
     *
     * @param transactionId The Transaction identifier
     * @param approvedStatus The status to set if the validation of the BA Unit
     * is successful.
     * @param validateOnly Validate the transaction data, but do not apply and
     * status changes
     * @param languageCode Language code to use for localization of the
     * validation messages
     * @return A list of validation results.
     */
    @Override
    @RolesAllowed({RolesConstants.APPLICATION_APPROVE, RolesConstants.APPLICATION_SERVICE_COMPLETE,
    RolesConstants.APPLICATION_VALIDATE})
    public List<ValidationResult> approveTransaction(
            String transactionId, String approvedStatus,
            boolean validateOnly, String languageCode) {
        List<ValidationResult> validationResult = new ArrayList<ValidationResult>();
        if (!this.isInRole(RolesConstants.APPLICATION_APPROVE)) {
            validateOnly = true;
        }
        //Change the status of BA Units that are involved in a transaction directly
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYTRANSACTIONID);
        params.put(BaUnit.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        params.put("username", getUserName());
        List<BaUnitStatusChanger> baUnitList =
                getRepository().getEntityList(BaUnitStatusChanger.class, params);

        for (BaUnitStatusChanger baUnit : baUnitList) {
            validationResult.addAll(this.validateBaUnit(baUnit, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                baUnit.setStatusCode(approvedStatus);
                baUnit.setTransactionId(transactionId);
                getRepository().saveEntity(baUnit);
            }
        }

        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Rrr.QUERY_WHERE_BYTRANSACTIONID);
        params.put(Rrr.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        params.put("username", getUserName());
        List<RrrStatusChanger> rrrStatusChangerList =
                getRepository().getEntityList(RrrStatusChanger.class, params);
        for (RrrStatusChanger rrr : rrrStatusChangerList) {
            validationResult.addAll(this.validateRrr(rrr, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                rrr.setStatusCode(approvedStatus);
                getRepository().saveEntity(rrr);
            }
        }
        if (!validateOnly) {
            params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitNotation.QUERY_WHERE_BYTRANSACTIONID);
            params.put(BaUnitNotation.QUERY_PARAMETER_TRANSACTIONID, transactionId);
            params.put("username", getUserName());
            params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BaUnitNotation.QUERY_ORDER_BY);

            List<BaUnitNotationStatusChanger> baUnitNotationList =
                    getRepository().getEntityList(BaUnitNotationStatusChanger.class, params);
            for (BaUnitNotationStatusChanger baUnitNotation : baUnitNotationList) {
                baUnitNotation.setStatusCode(RegistrationStatusType.STATUS_CURRENT);
                getRepository().saveEntity(baUnitNotation);
            }
        }

        return validationResult;
    }

    /**
     * Executes the business rules to validate the BA Unit.
     *
     * @param baUnit The BA Unit to validate
     * @param languageCode The language code to use for localization of any
     * validation messages
     * @return The list of validation results.
     */
    private List<ValidationResult> validateBaUnit(
            BaUnitStatusChanger baUnit, String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForValidatingTransaction(
                "ba_unit", RegistrationStatusType.STATUS_CURRENT, null);
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("id", baUnit.getId());
        //Run the validation
        return this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, params);
    }

    /**
     * Executes the business rules to validate the RRR.
     *
     * @param rrr The RRR to validate
     * @param languageCode The language code to use for localization of any
     * validation messages
     * @return The list of validation results.
     */
    private List<ValidationResult> validateRrr(
            RrrStatusChanger rrr, String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForValidatingRrr(
                RegistrationStatusType.STATUS_CURRENT, rrr.getTypeCode());
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("id", rrr.getId());
        params.put("username", getUserName());
        //Run the validation
        return this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, params);
    }

    /**
     * Returns all BA Units that are associated to the specified transaction
     *
     * @param transactionId The Transaction identifier
     */
    @Override
    public List<BaUnit> getBaUnitsByTransactionId(String transactionId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BY_TRANSACTION_ID_EXTENDED);
        params.put(BaUnit.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        return getRepository().getEntityList(BaUnit.class, params);
    }

    /**
     * Retrieves all administrative.ba_unit_rel_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     * @return
     */
    @Override
    public List<BaUnitRelType> getBaUnitRelTypes(String languageCode) {
        return getRepository().getCodeList(BaUnitRelType.class, languageCode);
    }

    /**
     * Identifies a BA Unit as subject to cancellation / termination by linking
     * the BA Unit to a Transaction via the administrative.ba_unit_target
     * association. The BA Unit is not canceled / terminated until the
     * application canceling the BA Unit is approved.
     *
     * <p>Requires the {@linkplain RolesConstants#ADMINISTRATIVE_BA_UNIT_SAVE}
     * role.</p>
     *
     * @param baUnitId The identifier of the BA Unit to be canceled / terminated
     * @param serviceId The identifier of the service that is canceling /
     * terminating the BA Unit
     * @return The BA Unit that will be canceled / terminated.
     * @see #cancelBaUnitTermination(java.lang.String) cancelBaUnitTermination
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit terminateBaUnit(String baUnitId, String serviceId) {
        if (baUnitId == null || serviceId == null) {
            return null;
        }

        // Check transaction to exist and have pending status
        Transaction transaction = transactionEJB.getTransactionByServiceId(
                serviceId, true, Transaction.class);
        if (transaction == null || !transaction.getStatusCode().equals(RegistrationStatusType.STATUS_PENDING)) {
            return null;
        }

        //TODO: Put BR check to have only one pending transaction for the BaUnit and BaUnit to be with "current" status.
        //TODO: Check BR for service to have cancel action and empty Rrr field.

        BaUnitTarget baUnitTarget = new BaUnitTarget();
        baUnitTarget.setBaUnitId(baUnitId);
        baUnitTarget.setTransactionId(transaction.getId());
        getRepository().saveEntity(baUnitTarget);

        return getBaUnitById(baUnitId);
    }

    /**
     * Reverses the cancellation / termination of a BA Unit by removing the BA
     * Unit Target created by {@linkplain #terminateBaUnit(java.lang.String, java.lang.String) terminateBaUnit}.
     * <p>Requires the {@linkplain RolesConstants#ADMINISTRATIVE_BA_UNIT_SAVE}
     * role.</p>
     *
     * @param baUnitId The identifier of the BA Unit to reverse the cancellation
     * for.
     * @return The details of the BA Unit that has had its termination canceled.
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit cancelBaUnitTermination(String baUnitId) {
        if (baUnitId == null) {
            return null;
        }

        //TODO: Put BR check to have only one pending transaction for the BaUnit and BaUnit to be with "current" status.

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitTarget.QUERY_WHERE_GET_BY_BAUNITID);
        params.put(BaUnitTarget.PARAM_BAUNIT_ID, baUnitId);

        List<BaUnitTarget> targets = getRepository().getEntityList(BaUnitTarget.class, params);

        if (targets != null && targets.size() > 0) {
            for (BaUnitTarget baUnitTarget : targets) {
                Transaction transaction = transactionEJB.getTransactionById(
                        baUnitTarget.getTransactionId(), Transaction.class);
                if (transaction != null
                        && transaction.getStatusCode().equals(RegistrationStatusType.STATUS_PENDING)) {
                    // DELETE peding 
                    baUnitTarget.setEntityAction(EntityAction.DELETE);
                    getRepository().saveEntity(baUnitTarget);
                }
            }
        }

        return getBaUnitById(baUnitId);
    }

    /**
     * Retrieves the actions a specific user has performed against any
     * application during a specific period.
     *
     * @param baUnitId
     * @return The list of areas of the baunit
     */
    @Override
    public BaUnitArea getBaUnitAreas(String baUnitId) {
        BaUnitArea result = null;
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitArea.QUERY_WHERE_BYUNITAREAID);
        params.put(BaUnitArea.QUERY_WHERE_BYBAUNITID, baUnitId);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BaUnitArea.QUERY_ORDER_BYCHANGETIME);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, 1);
        result = getRepository().getEntity(BaUnitArea.class, params);

        return result;
    }
    
    /**
     * Returns list of BaUnit areas by cadastre object id
     */
    @Override
    public List<BaUnitArea> getBaUnitAreasByCadastreObject(String coId){
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitArea.QUERY_WHERE_BY_CADASTRE_OBJECT);
        params.put(BaUnitArea.QUERY_PARAM_CO_ID, coId);
        return getRepository().getEntityList(BaUnitArea.class, params);
    }

    /**
     * Creates a new BA Unit Area <p>Requires the {@linkplain RolesConstants#ADMINISTRATIVE_BA_UNIT_SAVE}
     * role.</p>
     *
     * @param baUnitId The identifier of the area the BA Unit is being created
     * as part of
     * @param baUnitAreaTO The details of the BA Unit to create
     * @return The new BA Unit Area
     * @see #saveBaUnit(java.lang.String,
     * org.sola.services.ejb.administrative.repository.entities.BaUnitArea)
     * createBaUnit
     */
    @Override
//    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnitArea createBaUnitArea(String baUnitId, BaUnitArea baUnitArea) {
        if (baUnitArea == null) {
            return null;
        }
        return getRepository().saveEntity(baUnitArea);
    }

    /**
     * Locates a BA Unit and cadastre object's area size
     *
     *
     * @param id The BA Unit id
     * @param colist the list of cadastre object for the ba unit
     * @return The BA Unit matching the name
     */
    @Override
    public BaUnit getBaUnitWithCadObject(String nameFirstPart, String nameLastPart, String colist) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYPROPERTYCODE);
        params.put(BaUnit.QUERY_PARAMETER_FIRSTPART, nameFirstPart);
        params.put(BaUnit.QUERY_PARAMETER_LASTPART, nameLastPart);
        params.put(BaUnit.QUERY_PARAMETER_COLIST, colist);
        return getRepository().getEntity(BaUnit.class, params);
    }
    
    @Override
    public List <BaUnit> getBaUnitsByCadObject(String colist) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYCO);  
        params.put(BaUnit.QUERY_PARAMETER_COLIST, colist);
        return getRepository().getEntityList(BaUnit.class, params);
    }
    
    /**
     * Returns a maximum of 10 cadastre objects that have a name first part
     * and/or name last part that matches the specified search string. This
     * method supports partial matches and is case insensitive.
     *
     * @param searchString The search string to use
     * @return The list of Parcels matching the search string
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegPubDisParcelName> getSysRegPubDisParcelNameByLocation(String searchString, String languageCode) {

        this.validatePublicDisplay(searchString, languageCode);
        HashMap params = new HashMap();
        params.put("search_string", searchString);

        return getRepository().getEntityList(SysRegPubDisParcelName.class,
                SysRegPubDisParcelName.QUERY_WHERE_SEARCHBYPARTS, params);
    }

    /**
     * Returns a maximum of 10 cadastre objects that have a name first part
     * and/or name last part that matches the specified search string. This
     * method supports partial matches and is case insensitive.
     *
     * @param searchString The search string to use
     * @return The list of Parcels matching the search string
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegPubDisOwnerName> getSysRegPubDisOwnerNameByLocation(String searchString, String languageCode) {
        this.validatePublicDisplay(searchString, languageCode);
        HashMap params = new HashMap();
        params.put("search_string", searchString);
        return getRepository().getEntityList(SysRegPubDisOwnerName.class,
                SysRegPubDisParcelName.QUERY_WHERE_SEARCHBYPARTS, params);
    }

    /**
     * Returns a maximum of 10 cadastre objects that have a name first part
     * and/or name last part that matches the specified search string. This
     * method supports partial matches and is case insensitive.
     *
     * @param searchString The search string to use
     * @return The list of Parcels matching the search string
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegPubDisStateLand> getSysRegPubDisStateLandByLocation(String searchString, String languageCode) {
        this.validatePublicDisplay(searchString, languageCode);
        HashMap params = new HashMap();
        params.put("search_string", searchString);

        return getRepository().getEntityList(SysRegPubDisStateLand.class,
                SysRegPubDisParcelName.QUERY_WHERE_SEARCHBYPARTS, params);
    }
    
     /**
     * Returns  objects that have a name first part
     * and/or name last part that matches the specified search string. This
     * method supports partial matches and is case insensitive.
     *
     * @param searchString The search string to use
     * @return The list of Parcels matching the search string
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegSigningList> getSysRegSigningList(String searchString, String languageCode) {

        this.validatePublicDisplay(searchString, languageCode);
        HashMap params = new HashMap();
        params.put("search_string", searchString);

        return getRepository().getEntityList(SysRegSigningList.class,
                SysRegSigningList.QUERY_WHERE_SEARCHBYPARTS, params);
    }


    /**
     * It validates BRs for the public display. If one of the critical BRs is
     * violated, the exception is thrown.
     *
     * @param lastPart The last part of the objects that has to be reported in
     * the public display.
     * @param languageCode
     */
    private List<ValidationResult> validatePublicDisplay(String lastPart, String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForPublicDisplay();
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("lastPart", lastPart);
        //Run the validation
        List<ValidationResult> validationResult = this.systemEJB.checkRulesGetValidation(
                brValidationList, languageCode, params);
        if (!systemEJB.validationSucceeded(validationResult)) {
            throw new SOLAValidationException(validationResult);
        }
        return this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, params);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<ValidationResult> publicDisplay(
            String params, String languageCode) {
        return this.validatePublicDisplay(params, languageCode);
    }

    /**
     * Returns list of systematic registration applications that matches the
     * specified search string. This
     *
     *
     * @param searchString The search string to use
     * @return list of systematic registration applications matching the search
     * string
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegManagement> getSysRegManagement(SysRegManagementParams params, String languageCode) {
        List<SysRegManagement> result;
        Map queryParams = new HashMap<String, Object>();
        queryParams.put(CommonSqlProvider.PARAM_QUERY, SysRegManagement.QUERY_GETQUERY);

        queryParams.put(SysRegManagement.PARAMETER_FROM,
                params.getFromDate() == null ? new GregorianCalendar(1, 1, 1).getTime() : params.getFromDate());
        queryParams.put(SysRegManagement.PARAMETER_TO,
                params.getToDate() == null ? new GregorianCalendar(2500, 1, 1).getTime() : params.getToDate());
        queryParams.put(SysRegManagement.QUERY_PARAMETER_LASTPART, params.getNameLastpart());
        result = getRepository().executeFunction(queryParams, SysRegManagement.class);
        return result;
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegStatus> getSysRegStatus(SysRegManagementParams params, String languageCode) {
        List<SysRegStatus> result;
        Map queryParams = new HashMap<String, Object>();
        queryParams.put(CommonSqlProvider.PARAM_QUERY, SysRegStatus.QUERY_GETQUERY);

        queryParams.put(SysRegStatus.PARAMETER_FROM,
                params.getFromDate() == null ? new GregorianCalendar(1, 1, 1).getTime() : params.getFromDate());
        queryParams.put(SysRegStatus.PARAMETER_TO,
                params.getToDate() == null ? new GregorianCalendar(2500, 1, 1).getTime() : params.getToDate());
        queryParams.put(SysRegStatus.QUERY_PARAMETER_LASTPART, params.getNameLastpart());
        result = getRepository().executeFunction(queryParams, SysRegStatus.class);
        return result;
    }
    
    
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegGender> getSysRegGender(String searchString, String languageCode) {
        List<SysRegGender> result;
        Map queryParams = new HashMap<String, Object>();
        queryParams.put(CommonSqlProvider.PARAM_QUERY, SysRegGender.QUERY_GETQUERY);

        result = getRepository().executeFunction(queryParams, SysRegGender.class);
        return result;
    }
    
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_SYSTEMATIC_REGISTRATION)
    public List<SysRegProgress> getSysRegProgress(SysRegManagementParams params, String languageCode) {
        List<SysRegProgress> result;
        Map queryParams = new HashMap<String, Object>();
        queryParams.put(CommonSqlProvider.PARAM_QUERY, SysRegProgress.QUERY_GETQUERY);

        queryParams.put(SysRegProgress.PARAMETER_FROM,
                params.getFromDate() == null ? new GregorianCalendar(1, 1, 1).getTime() : params.getFromDate());
        queryParams.put(SysRegProgress.PARAMETER_TO,
                params.getToDate() == null ? new GregorianCalendar(2500, 1, 1).getTime() : params.getToDate());
        queryParams.put(SysRegProgress.QUERY_PARAMETER_LASTPART, params.getNameLastpart());
        result = getRepository().executeFunction(queryParams, SysRegProgress.class);
        return result;
    }

    /**
     * DISPUTE
     *
     */

    /*
     * Retrieves the DisputeComments matching the supplied identifier.
     *
     * @param id The DisputeComments identifier @return The DisputeComments
     * details or null if the identifier is invalid.
     */
    @Override
    public DisputeComments getDisputeCommentsById(String id) {
        DisputeComments result = null;
        if (id != null) {
            result = getRepository().getEntity(DisputeComments.class, id);
        }
        return result;
    }

    @Override
    public Dispute getDisputeByService(String serviceId) {
        if (serviceId != null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, Dispute.QUERY_WHERE_BYSERVICEID);
            params.put(Dispute.QUERY_PARAMETER_SERVICEID, serviceId);
            return getRepository().getEntity(Dispute.class, params);
        } else {
            return null;
        }
    }

    /**
     * Returns the details for the specified Dispute.
     *
     * <p>No role is required to execute this method.</p>
     *
     * @param id The identifier of the source to retrieve.
     */
    @Override
    public Dispute getDisputeById(String id) {
        return getRepository().getEntity(Dispute.class, id);
    }

    /**
     * Returns the details for the specified Dispute.
     *
     * <p>No role is required to execute this method.</p>
     *
     * @param nr The identifier of the source to retrieve.
     */
    @Override
    public Dispute getDisputeByNr(String nr) {
        return getRepository().getEntity(Dispute.class, nr);
    }

    /**
     * Returns the details for the specified Dispute Party.
     *
     * <p>No role is required to execute this method.</p>
     *
     * @param Id The identifier of the source to retrieve.
     */
    @Override
    public DisputeParty getDisputePartyById(String id) {
        DisputeParty result = null;
        if (id != null) {
            result = getRepository().getEntity(DisputeParty.class, id);
        }
        return result;
    }

    @Override
    public Dispute getDisputeByUser(String userId) {
        if (userId != null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, Dispute.QUERY_WHERE_BYUSERID);
            params.put(Dispute.QUERY_PARAMETER_USERID, userId);
            return getRepository().getEntity(Dispute.class, params);
        } else {
            return null;
        }
    }

    @Override
    public DisputeComments getDisputeCommentsByDispute(String disputeNr) {
        if (disputeNr != null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, DisputeComments.QUERY_WHERE_BYDISPUTENR);
            params.put(DisputeComments.QUERY_PARAMETER_BYDISPUTENR, disputeNr);
            return getRepository().getEntity(DisputeComments.class, params);
        } else {
            return null;
        }

    }
    
    @Override
    public List<DisputeParty> getDisputePartyByDispute(String disputeNr) {
        if (disputeNr != null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, DisputeParty.QUERY_WHERE_BYDISPUTENR);
            params.put(DisputeComments.QUERY_PARAMETER_BYDISPUTENR, disputeNr);
            return getRepository().getEntityList(DisputeParty.class, params);
        } else {
            return null;
        }

    }

    
    @Override
    public Dispute getDispute(String id) {
        Dispute result = null;
        if (id != null) {
            result = getRepository().getEntity(Dispute.class, id);
        }
        return result;
    }

    /**
     * Creates a new Dispute record with a default status of pending.
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_DISPUTE_SAVE)
    public Dispute createDispute(Dispute dispute) {
        if (dispute == null) {
            return null;
        }

        if (dispute.getLodgementDate() == null) {
            dispute.setLodgementDate(DateUtility.now());
        }

        if (dispute.getCompletionDate() == null) {
            dispute.setCompletionDate(DateUtility.now());
        }
        return saveDispute(dispute);
    }

    /**
     * Saves any updates to an existing Dispute.
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_DISPUTE_SAVE)
    public Dispute saveDispute(Dispute dispute) {
        if (dispute == null) {
            return dispute;
        }

        dispute = getRepository().saveEntity(dispute);

        return dispute;

    }

    /**
     * Saves any updates to Dispute comments.
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_DISPUTE_COMMENTS_SAVE)
    public DisputeComments saveDisputeComments(DisputeComments disputeComments) {
        if (disputeComments == null) {
            return null;
        }

        return getRepository().saveEntity(disputeComments);
    }

    /**
     * Retrieves all administrative.dispute_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<DisputeType> getDisputeType(String languageCode) {
        return getRepository().getCodeList(DisputeType.class, languageCode);
    }
    
     /**
     * Retrieves all administrative.dispute_role_type code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<DisputeRoleType> getDisputeRoleType(String languageCode) {
        return getRepository().getCodeList(DisputeRoleType.class, languageCode);
    }

   

    /**
     * Retrieves all administrative.dispute_category code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<DisputeCategory> getDisputeCategory(String languageCode) {
        return getRepository().getCodeList(DisputeCategory.class, languageCode);
    }

    /**
     * Retrieves all administrative.dispute_status code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<DisputeStatus> getDisputeStatus(String languageCode) {
        return getRepository().getCodeList(DisputeStatus.class, languageCode);
    }

    /**
     * Retrieves all administrative.other_authorities code values.
     *
     * @param languageCode The language code to use for localization of display
     * values.
     */
    @Override
    public List<OtherAuthorities> getOtherAuthorities(String languageCode) {
        return getRepository().getCodeList(OtherAuthorities.class, languageCode);
    }

    /**
     * Saves any updates to Dispute Party.
     */
    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_DISPUTE_PARTY_SAVE)
    public DisputeParty saveDisputeParty(DisputeParty disputeParty) {
        if (disputeParty == null) {
            return null;
        }

        return getRepository().saveEntity(disputeParty);
    }
    
    @Override
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SETTINGS)
    public boolean importBaUnit(BaUnitOT baUnit, String redcadId, String sltrId) {
        if (baUnit == null) {
            return false;
        }

        // Create transaction
        Transaction tran = new Transaction();
        tran.setFromServiceId(sltrId);
        getRepository().saveEntity(tran);
        
        Transaction tran2 = new Transaction();
        tran2.setFromServiceId(redcadId);
        getRepository().saveEntity(tran2);
        
        if (tran == null) {
            throw new RuntimeException("Transaction not found");
        }

        // Save BaUnit
        LocalInfo.setTransactionId(tran.getId());
        baUnit.setTransactionId(tran.getId());
        if (baUnit.getRrrList() != null) {
            for (Rrr rrr : baUnit.getRrrList()) {
                rrr.setTransactionId(tran.getId());
                // Save parties first
                if (rrr.getRrrShareList() != null) {
                    for (RrrShare share : rrr.getRrrShareList()) {
                        if (share.getRightHolderList() != null) {
                            for (Party party : share.getRightHolderList()) {
                                getRepository().saveEntity(party);
                            }
                        }
                    }
                }
                // Save sources
                if (baUnit.getSourceList() != null) {
                    for (Source source : baUnit.getSourceList()) {
                        getRepository().saveEntity(source);
                    }
                }
                
                if (rrr.getSourceList() != null) {
                    for (Source source : rrr.getSourceList()) {
                        getRepository().saveEntity(source);
                    }
                }
                // Assign transaction ID to notation
                if (rrr.getNotation() != null) {
                    rrr.getNotation().setTransactionId(tran.getId());
                }
            }
        }
        
        if (baUnit.getCadastreObjectList() != null) {
            for (CadastreObjectOT cadastreObject : baUnit.getCadastreObjectList()) {
                cadastreObject.setTransactionId(tran.getId());
            }
        }

        getRepository().saveEntity(baUnit);

        // Set approved status to cadastre objects
        List<ValidationResult> validationResult = new ArrayList<ValidationResult>();
        cadastreEjb.ChangeStatusOfCadastreObjects(
                tran.getId(),
                CadastreObjectStatusChanger.QUERY_WHERE_SEARCHBYTRANSACTION_PENDING,
//                 RegistrationStatusType.STATUS_PENDING);
                RegistrationStatusType.STATUS_CURRENT);

        // Set approved status to BaUnit
        BaUnitStatusChanger baUnitStatusChanger = getRepository()
                .getEntity(BaUnitStatusChanger.class, baUnit.getId());

        //validationResult.addAll(this.validateBaUnit(baUnitStatusChanger, "en-US"));
        //if (systemEJB.validationSucceeded(validationResult)) {
        baUnitStatusChanger.setStatusCode(RegistrationStatusType.STATUS_PENDING);
        baUnitStatusChanger.setTransactionId(tran.getId());
        getRepository().saveEntity(baUnitStatusChanger);
        //} 
        
        // Set approved status for RRRs
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Rrr.QUERY_WHERE_BYTRANSACTIONID);
        params.put(Rrr.QUERY_PARAMETER_TRANSACTIONID, tran.getId());
        params.put("username", getUserName());
        List<RrrStatusChanger> rrrStatusChangerList
                = getRepository().getEntityList(RrrStatusChanger.class, params);
        for (RrrStatusChanger rrr : rrrStatusChangerList) {
            //validationResult.addAll(this.validateRrr(rrr, "en-US"));
            //if (systemEJB.validationSucceeded(validationResult)) {
            rrr.setStatusCode(RegistrationStatusType.STATUS_PENDING);
            getRepository().saveEntity(rrr);
            //}
        }

        // Approve noations
        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitNotation.QUERY_WHERE_BYTRANSACTIONID);
        params.put(BaUnitNotation.QUERY_PARAMETER_TRANSACTIONID, tran.getId());
        params.put("username", getUserName());

        List<BaUnitNotationStatusChanger> baUnitNotationList
                = getRepository().getEntityList(BaUnitNotationStatusChanger.class, params);
        for (BaUnitNotationStatusChanger baUnitNotation : baUnitNotationList) {
            baUnitNotation.setStatusCode(RegistrationStatusType.STATUS_PENDING);
            getRepository().saveEntity(baUnitNotation);
        }

        // Approve transaction
        TransactionStatusChanger tranStatusChanger = getRepository().getEntity(TransactionStatusChanger.class, tran.getId());
//        tranStatusChanger.setStatusCode(TransactionStatusType.APPROVED);
        tranStatusChanger.setStatusCode(TransactionStatusType.PENDING);
        tranStatusChanger.setApprovalDatetime(DateUtility.now());
        getRepository().saveEntity(tranStatusChanger);
        
        TransactionStatusChanger tranStatusChanger2 = getRepository().getEntity(TransactionStatusChanger.class, tran2.getId());
        tranStatusChanger2.setStatusCode(TransactionStatusType.PENDING);
        tranStatusChanger2.setApprovalDatetime(DateUtility.now());
        getRepository().saveEntity(tranStatusChanger2);
        
        if (!systemEJB.validationSucceeded(validationResult)) {
            throw new SOLAValidationException(validationResult);
        }

        return true;
    }
      @Override
    public LeaseConditionTemplate getLeaseConditionTemplate(String id) {
        return getRepository().getEntity(LeaseConditionTemplate.class, id);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_REFDATA)
    public LeaseConditionTemplate saveLeaseConditionTemplate(LeaseConditionTemplate template) {
        return getRepository().saveEntity(template);
    }   

    @Override
    public List<CofoType> getCofoTypes(String languageCode) {
        return getRepository().getCodeList(CofoType.class, languageCode);
    }
    
    @Override
    public List<RotType> getRotTypes(String languageCode) {
        return getRepository().getCodeList(RotType.class, languageCode);
    }
}
