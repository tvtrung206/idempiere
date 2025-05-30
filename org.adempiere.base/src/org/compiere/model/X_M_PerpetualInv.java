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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_PerpetualInv
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="M_PerpetualInv")
public class X_M_PerpetualInv extends PO implements I_M_PerpetualInv, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241222L;

    /** Standard Constructor */
    public X_M_PerpetualInv (Properties ctx, int M_PerpetualInv_ID, String trxName)
    {
      super (ctx, M_PerpetualInv_ID, trxName);
      /** if (M_PerpetualInv_ID == 0)
        {
			setCountHighMovement (false);
			setDateNextRun (new Timestamp( System.currentTimeMillis() ));
			setM_PerpetualInv_ID (0);
			setName (null);
			setNoInventoryCount (0);
// 1
			setNoProductCount (0);
// 1
			setNumberOfRuns (0);
// 1
        } */
    }

    /** Standard Constructor */
    public X_M_PerpetualInv (Properties ctx, int M_PerpetualInv_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, M_PerpetualInv_ID, trxName, virtualColumns);
      /** if (M_PerpetualInv_ID == 0)
        {
			setCountHighMovement (false);
			setDateNextRun (new Timestamp( System.currentTimeMillis() ));
			setM_PerpetualInv_ID (0);
			setName (null);
			setNoInventoryCount (0);
// 1
			setNoProductCount (0);
// 1
			setNumberOfRuns (0);
// 1
        } */
    }

    /** Standard Constructor */
    public X_M_PerpetualInv (Properties ctx, String M_PerpetualInv_UU, String trxName)
    {
      super (ctx, M_PerpetualInv_UU, trxName);
      /** if (M_PerpetualInv_UU == null)
        {
			setCountHighMovement (false);
			setDateNextRun (new Timestamp( System.currentTimeMillis() ));
			setM_PerpetualInv_ID (0);
			setName (null);
			setNoInventoryCount (0);
// 1
			setNoProductCount (0);
// 1
			setNumberOfRuns (0);
// 1
        } */
    }

    /** Standard Constructor */
    public X_M_PerpetualInv (Properties ctx, String M_PerpetualInv_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, M_PerpetualInv_UU, trxName, virtualColumns);
      /** if (M_PerpetualInv_UU == null)
        {
			setCountHighMovement (false);
			setDateNextRun (new Timestamp( System.currentTimeMillis() ));
			setM_PerpetualInv_ID (0);
			setName (null);
			setNoInventoryCount (0);
// 1
			setNoProductCount (0);
// 1
			setNumberOfRuns (0);
// 1
        } */
    }

    /** Load Constructor */
    public X_M_PerpetualInv (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_PerpetualInv[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Count high turnover items.
		@param CountHighMovement Count High Movement products
	*/
	public void setCountHighMovement (boolean CountHighMovement)
	{
		set_Value (COLUMNNAME_CountHighMovement, Boolean.valueOf(CountHighMovement));
	}

	/** Get Count high turnover items.
		@return Count High Movement products
	  */
	public boolean isCountHighMovement()
	{
		Object oo = get_Value(COLUMNNAME_CountHighMovement);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Date Last Run.
		@param DateLastRun Date the process was last run.
	*/
	public void setDateLastRun (Timestamp DateLastRun)
	{
		set_ValueNoCheck (COLUMNNAME_DateLastRun, DateLastRun);
	}

	/** Get Date Last Run.
		@return Date the process was last run.
	  */
	public Timestamp getDateLastRun()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastRun);
	}

	/** Set Date Next Run.
		@param DateNextRun Date the process will run next
	*/
	public void setDateNextRun (Timestamp DateNextRun)
	{
		set_ValueNoCheck (COLUMNNAME_DateNextRun, DateNextRun);
	}

	/** Get Date Next Run.
		@return Date the process will run next
	  */
	public Timestamp getDateNextRun()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateNextRun);
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Perpetual Inventory.
		@param M_PerpetualInv_ID Rules for generating physical inventory
	*/
	public void setM_PerpetualInv_ID (int M_PerpetualInv_ID)
	{
		if (M_PerpetualInv_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_PerpetualInv_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_PerpetualInv_ID, Integer.valueOf(M_PerpetualInv_ID));
	}

	/** Get Perpetual Inventory.
		@return Rules for generating physical inventory
	  */
	public int getM_PerpetualInv_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PerpetualInv_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_PerpetualInv_UU.
		@param M_PerpetualInv_UU M_PerpetualInv_UU
	*/
	public void setM_PerpetualInv_UU (String M_PerpetualInv_UU)
	{
		set_Value (COLUMNNAME_M_PerpetualInv_UU, M_PerpetualInv_UU);
	}

	/** Get M_PerpetualInv_UU.
		@return M_PerpetualInv_UU	  */
	public String getM_PerpetualInv_UU()
	{
		return (String)get_Value(COLUMNNAME_M_PerpetualInv_UU);
	}

	public org.compiere.model.I_M_Product_Category getM_Product_Category() throws RuntimeException
	{
		return (org.compiere.model.I_M_Product_Category)MTable.get(getCtx(), org.compiere.model.I_M_Product_Category.Table_ID)
			.getPO(getM_Product_Category_ID(), get_TrxName());
	}

	/** Set Product Category.
		@param M_Product_Category_ID Category of a Product
	*/
	public void setM_Product_Category_ID (int M_Product_Category_ID)
	{
		if (M_Product_Category_ID < 1)
			set_Value (COLUMNNAME_M_Product_Category_ID, null);
		else
			set_Value (COLUMNNAME_M_Product_Category_ID, Integer.valueOf(M_Product_Category_ID));
	}

	/** Get Product Category.
		@return Category of a Product
	  */
	public int getM_Product_Category_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Category_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException
	{
		return (org.compiere.model.I_M_Warehouse)MTable.get(getCtx(), org.compiere.model.I_M_Warehouse.Table_ID)
			.getPO(getM_Warehouse_ID(), get_TrxName());
	}

	/** Set Warehouse.
		@param M_Warehouse_ID Storage Warehouse and Service Point
	*/
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1)
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Number of Inventory counts.
		@param NoInventoryCount Frequency of inventory counts per year
	*/
	public void setNoInventoryCount (int NoInventoryCount)
	{
		set_Value (COLUMNNAME_NoInventoryCount, Integer.valueOf(NoInventoryCount));
	}

	/** Get Number of Inventory counts.
		@return Frequency of inventory counts per year
	  */
	public int getNoInventoryCount()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NoInventoryCount);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number of Product counts.
		@param NoProductCount Frequency of product counts per year
	*/
	public void setNoProductCount (int NoProductCount)
	{
		set_Value (COLUMNNAME_NoProductCount, Integer.valueOf(NoProductCount));
	}

	/** Get Number of Product counts.
		@return Frequency of product counts per year
	  */
	public int getNoProductCount()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NoProductCount);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number of runs.
		@param NumberOfRuns Frequency of processing Perpetual Inventory
	*/
	public void setNumberOfRuns (int NumberOfRuns)
	{
		set_Value (COLUMNNAME_NumberOfRuns, Integer.valueOf(NumberOfRuns));
	}

	/** Get Number of runs.
		@return Frequency of processing Perpetual Inventory
	  */
	public int getNumberOfRuns()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumberOfRuns);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Process Now.
		@param Processing Process Now
	*/
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing()
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}