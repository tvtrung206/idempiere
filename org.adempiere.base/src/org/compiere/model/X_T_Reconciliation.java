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

/** Generated Model for T_Reconciliation
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="T_Reconciliation")
public class X_T_Reconciliation extends PO implements I_T_Reconciliation, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241222L;

    /** Standard Constructor */
    public X_T_Reconciliation (Properties ctx, String T_Reconciliation_UU, String trxName)
    {
      super (ctx, T_Reconciliation_UU, trxName);
      /** if (T_Reconciliation_UU == null)
        {
			setAD_PInstance_ID (0);
			setFact_Acct_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_T_Reconciliation (Properties ctx, String T_Reconciliation_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, T_Reconciliation_UU, trxName, virtualColumns);
      /** if (T_Reconciliation_UU == null)
        {
			setAD_PInstance_ID (0);
			setFact_Acct_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_Reconciliation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_T_Reconciliation[")
        .append(get_UUID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_PInstance getAD_PInstance() throws RuntimeException
	{
		return (org.compiere.model.I_AD_PInstance)MTable.get(getCtx(), org.compiere.model.I_AD_PInstance.Table_ID)
			.getPO(getAD_PInstance_ID(), get_TrxName());
	}

	/** Set Process Instance.
		@param AD_PInstance_ID Instance of the process
	*/
	public void setAD_PInstance_ID (int AD_PInstance_ID)
	{
		if (AD_PInstance_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_PInstance_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_PInstance_ID, Integer.valueOf(AD_PInstance_ID));
	}

	/** Get Process Instance.
		@return Instance of the process
	  */
	public int getAD_PInstance_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Accounting Fact.
		@param Fact_Acct_ID Accounting Fact
	*/
	public void setFact_Acct_ID (int Fact_Acct_ID)
	{
		if (Fact_Acct_ID < 1)
			set_ValueNoCheck (COLUMNNAME_Fact_Acct_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_Fact_Acct_ID, Integer.valueOf(Fact_Acct_ID));
	}

	/** Get Accounting Fact.
		@return Accounting Fact	  */
	public int getFact_Acct_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Fact_Acct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Match Code.
		@param MatchCode String identifying related accounting facts
	*/
	public void setMatchCode (String MatchCode)
	{
		set_Value (COLUMNNAME_MatchCode, MatchCode);
	}

	/** Get Match Code.
		@return String identifying related accounting facts
	  */
	public String getMatchCode()
	{
		return (String)get_Value(COLUMNNAME_MatchCode);
	}

	/** Set T_Reconciliation_UU.
		@param T_Reconciliation_UU T_Reconciliation_UU
	*/
	public void setT_Reconciliation_UU (String T_Reconciliation_UU)
	{
		set_Value (COLUMNNAME_T_Reconciliation_UU, T_Reconciliation_UU);
	}

	/** Get T_Reconciliation_UU.
		@return T_Reconciliation_UU	  */
	public String getT_Reconciliation_UU()
	{
		return (String)get_Value(COLUMNNAME_T_Reconciliation_UU);
	}
}