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
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for C_BP_Vendor_Acct
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="C_BP_Vendor_Acct")
public class X_C_BP_Vendor_Acct extends PO implements I_C_BP_Vendor_Acct, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241222L;

    /** Standard Constructor */
    public X_C_BP_Vendor_Acct (Properties ctx, int C_BP_Vendor_Acct_ID, String trxName)
    {
      super (ctx, C_BP_Vendor_Acct_ID, trxName);
      /** if (C_BP_Vendor_Acct_ID == 0)
        {
			setC_AcctSchema_ID (0);
			setC_BPartner_ID (0);
			setV_Liability_Acct (0);
			setV_Prepayment_Acct (0);
        } */
    }

    /** Standard Constructor */
    public X_C_BP_Vendor_Acct (Properties ctx, int C_BP_Vendor_Acct_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, C_BP_Vendor_Acct_ID, trxName, virtualColumns);
      /** if (C_BP_Vendor_Acct_ID == 0)
        {
			setC_AcctSchema_ID (0);
			setC_BPartner_ID (0);
			setV_Liability_Acct (0);
			setV_Prepayment_Acct (0);
        } */
    }

    /** Standard Constructor */
    public X_C_BP_Vendor_Acct (Properties ctx, String C_BP_Vendor_Acct_UU, String trxName)
    {
      super (ctx, C_BP_Vendor_Acct_UU, trxName);
      /** if (C_BP_Vendor_Acct_UU == null)
        {
			setC_AcctSchema_ID (0);
			setC_BPartner_ID (0);
			setV_Liability_Acct (0);
			setV_Prepayment_Acct (0);
        } */
    }

    /** Standard Constructor */
    public X_C_BP_Vendor_Acct (Properties ctx, String C_BP_Vendor_Acct_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, C_BP_Vendor_Acct_UU, trxName, virtualColumns);
      /** if (C_BP_Vendor_Acct_UU == null)
        {
			setC_AcctSchema_ID (0);
			setC_BPartner_ID (0);
			setV_Liability_Acct (0);
			setV_Prepayment_Acct (0);
        } */
    }

    /** Load Constructor */
    public X_C_BP_Vendor_Acct (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_C_BP_Vendor_Acct[")
        .append(get_UUID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException
	{
		return (org.compiere.model.I_C_AcctSchema)MTable.get(getCtx(), org.compiere.model.I_C_AcctSchema.Table_ID)
			.getPO(getC_AcctSchema_ID(), get_TrxName());
	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID Rules for accounting
	*/
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AcctSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_BP_Vendor_Acct_UU.
		@param C_BP_Vendor_Acct_UU C_BP_Vendor_Acct_UU
	*/
	public void setC_BP_Vendor_Acct_UU (String C_BP_Vendor_Acct_UU)
	{
		set_Value (COLUMNNAME_C_BP_Vendor_Acct_UU, C_BP_Vendor_Acct_UU);
	}

	/** Get C_BP_Vendor_Acct_UU.
		@return C_BP_Vendor_Acct_UU	  */
	public String getC_BP_Vendor_Acct_UU()
	{
		return (String)get_Value(COLUMNNAME_C_BP_Vendor_Acct_UU);
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner.
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Liability_A() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getV_Liability_Acct(), get_TrxName());
	}

	/** Set Vendor Liability.
		@param V_Liability_Acct Account for Vendor Liability
	*/
	public void setV_Liability_Acct (int V_Liability_Acct)
	{
		set_Value (COLUMNNAME_V_Liability_Acct, Integer.valueOf(V_Liability_Acct));
	}

	/** Get Vendor Liability.
		@return Account for Vendor Liability
	  */
	public int getV_Liability_Acct()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Liability_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Liability_Services_A() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getV_Liability_Services_Acct(), get_TrxName());
	}

	/** Set Vendor Service Liability.
		@param V_Liability_Services_Acct Account for Vendor Service Liability
	*/
	public void setV_Liability_Services_Acct (int V_Liability_Services_Acct)
	{
		set_Value (COLUMNNAME_V_Liability_Services_Acct, Integer.valueOf(V_Liability_Services_Acct));
	}

	/** Get Vendor Service Liability.
		@return Account for Vendor Service Liability
	  */
	public int getV_Liability_Services_Acct()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Liability_Services_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Prepayment_A() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getV_Prepayment_Acct(), get_TrxName());
	}

	/** Set Vendor Prepayment.
		@param V_Prepayment_Acct Account for Vendor Prepayments
	*/
	public void setV_Prepayment_Acct (int V_Prepayment_Acct)
	{
		set_Value (COLUMNNAME_V_Prepayment_Acct, Integer.valueOf(V_Prepayment_Acct));
	}

	/** Get Vendor Prepayment.
		@return Account for Vendor Prepayments
	  */
	public int getV_Prepayment_Acct()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Prepayment_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}