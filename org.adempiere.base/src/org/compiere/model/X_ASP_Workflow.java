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

/** Generated Model for ASP_Workflow
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ASP_Workflow")
public class X_ASP_Workflow extends PO implements I_ASP_Workflow, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241222L;

    /** Standard Constructor */
    public X_ASP_Workflow (Properties ctx, int ASP_Workflow_ID, String trxName)
    {
      super (ctx, ASP_Workflow_ID, trxName);
      /** if (ASP_Workflow_ID == 0)
        {
			setAD_Workflow_ID (0);
			setASP_Level_ID (0);
			setASP_Status (null);
// S
        } */
    }

    /** Standard Constructor */
    public X_ASP_Workflow (Properties ctx, int ASP_Workflow_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ASP_Workflow_ID, trxName, virtualColumns);
      /** if (ASP_Workflow_ID == 0)
        {
			setAD_Workflow_ID (0);
			setASP_Level_ID (0);
			setASP_Status (null);
// S
        } */
    }

    /** Standard Constructor */
    public X_ASP_Workflow (Properties ctx, String ASP_Workflow_UU, String trxName)
    {
      super (ctx, ASP_Workflow_UU, trxName);
      /** if (ASP_Workflow_UU == null)
        {
			setAD_Workflow_ID (0);
			setASP_Level_ID (0);
			setASP_Status (null);
// S
        } */
    }

    /** Standard Constructor */
    public X_ASP_Workflow (Properties ctx, String ASP_Workflow_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ASP_Workflow_UU, trxName, virtualColumns);
      /** if (ASP_Workflow_UU == null)
        {
			setAD_Workflow_ID (0);
			setASP_Level_ID (0);
			setASP_Status (null);
// S
        } */
    }

    /** Load Constructor */
    public X_ASP_Workflow (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System
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
      StringBuilder sb = new StringBuilder ("X_ASP_Workflow[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Workflow getAD_Workflow() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Workflow)MTable.get(getCtx(), org.compiere.model.I_AD_Workflow.Table_ID)
			.getPO(getAD_Workflow_ID(), get_TrxName());
	}

	/** Set Workflow.
		@param AD_Workflow_ID Workflow or combination of tasks
	*/
	public void setAD_Workflow_ID (int AD_Workflow_ID)
	{
		if (AD_Workflow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_Workflow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_Workflow_ID, Integer.valueOf(AD_Workflow_ID));
	}

	/** Get Workflow.
		@return Workflow or combination of tasks
	  */
	public int getAD_Workflow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Workflow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_ASP_Level getASP_Level() throws RuntimeException
	{
		return (org.compiere.model.I_ASP_Level)MTable.get(getCtx(), org.compiere.model.I_ASP_Level.Table_ID)
			.getPO(getASP_Level_ID(), get_TrxName());
	}

	/** Set ASP Level.
		@param ASP_Level_ID ASP Level
	*/
	public void setASP_Level_ID (int ASP_Level_ID)
	{
		if (ASP_Level_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ASP_Level_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ASP_Level_ID, Integer.valueOf(ASP_Level_ID));
	}

	/** Get ASP Level.
		@return ASP Level	  */
	public int getASP_Level_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ASP_Level_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** ASP_Status AD_Reference_ID=53234 */
	public static final int ASP_STATUS_AD_Reference_ID=53234;
	/** Hide = H */
	public static final String ASP_STATUS_Hide = "H";
	/** Show = S */
	public static final String ASP_STATUS_Show = "S";
	/** Undefined = U */
	public static final String ASP_STATUS_Undefined = "U";
	/** Set ASP Status.
		@param ASP_Status ASP Status
	*/
	public void setASP_Status (String ASP_Status)
	{

		set_Value (COLUMNNAME_ASP_Status, ASP_Status);
	}

	/** Get ASP Status.
		@return ASP Status	  */
	public String getASP_Status()
	{
		return (String)get_Value(COLUMNNAME_ASP_Status);
	}

	/** Set ASP Workflow.
		@param ASP_Workflow_ID ASP Workflow
	*/
	public void setASP_Workflow_ID (int ASP_Workflow_ID)
	{
		if (ASP_Workflow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ASP_Workflow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ASP_Workflow_ID, Integer.valueOf(ASP_Workflow_ID));
	}

	/** Get ASP Workflow.
		@return ASP Workflow	  */
	public int getASP_Workflow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ASP_Workflow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ASP_Workflow_UU.
		@param ASP_Workflow_UU ASP_Workflow_UU
	*/
	public void setASP_Workflow_UU (String ASP_Workflow_UU)
	{
		set_Value (COLUMNNAME_ASP_Workflow_UU, ASP_Workflow_UU);
	}

	/** Get ASP_Workflow_UU.
		@return ASP_Workflow_UU	  */
	public String getASP_Workflow_UU()
	{
		return (String)get_Value(COLUMNNAME_ASP_Workflow_UU);
	}
}