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

/** Generated Interface for M_ShippingTransactionLine
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
public interface I_M_ShippingTransactionLine 
{

    /** TableName=M_ShippingTransactionLine */
    public static final String Table_Name = "M_ShippingTransactionLine";

    /** AD_Table_ID=200052 */
    public static final int Table_ID = 200052;

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

    /** Column name C_UOM_Length_ID */
    public static final String COLUMNNAME_C_UOM_Length_ID = "C_UOM_Length_ID";

	/** Set UOM for Length.
	  * Standard Unit of Measure for Length
	  */
	public void setC_UOM_Length_ID (int C_UOM_Length_ID);

	/** Get UOM for Length.
	  * Standard Unit of Measure for Length
	  */
	public int getC_UOM_Length_ID();

	public org.compiere.model.I_C_UOM getC_UOM_Length() throws RuntimeException;

    /** Column name C_UOM_Weight_ID */
    public static final String COLUMNNAME_C_UOM_Weight_ID = "C_UOM_Weight_ID";

	/** Set UOM for Weight.
	  * Standard Unit of Measure for Weight
	  */
	public void setC_UOM_Weight_ID (int C_UOM_Weight_ID);

	/** Get UOM for Weight.
	  * Standard Unit of Measure for Weight
	  */
	public int getC_UOM_Weight_ID();

	public org.compiere.model.I_C_UOM getC_UOM_Weight() throws RuntimeException;

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

    /** Column name Height */
    public static final String COLUMNNAME_Height = "Height";

	/** Set Height	  */
	public void setHeight (BigDecimal Height);

	/** Get Height	  */
	public BigDecimal getHeight();

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

    /** Column name Length */
    public static final String COLUMNNAME_Length = "Length";

	/** Set Length	  */
	public void setLength (BigDecimal Length);

	/** Get Length	  */
	public BigDecimal getLength();

    /** Column name M_PackageMPS_ID */
    public static final String COLUMNNAME_M_PackageMPS_ID = "M_PackageMPS_ID";

	/** Set Package MPS	  */
	public void setM_PackageMPS_ID (int M_PackageMPS_ID);

	/** Get Package MPS	  */
	public int getM_PackageMPS_ID();

	public org.compiere.model.I_M_PackageMPS getM_PackageMPS() throws RuntimeException;

    /** Column name M_ShippingTransactionLine_ID */
    public static final String COLUMNNAME_M_ShippingTransactionLine_ID = "M_ShippingTransactionLine_ID";

	/** Set Shipping Transaction Line	  */
	public void setM_ShippingTransactionLine_ID (int M_ShippingTransactionLine_ID);

	/** Get Shipping Transaction Line	  */
	public int getM_ShippingTransactionLine_ID();

    /** Column name M_ShippingTransactionLine_UU */
    public static final String COLUMNNAME_M_ShippingTransactionLine_UU = "M_ShippingTransactionLine_UU";

	/** Set M_ShippingTransactionLine_UU	  */
	public void setM_ShippingTransactionLine_UU (String M_ShippingTransactionLine_UU);

	/** Get M_ShippingTransactionLine_UU	  */
	public String getM_ShippingTransactionLine_UU();

    /** Column name M_ShippingTransaction_ID */
    public static final String COLUMNNAME_M_ShippingTransaction_ID = "M_ShippingTransaction_ID";

	/** Set Shipping Transaction	  */
	public void setM_ShippingTransaction_ID (int M_ShippingTransaction_ID);

	/** Get Shipping Transaction	  */
	public int getM_ShippingTransaction_ID();

	public org.compiere.model.I_M_ShippingTransaction getM_ShippingTransaction() throws RuntimeException;

    /** Column name MasterTrackingNo */
    public static final String COLUMNNAME_MasterTrackingNo = "MasterTrackingNo";

	/** Set Master Tracking No	  */
	public void setMasterTrackingNo (String MasterTrackingNo);

	/** Get Master Tracking No	  */
	public String getMasterTrackingNo();

    /** Column name Price */
    public static final String COLUMNNAME_Price = "Price";

	/** Set Price.
	  * Price
	  */
	public void setPrice (BigDecimal Price);

	/** Get Price.
	  * Price
	  */
	public BigDecimal getPrice();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name TrackingNo */
    public static final String COLUMNNAME_TrackingNo = "TrackingNo";

	/** Set Tracking No.
	  * Number to track the shipment
	  */
	public void setTrackingNo (String TrackingNo);

	/** Get Tracking No.
	  * Number to track the shipment
	  */
	public String getTrackingNo();

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

    /** Column name Weight */
    public static final String COLUMNNAME_Weight = "Weight";

	/** Set Weight.
	  * Weight of a product
	  */
	public void setWeight (BigDecimal Weight);

	/** Get Weight.
	  * Weight of a product
	  */
	public BigDecimal getWeight();

    /** Column name Width */
    public static final String COLUMNNAME_Width = "Width";

	/** Set Width	  */
	public void setWidth (BigDecimal Width);

	/** Get Width	  */
	public BigDecimal getWidth();
}
