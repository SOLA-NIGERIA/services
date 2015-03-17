/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations
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
package org.sola.services.ejbs.admin.businesslogic;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.util.ByteArrayDataSource;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.lang.StringUtils;
import org.sola.common.ConfigConstants;
import org.sola.common.DateUtility;
import org.sola.common.FileUtility;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.StringUtility;
import org.sola.common.messaging.MessageUtility;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;

import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.faults.SOLAValidationException;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.system.br.Result;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.system.repository.entities.BrValidation;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.ConsolidationConfig;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.GroupSummary;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Language;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Role;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.User;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Group;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.UserGroup;

/**
 * Contains business logic methods to administer system settings, users and
 * roles.
 */
@Stateless
@EJB(name = "java:global/SOLA_SR/AdminEJBLocal", beanInterface = AdminEJBLocal.class)
public class AdminEJB extends AbstractEJB implements AdminEJBLocal {

    @EJB
    private SystemEJBLocal systemEJB;

    /**
     * Returns the list of all users from the database.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<User> getUsers() {
        return getRepository().getEntityList(User.class);
    }

    /**
     * Returns the details of the user with the specified user name.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     *
     * @param userName The user name of the user to search for.
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public User getUser(String userName) {
        return getUserInfo(userName);
    }
/**
     * Returns the details of the user with the specified user name. Should be
     * used only between EJBs, not exposing this method outside. It has no
     * security roles.
     *
     * @param userName The user name of the user to search for.
     * @return
     */
    @Override
    public User getUserInfo(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, userName);
        return getRepository().getEntity(User.class, params);
    }

    /**
     * Returns the details for the currently authenticated user.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public User getCurrentUser() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntity(User.class, params);
    }

    /**
     * Can be used to create a new user or save any updates to the details of an
     * existing user. Cannot be used to change the users password. This can only
     * be done using the
     * {@linkplain #changePassword(java.lang.String, java.lang.String) changePassword}
     * method.
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param user The details of the user to save
     * @return The user details after the save is completed
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public User saveUser(User user) {
        return getRepository().saveEntity(user);
    }

    
    /**
     * Returns the list of all security roles in SOLA.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public List<Role> getRoles() {
        return getRepository().getEntityList(Role.class);
    }

    /**
     * Returns the role for the specified role code
     * <p>
     * No role is required to execute this method.</p>
     *
     * @param roleCode The role code to retrieve
     */
    @PermitAll
    @Override
    public Role getRole(String roleCode) {
        return getRepository().getEntity(Role.class, roleCode);
    }

    /**
     * Returns the list of all user groups supported by SOLA.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Group> getGroups() {
        return getRepository().getEntityList(Group.class);
    }

    /**
     * Can be used to create a new user group or save any updates to the details
     * of an existing user group.
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param userGroup The details of the user group to save
     * @return The user group after the save is completed
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group saveGroup(Group userGroup) {
        return getRepository().saveEntity(userGroup);
    }

    /**
     * Returns the details for the specified group.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     *
     * @param groupId The identifier of the group to retrieve from the SOLA
     * database
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group getGroup(String groupId) {
        return getRepository().getEntity(Group.class, groupId);
    }

    /**
     * Can be used to create a new security role or save any updates to the
     * details of an existing security role.
     * <p>
     * Note that security roles are linked to the SOLA code base. Adding a new
     * role also requires updating code before SOLA will recognize the role</p>
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role. </p>
     *
     * @param role The details of the security role to save
     * @return The security role after the save is completed
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Role saveRole(Role role) {
        return getRepository().saveEntity(role);
    }

    /**
     * Returns a summary list of all user groups supported by SOLA.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.</p>
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<GroupSummary> getGroupsSummary() {
        return getRepository().getEntityList(GroupSummary.class);
    }

    /**
     * Allows the users password to be changed
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_CHANGE_PASSWORD} role. </p>
     *
     * @param userName The username to change the password for
     * @param password The users new password
     * @return true if the change is successful.
     */
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_CHANGE_PASSWORD})
    @Override
    public boolean changePassword(String userName, String password) {
    
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, User.QUERY_SET_PASSWORD);
        params.put(User.PARAM_PASSWORD, getPasswordHash(password));
        params.put(User.PARAM_USERNAME, userName);

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return ((Integer) ((Entry) list.get(0).entrySet().iterator().next()).getValue()) > 0;
        } else {
            return false;
        }
    }

    /**
     * Returns SHA-256 hash for the password.
     *
     * @param password Password string to hash.
     */
    private String getPasswordHash(String password) {
        String hashString = null;

        if (password != null && password.length() > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();

                // Ticket #410 - Fix password encyption. Ensure 0 is prepended
                // if the hex length is == 1 
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }
                
                hashString = sb.toString();

            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }

        return hashString;
    }

    /**
     * Returns all roles associated to the specified username.
     *
     * <p>
     * Requires the {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY} role.
     * </p>
     *
     * @param userName The username to use for retrieval of the roles.
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Role> getUserRoles(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, userName);
        isInRole(userName);
        return getRepository().getEntityList(Role.class, params);
    }

    /**
     * Returns the list of all security roles assigned to the current user.
     *
     * <p>
     * No role is required to execute this method.</p>
     */
    @PermitAll
    @Override
    public List<Role> getCurrentUserRoles() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntityList(Role.class, params);
    }

    /**
     * Checks if the current user has been assigned one or more of the null null
     * null null     {@linkplain RolesConstants#ADMIN_MANAGE_SECURITY},
     * {@linkplain RolesConstants#ADMIN_MANAGE_REFDATA} or
     * {@linkplain RolesConstants#ADMIN_MANAGE_SETTINGS} security roles.
     * <p>
     * No role is required to execute this method.</p>
     *
     * @return true if the user is assigned one of the Admin security roles
     */
    @PermitAll
    @Override
    public boolean isUserAdmin() {
        return isInRole(RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_MANAGE_REFDATA,
                RolesConstants.ADMIN_MANAGE_SETTINGS);
    }

    /**
     * Returns the list of languages supported by SOLA for localization in
     * priority order.
     *
     * <p>
     * No role is required to execute this method.</p>
     *
     * @param lang The language code to use to localize the display value for
     * each language.
     */
    @PermitAll
    @Override
    public List<Language> getLanguages(String lang) {
        Map params = new HashMap<String, Object>();
        if(lang != null){
            params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        }
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, "item_order");
        return getRepository().getEntityList(Language.class, params);
    }

    /**
     * It generates a script with the extracted records during the consolidation
     * process. The generated script can be used to consolidate the records to
     * another database.
     *
     * @return The file name as it is saved in the server and ready for
     * download.
     *
     * @throws IOException
     */
     @RolesAllowed(RolesConstants.CONSOLIDATION_EXTRACT)
    @Override
    public String consolidationExtract(String processName, boolean everything, String password) {
        String sqlStatement = "select system.consolidation_extract(#{current_user}, #{everything}, #{process_name}) as vl";
        String schemaName = "consolidation";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("current_user", getUser(getUserName()).getId());
        params.put("everything", everything);
        params.put("process_name", processName);
        try {
            startProcessLog(processName);
            Boolean consolidationSchemaReady = getRepository().getScalar(Boolean.class, params);
            if (!consolidationSchemaReady) {
                throw new SOLAException(ServiceMessage.ADMIN_WS_EXTRACTION_CONSOLIDATION_SCHEMA_FAILED);
            }
            //Extract the script from the schema consolidation
            ArrayList filesToAdd = new ArrayList();
            //Extract script for schema structure only
            updateProcessLog(processName, "Retrieve script for schema structure...");
            sqlStatement = "select system.get_text_from_schema_only(#{consolidation_schema})";
            params.clear();
            params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
            params.put("consolidation_schema", schemaName);
            String scriptPart = getRepository().getScalar(String.class, params);
            updateProcessLog(processName, "done");
            setProcessProgress(processName, getProcessProgress(processName, false) + 1);
            int fileIndex = 0;
            String filePath = FileUtility.createFileFromContent(String.format("%s_%04d_schema.sql", schemaName, fileIndex), scriptPart);
            setProcessProgress(processName, getProcessProgress(processName, false) + 1);
            filesToAdd.add(filePath);

            //Extract config table data
            ArrayList filesOfConfigTableData = extractTableRows(
                    processName, schemaName, "config", 10000, fileIndex);
            fileIndex = fileIndex + filesOfConfigTableData.size();
            filesToAdd.addAll(filesOfConfigTableData);

            //Extract the data for each table found in consolidation config table
            params.clear();
            params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, ConsolidationConfig.ORDER_BY);
            List<ConsolidationConfig> consolidationConfigList =
                    getRepository().getEntityList(ConsolidationConfig.class, params);

            for (ConsolidationConfig consolidationConfig : consolidationConfigList) {
                ArrayList extractedFiles = extractTableRows(
                        processName, schemaName,
                        consolidationConfig.getConsolidatedTableName(),
                        consolidationConfig.getNrRowsAtOnce(), fileIndex);
                fileIndex = fileIndex + extractedFiles.size();
                filesToAdd.addAll(extractedFiles);
                setProcessProgress(processName, getProcessProgress(processName, false) + 5);
            }
            updateProcessLog(processName, "Compress all generated scripts...");
            String compressedFilePath = FileUtility.compress(
                    systemEJB.checkRuleGetResultSingle("consolidation-extraction-file-name", null).getValue().toString(),
                    filesToAdd, password);
            updateProcessLog(processName, "done");
            updateProcessLog(processName, "Finished with success!");
            setProcessProgress(processName, getProcessProgress(processName, false) + 10);
            return compressedFilePath;
        } catch (Exception ex) {
            updateProcessLog(processName, String.format("%s\r\n%s",
                    MessageUtility.getLocalizedMessage(ServiceMessage.ADMIN_WS_EXTRACTION_FAILED).getMessage(),
                    ex.getMessage()));
            throw new SOLAException(ServiceMessage.ADMIN_WS_EXTRACTION_FAILED, ex);
        }
    }

    private ArrayList extractTableRows(
            String processName, String schemaName, String tableName,
            int rowsAtOnce, int fileIndex) throws IOException {

        String sqlStatement = "select system.get_text_from_schema_table(#{schema_name}, #{table_name}, #{rows_at_once}, #{start_row_nr})";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("schema_name", schemaName);

        updateProcessLog(processName,
                String.format("Retrieve script for table:%s  - rows at once:%s   ... ",
                tableName, rowsAtOnce));
        params.put("table_name", tableName);
        params.put("rows_at_once", rowsAtOnce);
        int fromRowNumber = 1;
        ArrayList filesToAdd = new ArrayList();
        String scriptPart;
        do {
            updateProcessLog(processName, String.format("    From row: %s ", fromRowNumber));
            params.put("start_row_nr", fromRowNumber);
            scriptPart = getRepository().getScalar(String.class, params);
            if (!scriptPart.isEmpty()) {
                String filePath = FileUtility.createFileFromContent(
                        String.format("%s_%04d_%s-start_row_%s.sql", schemaName, ++fileIndex, tableName, fromRowNumber),
                        scriptPart);
                filesToAdd.add(filePath);
                updateProcessLog(processName, "    Rows extracted. File created.");
            } else {
                updateProcessLog(processName, "    No rows extracted. No file created.");
            }
            fromRowNumber += rowsAtOnce;
        } while (!scriptPart.isEmpty());
        updateProcessLog(processName, "done");
        return filesToAdd;
    }

  

    /**
     * It takes a file name that is in the server cache folder which is supposed
     * to be the consolidated records and makes the consolidation.
     *
     * @param processName
     * @param languageCode
     * @param fileInServer
     * @param password
     * @return
     */
    @RolesAllowed(RolesConstants.CONSOLIDATION_CONSOLIDATE)
    @Override
    public void consolidationConsolidate(String processName, String languageCode, String fileInServer, String password) {

        String sqlStatementUpload = "select system.run_script(#{script}) as vl";
        String sqlStatementConsolidate = "select system.consolidation_consolidate(#{current_user}, #{process_name}) as vl";
        //Start process progress and log
        try {
            startProcessLog(processName);
            // Run all the scripts in the archive one by one
            updateProcessLog(processName, "Extracting and loading scripts to the database");
            Map params = new HashMap();
            params.clear();
            params.put(CommonSqlProvider.PARAM_QUERY, sqlStatementUpload);
            ZipFile zipFile = FileUtility.getArchiveFile(fileInServer, password);
            for (int fileHeaderIndex = 0;
                    fileHeaderIndex < zipFile.getFileHeaders().size();
                    fileHeaderIndex++) {
                //Get the script from the archive
                FileHeader fileHeader = (FileHeader) zipFile.getFileHeaders().get(fileHeaderIndex);
                updateProcessLog(processName, String.format("  - Script: %s ", fileHeader.getFileName()));
                updateProcessLog(processName, "      extract...");
                String script = new String(FileUtility.getArchiveFileContent(zipFile, fileHeader));
                updateProcessLog(processName, "      done");
                updateProcessLog(processName, "      load to database...");
                params.put("script", script);
                //Execute the script
                getRepository().getScalar(String.class, params);
                updateProcessLog(processName, "      done");
                setProcessProgress(processName, getProcessProgress(processName, false) + 2);
            }
            updateProcessLog(processName, "done");
            updateProcessLog(processName, "Validating consolidation schema against the other tables...");
            //It will check for consistancy before making the consolidation
            List<ValidationResult> validationResultList = validateBeforeConsolidation(languageCode);
            setProcessProgress(processName,
                    getProcessProgress(processName, false) + (validationResultList.size() * 2));

            for (ValidationResult vr : validationResultList) {
                String brResult = String.format("    BR:%s    Severity:%s    Passed:%s    Feedback:%s",
                        vr.getName(), vr.getSeverity(), vr.isSuccessful(), vr.getFeedback());
                updateProcessLog(processName, brResult);
            }
            setProcessProgress(processName, getProcessProgress(processName, false) + 2);

            if (!systemEJB.validationSucceeded(validationResultList)) {
                //If the validation fails the whole transaction is rolledback.
                throw new RuntimeException("Validation failed.");
            } else {
                updateProcessLog(processName, "Validation finished with success.");
            }

            //Initiate the consolidation from consolidation schema to main tables
            params.clear();
            params.put(CommonSqlProvider.PARAM_QUERY, sqlStatementConsolidate);
            params.put("current_user", getUser(getUserName()).getId());
            params.put("process_name", processName);
            getRepository().getScalar(String.class, params);
        } catch (Exception ex) {
            updateProcessLog(processName, String.format("%s\r\n%s",
                    MessageUtility.getLocalizedMessage(ServiceMessage.ADMIN_WS_CONSOLIDATION_FAILED).getMessage(),
                    ex.getMessage()));
            throw new SOLAException(ServiceMessage.ADMIN_WS_CONSOLIDATION_FAILED, ex);
        }
    }
    private List<ValidationResult> validateBeforeConsolidation(String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForConsolidation();
        List<ValidationResult> validationResultList
                = this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, null);
        return validationResultList;
	}

