/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_SalesStage
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
public interface I_C_SalesStage 
{

    /** TableName=C_SalesStage */
    public static final String Table_Name = "C_SalesStage";

    /** AD_Table_ID=53338 */
    public static final int Table_ID = 53338;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name C_SalesStage_ID */
    public static final String COLUMNNAME_C_SalesStage_ID = "C_SalesStage_ID";

	/** Set Sales Stage.
	  * Stages of the sales process
	  */
	public void setC_SalesStage_ID (int C_SalesStage_ID);

	/** Get Sales Stage.
	  * Stages of the sales process
	  */
	public int getC_SalesStage_ID();

    /** Column name C_SalesStage_UU */
    public static final String COLUMNNAME_C_SalesStage_UU = "C_SalesStage_UU";

	/** Set C_SalesStage_UU	  */
	public void setC_SalesStage_UU (String C_SalesStage_UU);

	/** Get C_SalesStage_UU	  */
	public String getC_SalesStage_UU();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsClosed */
    public static final String COLUMNNAME_IsClosed = "IsClosed";

	/** Set Closed Status.
	  * The status is closed
	  */
	public void setIsClosed (boolean IsClosed);

	/** Get Closed Status.
	  * The status is closed
	  */
	public boolean isClosed();

    /** Column name IsWon */
    public static final String COLUMNNAME_IsWon = "IsWon";

	/** Set Won.
	  * The opportunity was won
	  */
	public void setIsWon (boolean IsWon);

	/** Get Won.
	  * The opportunity was won
	  */
	public boolean isWon();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Probability */
    public static final String COLUMNNAME_Probability = "Probability";

	/** Set Probability	  */
	public void setProbability (BigDecimal Probability);

	/** Get Probability	  */
	public BigDecimal getProbability();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