//    /**
//     * Restores user password by generating activation code and sending a link
//     * to the user for changing the password.
//     *
//     * @param email User's email
//     */
//    @Override
//    public void restoreUserPassword(String email) {
//        User user = getUserByEmail(email);
//        if (user == null || StringUtility.isEmpty(user.getEmail()) || !user.isActive()) {
//            return;
//        }
//
//        String code = UUID.randomUUID().toString();
//
//        user.setActivationCode(code);
//        user.setEntityAction(EntityAction.UPDATE);
//        getRepository().saveEntity(user);
//
//        // Send email
//        if (systemEJB.isEmailServiceEnabled() && !StringUtility.isEmpty(user.getEmail())) {
//            String msgBody = systemEJB.getSetting(ConfigConstants.EMAIL_MSG_PASSWD_RESTORE_BODY, "");
//            String msgSubject = systemEJB.getSetting(ConfigConstants.EMAIL_MSG_PASSWD_RESTORE_SUBJECT, "");
//            String restoreUrl = StringUtility.empty(LocalInfo.getBaseUrl()) + "/user/pwdrestore.xhtml?code=" + code;
//
//            msgBody = msgBody.replace(EmailVariables.FULL_USER_NAME, user.getFirstName());
//            msgBody = msgBody.replace(EmailVariables.PASSWORD_RESTORE_LINK, restoreUrl);
//
//            systemEJB.sendEmail(user.getFullName(), user.getEmail(), msgBody, msgSubject);
//        }
//    }
//
//    /**
//     * Returns user by activation code
//     *
//     * @param activationCode Activation code
//     * @return
//     */
//    @Override
//    public User getUserByActivationCode(String activationCode) {
//        Map params = new HashMap<String, Object>();
//        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_ACTIVATION_CODE);
//        params.put(User.PARAM_ACTIVATION_CODE, activationCode);
//        return getRepository().getEntity(User.class, params);
//    }

    /**
     * It initializes a new process progress.
     *
     * @param processName Process name
     * @param maximumValue The maximum value the progress can get
     */
    @Override
    public void startProcessProgress(String processName, int maximumValue) {
        String sqlStatement = "select system.process_progress_start(#{process_name}, #{maximum_value}) as vl";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        params.put("maximum_value", maximumValue);
        getRepository().getScalar(Void.class, params);
    }

    /**
     * It initializes a new process progress.
     *
     * @param processName Process name
     * @param brNameToGenerateMaximumValue The name of the BR that will generate
     * the maximum value the progress can get
     */
    @Override
    public void startProcessProgressUsingBr(String processName, String brNameToGenerateMaximumValue) {
        Result brResult = systemEJB.checkRuleGetResultSingle(brNameToGenerateMaximumValue, null);
        Integer maximumValue = 100;
        if (brResult.getValue() != null) {
            maximumValue = Integer.parseInt(brResult.getValue().toString());
        }
        startProcessProgress(processName, maximumValue);
    }

    /**
     * Returns the progress of a certain process.
     *
     * @param processName process name
     * @param inPercentage True - the value in percentage, otherwise the
     * absolute value
     * @return
     */
    @Override
    public int getProcessProgress(String processName, boolean inPercentage) {
        String sqlStatement = "select system.process_progress_get(#{process_name}) as vl";
        if (inPercentage) {
            sqlStatement = "select system.process_progress_get_in_percentage(#{process_name}) as vl";
        }
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        return getRepository().getScalar(Integer.class, params);
    }

    /**
     * Sets the progress in absolute value of the progress of a process.
     *
     * @param processName process name
     * @param progressValue progress value
     */
    @Override
    public void setProcessProgress(String processName, int progressValue) {
        String sqlStatement = "select system.process_progress_set(#{process_name}, #{progress_value}) as vl";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        params.put("progress_value", progressValue);
        getRepository().getScalar(Void.class, params);
    }

    @Override
    public String getProcessLog(String processName) {
        String sqlStatement = "select system.process_log_get(#{process_name}) as vl";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        return getRepository().getScalar(String.class, params);
    }

    /**
     * Updates the log of the process.
     *
     * @param processName Process name
     * @param logInput log input
     */
    @Override
    public void updateProcessLog(String processName, String logInput) {
        String sqlStatement = "select system.process_log_update(#{process_name}, #{log_input}) as vl";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        params.put("log_input", logInput);
        getRepository().getScalar(String.class, params);
    }

    /**
     * Starts the log of the process.
     *
     * @param processName Process name
     */
    @Override
    public void startProcessLog(String processName) {
        String sqlStatement = "select system.process_log_start(#{process_name}) as vl";
        Map params = new HashMap();
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        params.put("process_name", processName);
        getRepository().getScalar(String.class, params);
    }
}

