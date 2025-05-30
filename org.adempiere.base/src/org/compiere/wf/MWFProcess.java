/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.wf;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MRole;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_WF_Process;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfo;
import org.compiere.process.StateEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

/**
 *	Extended Workflow Process model for AD_WF_Process
 *	
 *  @author Jorg Janke
 *  @author Silvano Trinchero, www.freepath.it
 *  			<li>IDEMPIERE-3209 changed fucntions to public to improve integration support
 *  @version $Id: MWFProcess.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MWFProcess extends X_AD_WF_Process
{
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 5981488658756275526L;

    /**
     * UUID based Constructor
     * @param ctx  Context
     * @param AD_WF_Process_UU  UUID key
     * @param trxName Transaction
     */
    public MWFProcess(Properties ctx, String AD_WF_Process_UU, String trxName) {
        super(ctx, AD_WF_Process_UU, trxName);
		if (Util.isEmpty(AD_WF_Process_UU))
			throw new IllegalArgumentException ("Cannot create new WF Process directly");
		m_state = new StateEngine (getWFState());
    }

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 *	@param trxName transaction
	 */
	public MWFProcess (Properties ctx, int AD_WF_Process_ID, String trxName)
	{
		super (ctx, AD_WF_Process_ID, trxName);
		if (AD_WF_Process_ID == 0)
			throw new IllegalArgumentException ("Cannot create new WF Process directly");
		m_state = new StateEngine (getWFState());
	}	//	MWFProcess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MWFProcess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_state = new StateEngine (getWFState());
	}	//	MWFProcess
	
	/**
	 * 	New Constructor
	 *	@param wf workflow
	 *	@param pi Process Info (Record_ID)
	 *  @deprecated
	 *	@throws Exception
	 */
	@Deprecated
	public MWFProcess (MWorkflow wf, ProcessInfo pi) throws Exception
	{
		this(wf, pi, wf.get_TrxName());
	}
	
	/**
	 * 	New Constructor
	 *	@param wf workflow
	 *	@param pi Process Info (Record_ID)
	 *  @param trxName 
	 *	@throws Exception
	 */
	public MWFProcess (MWorkflow wf, ProcessInfo pi, String trxName) throws Exception
	{
		super (wf.getCtx(), 0, trxName);
		if (!TimeUtil.isValid(wf.getValidFrom(), wf.getValidTo()))
			throw new IllegalStateException("Workflow not valid");
		m_wf = wf;
		m_pi = pi;	
		setAD_Workflow_ID (wf.getAD_Workflow_ID());
		setPriority(wf.getPriority());
		super.setWFState (WFSTATE_NotStarted);
		
		//	Document
		setAD_Table_ID(wf.getAD_Table_ID());
		setRecord_ID(pi.getRecord_ID());
		if (pi.getPO() != null)
			m_po = pi.getPO();
		if (getPO() == null)
		{
			setTextMsg("No PO with ID=" + pi.getRecord_ID());
			addTextMsg(new Exception(""));
			super.setWFState (WFSTATE_Terminated);
		}
		else
			setTextMsg(getPO());
		//	Responsible/User
		if (wf.getAD_WF_Responsible_ID() == 0)
			setAD_WF_Responsible_ID();
		else
			setAD_WF_Responsible_ID(wf.getAD_WF_Responsible_ID());
		setUser_ID(pi.getAD_User_ID());		//	user starting
		//
		m_state = new StateEngine (getWFState());
		setProcessed (false);
		//	Lock Entity
		getPO();
		setAD_Org_ID(m_po.getAD_Org_ID());//Add by Hideaki Hagiwara
	}	//	MWFProcess

	/**	State Machine				*/
	private StateEngine			m_state = null;
	/**	Activities					*/
	private MWFActivity[] 		m_activities = null;
	/**	Workflow					*/
	private MWorkflow			m_wf = null;
	/**	Process Info				*/
	private ProcessInfo			m_pi = null;
	/**	Persistent Object			*/
	private PO					m_po = null;
	/** Message from Activity		*/
	private String				m_processMsg = null;
	
	/**
	 * 	Get active Activities of Process
	 *	@param requery true to reload from DB
	 *	@param onlyActive only active activities
	 *	@return array of activities
	 */
	public MWFActivity[] getActivities (boolean requery, boolean onlyActive)
	{
		return getActivities(requery, onlyActive, get_TrxName());
	}
	
	/**
	 * 	Get active Activities of Process
	 *	@param requery true to reload from DB
	 *	@param onlyActive only active activities
	 *	@return array of activities
	 */
	public MWFActivity[] getActivities (boolean requery, boolean onlyActive, String trxName)
	{
		if (!requery && m_activities != null)
			return m_activities;
		//
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder whereClause = new StringBuilder("AD_WF_Process_ID=?");
		params.add(getAD_WF_Process_ID());
		if (onlyActive)
		{
			whereClause.append(" AND Processed=?");
			params.add(false);
		}
		List<MWFActivity> list = new Query(getCtx(), MWFActivity.Table_Name, whereClause.toString(), trxName)
								.setParameters(params)
								.setOrderBy(MWFActivity.COLUMNNAME_AD_WF_Activity_ID)
								.list();
		m_activities = new MWFActivity[list.size ()];
		list.toArray (m_activities);
		return m_activities;
	}	//	getActivities
	
	/**
	 * 	Get State
	 *	@return state
	 */
	public StateEngine getState()
	{
		return m_state;
	}	//	getState
	
	/**
	 * 	Get Action Options
	 *	@return array of valid actions
	 */
	public String[] getActionOptions()
	{
		return m_state.getActionOptions();
	}	//	getActionOptions
	
	/**
	 * 	Set Process State and update Actions
	 *	@param WFState
	 */
	public void setWFState (String WFState)
	{
		if (m_state == null)
			m_state = new StateEngine (getWFState());
		if (m_state.isClosed())
			return;
		if (getWFState().equals(WFState))
			return;
		//
		if (m_state.isValidNewState(WFState))
		{
			log.fine(WFState); 
			super.setWFState (WFState);
			m_state = new StateEngine (getWFState());
			if (m_state.isClosed())
				setProcessed(true);
			saveEx();
			//	Force close to all Activities
			if (m_state.isClosed())
			{
				MWFActivity[] activities = getActivities(true, true);	//	requery only active
				for (int i = 0; i < activities.length; i++)
				{
					if (!activities[i].isClosed())
					{
						activities[i].setTextMsg("Process:" + WFState);
						activities[i].setWFState(WFState);
					}
					if (!activities[i].isProcessed())
						activities[i].setProcessed(true);
					activities[i].saveEx();
				}
			}	//	closed
		}
		else	
			log.log(Level.SEVERE, "Ignored Invalid Transformation - New=" + WFState 
				+ ", Current=" + getWFState());
	}	//	setWFState

	/**
	 * 	Check Status of Activities.<br/>
	 * 	- start new activity
	 * 	@param trxName transaction
	 * 	@param lastPO PO
	 */
	public void checkActivities(String trxName, PO lastPO)
	{
		this.set_TrxName(trxName); // ensure process is working on the same transaction
		if (log.isLoggable(Level.INFO)) log.info("(" + getAD_Workflow_ID() + ") - " + getWFState() 
			+ (trxName == null ? "" : "[" + trxName + "]"));
		if (m_state.isClosed())
			return;
		
		if (lastPO != null && lastPO.get_ID() == this.getRecord_ID())
			m_po = lastPO;
		
		//
		MWFActivity[] activities = getActivities (true, true, trxName);	//	requery active
		String closedState = null;
		for (int i = 0; i < activities.length; i++)
		{
			MWFActivity activity = activities[i];
			StateEngine activityState = activity.getState(); 
			
			//	Completed - Start Next
			if (activityState.isCompleted())
			{
				if (startNext (activity, activities, lastPO, trxName))
					continue;		
			}
			//
			String activityWFState = activity.getWFState();
			if (activityState.isClosed())
			{
				//	eliminate from active processed
				activity.setProcessed(true);
				activity.saveEx();
				//
				if (closedState == null)
					closedState = activityWFState;
				else if (!closedState.equals(activityState.getState()))
				{
					//	Overwrite if terminated
					if (activityState.isTerminated())
						closedState = activityWFState;
					//	Overwrite if activity aborted and no other terminated
					else if (activityState.isAborted() && !WFSTATE_Terminated.equals(closedState))
						closedState = activityWFState;
				}
			}
			else	//	not closed
			{
				closedState = null;		//	all need to be closed
			}
		}	//	for all activities
		if (activities.length == 0)
		{
			setTextMsg("No Active Processed found");
			addTextMsg(new Exception(""));
			closedState = WFSTATE_Terminated;
		}
		if (closedState != null)
			getPO();
	}	//	checkActivities

	/**
	 * 	Update process status based on status of activities.
	 * 	@param trxName transaction
	 */
	public void checkCloseActivities(String trxName) {
		this.set_TrxName(trxName); // ensure process is working on the same transaction
		if (log.isLoggable(Level.INFO)) log.info("(" + getAD_Workflow_ID() + ") - " + getWFState() 
			+ (trxName == null ? "" : "[" + trxName + "]"));
		if (m_state.isClosed())
			return;

		//
		MWFActivity[] activities = getActivities (true, false, trxName);	//	requery active
		String closedState = null;
		boolean suspended = false;
		boolean running = false;
		for (int i = 0; i < activities.length; i++)
		{
			MWFActivity activity = activities[i];
			StateEngine activityState = activity.getState(); 
			//
			String activityWFState = activity.getWFState();
			if (activityState.isClosed())
			{
				//
				if (closedState == null)
					closedState = activityWFState;
				else if (!closedState.equals(activityState.getState()))
				{
					//	Overwrite if terminated
					if (activityState.isTerminated())
						closedState = activityWFState;
					//	Overwrite if activity aborted and no other terminated
					else if (activityState.isAborted() && !WFSTATE_Terminated.equals(closedState))
						closedState = activityWFState;
				}
			}
			else	//	not closed
			{
				closedState = null;		//	all need to be closed
				if (activityState.isSuspended())
					suspended = true;
				if (activityState.isRunning())
					running = true;
			}
		}	//	for all activities
		if (activities.length == 0)
		{
			setTextMsg("No Active Processed found");
			addTextMsg(new Exception(""));
			closedState = WFSTATE_Terminated;
		}
		if (closedState != null)
		{
			setWFState(closedState);
		}
		else if (suspended)
			setWFState(WFSTATE_Suspended);
		else if (running)
			setWFState(WFSTATE_Running);
		saveEx();
	}	//	checkCloseActivities

	/**
	 * 	Start Next Activity
	 *	@param last last activity
	 *	@param activities all activities
	 *  @param lastPO
	 *  @param trxName
	 *	@return true if there is a next activity
	 */
	private boolean startNext (MWFActivity last, MWFActivity[] activities, PO lastPO, String trxName)
	{
		if (log.isLoggable(Level.FINE)) log.fine("Last=" + last);
		//	transitions from the last processed node
		MWFNodeNext[] transitions = getWorkflow().getNodeNexts(
			last.getAD_WF_Node_ID(), last.getPO_AD_Client_ID());
		if (transitions == null || transitions.length == 0)
			return false;	//	done
		
		last.setProcessed(true);
		last.saveEx();

		//	Start next activity
		String split = last.getNode().getSplitElement();
		for (int i = 0; i < transitions.length; i++)
		{
			//	Is this a valid transition?
			if (!transitions[i].isValidFor(last))
				continue;
			
			//	Start new Activity...
			MWFActivity activity = new MWFActivity (this, transitions[i].getAD_WF_Next_ID(), lastPO);
			/**
			 * IDEMPIERE-3942
			 * Implement JoinElement AND Status
			 */
			if(MWFNode.JOINELEMENT_AND.equals(activity.getNode().getJoinElement()))
			{
				if(!isJoinElementAndProcessed(activity))
				{
					activity.delete(true, get_TrxName());
					continue;
				}
			}
			
			activity.set_TrxName(trxName);
			activity.run();
			
			//	only the first valid if XOR
			if (MWFNode.SPLITELEMENT_XOR.equals(split))
				return true;
		}	//	for all transitions
		return true;
	}	//	startNext

	/**
	 * IDEMPIERE-3942
	 * Implement JoinElement AND Status
	 * @param activity
	 * @return true if all parent activities processed
	 */	
	private boolean isJoinElementAndProcessed(MWFActivity activity) {
		Query queryNodeNext = new Query(Env.getCtx(), MWFNodeNext.Table_Name, "AD_WF_Next_ID = ?", get_TrxName());
		queryNodeNext.setParameters(activity.getAD_WF_Node_ID());
		List<MWFNodeNext> nodeNexts = queryNodeNext.list();
		/**
		 * IDEMPIERE-3942 #2 Transition need to match with Activity
		 */
		int totalParent = 0;
		int totalActivities = 0;
		for (MWFNodeNext nodeNext : nodeNexts) {
			totalParent++;
			Query queryMWFActivity = new Query(Env.getCtx(), MWFActivity.Table_Name,
					"AD_WF_Process_ID = ? AND AD_WF_Node_ID = ? ", get_TrxName());

			Object params[] = { activity.getAD_WF_Process_ID(), nodeNext.getAD_WF_Node_ID() };
			queryMWFActivity.setParameters(params);
			List<MWFActivity> parentActivities = queryMWFActivity.list();
			for (MWFActivity parentActivity : parentActivities) {
				totalActivities++;
				if(!parentActivity.isProcessed())
					return false;
			}
			
		}
		if(totalParent < totalActivities)
			return false;
		
		return true;
	}

	/**
	 * 	Set Workflow Responsible.
	 * 	Searches for a Invoker.
	 */
	public void setAD_WF_Responsible_ID ()
	{
		int AD_WF_Responsible_ID = DB.getSQLValueEx(null,
			MRole.getDefault(getCtx(), false).addAccessSQL(	
			"SELECT AD_WF_Responsible_ID FROM AD_WF_Responsible "
			+ "WHERE ResponsibleType='H' AND COALESCE(AD_User_ID,0)=0 "
			+ "ORDER BY AD_Client_ID DESC", 
			"AD_WF_Responsible", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO));
		setAD_WF_Responsible_ID (AD_WF_Responsible_ID);
	}	//	setAD_WF_Responsible_ID

	/**
	 * 	Set User from 
	 *  <ol>
	 * 	  <li>Responsible
	 *    <li>Document Sales Rep
	 *    <li>Document UpdatedBy
	 * 	  <li>Process invoker
	 *  </ol>
	 * 	@param User_ID process invoker
	 */
	private void setUser_ID (Integer User_ID)
	{
		//	Responsible
		MWFResponsible resp = MWFResponsible.get(getCtx(), getAD_WF_Responsible_ID());
		//	(1) User - Directly responsible
		int AD_User_ID = resp.getAD_User_ID();
		
		//	Invoker - get Sales Rep or last updater of Document
		if (AD_User_ID == 0 && resp.isInvoker())
		{
			getPO();
			//	(2) Doc Owner
			if (m_po != null && m_po instanceof DocAction)
			{
				DocAction da = (DocAction)m_po;
				AD_User_ID = da.getDoc_User_ID();
			}
			//	(2) Sales Rep
			if (AD_User_ID == 0 && m_po != null && m_po.get_ColumnIndex("SalesRep_ID") != -1)
			{
				Object sr = m_po.get_Value("SalesRep_ID");
				if (sr != null && sr instanceof Integer)
					AD_User_ID = ((Integer)sr).intValue();
			}
			//	(3) UpdatedBy
			if (AD_User_ID == 0 && m_po != null)
				AD_User_ID = m_po.getUpdatedBy();
		}
		
		//	(4) Process Owner
		if (AD_User_ID == 0 && User_ID != null)
			AD_User_ID = User_ID.intValue();
		//	Fallback 
		if (AD_User_ID == 0)
			AD_User_ID = Env.getAD_User_ID(getCtx());
		//
		setAD_User_ID(AD_User_ID);
	}	//	setUser_ID
	
	/**
	 * 	Get Workflow
	 *	@return workflow
	 */
	public MWorkflow getWorkflow()
	{
		if (m_wf == null)
			m_wf = MWorkflow.getCopy(getCtx(), getAD_Workflow_ID(), get_TrxName());
		if (m_wf.get_ID() == 0)
			throw new IllegalStateException("Not found - AD_Workflow_ID=" + getAD_Workflow_ID());
		return m_wf;
	}	//	getWorkflow
	
	/**
	 * 	Perform Action
	 *	@param action StateEngine.ACTION_*
	 *	@return true if valid
	 */
	public boolean perform (String action)
	{
		if (!m_state.isValidAction(action))
		{
			log.log(Level.SEVERE, "Ignored Invalid Transformation - Action=" + action 
				+ ", CurrentState=" + getWFState());
			return false;
		}
		if (log.isLoggable(Level.FINE)) log.fine(action); 
		//	Action is Valid
		if (StateEngine.ACTION_Start.equals(action))
			return startWork();
		//	Set new State
		setWFState (m_state.getNewStateIfAction(action));
		return true;
	}	//	perform
	
	/**
	 * 	Start WF Execution
	 *	@return true if success
	 */
	public boolean startWork()
	{
		if (!m_state.isValidAction(StateEngine.ACTION_Start))
		{
			log.warning("State=" + getWFState() + " - cannot start");
			return false;
		}
		int AD_WF_Node_ID = getWorkflow().getAD_WF_Node_ID();
		
		//Check configuration completeness
		if(Env.getAD_Client_ID(getCtx())>0) {
			MWFNode[] nodes = getWorkflow().getNodesInOrder(Env.getAD_Client_ID(getCtx()));
			StringBuilder sbMsg = null;
			int ind =1;
			for(MWFNode node:nodes) {
				if(node.getAD_WF_Responsible_ID()>0) {
					MWFResponsible resp = MWFResponsible.get(getCtx(), node.getAD_WF_Responsible_ID());
					if(resp.getAD_Client_ID()==0 && (MWFResponsible.RESPONSIBLETYPE_Role.equals(resp.getResponsibleType()) || (resp.isHuman() && !resp.isInvoker())))
					{
						MWFResponsible cResp = MWFResponsible.getClientWFResp(getCtx(), resp.getAD_WF_Responsible_ID());
						if(cResp==null) {
							if(sbMsg==null) {
								sbMsg = new StringBuilder().append(ind++).append(". ").append(resp.getName());
							}else
								sbMsg.append("\n").append(ind++).append(". ").append(resp.getName());
						}
					}
				}
			}
			if(sbMsg!=null)
				throw new AdempiereException(Msg.getMsg(getCtx(), "IncompeteWorkflowResponsible", new Object[] {sbMsg.toString()}));
		}

		if (log.isLoggable(Level.FINE)) log.fine("AD_WF_Node_ID=" + AD_WF_Node_ID);
		setWFState(WFSTATE_Running);
		try
		{
			//	Start first Activity with first Node
			MWFActivity activity = new MWFActivity (this, AD_WF_Node_ID);
			activity.run();

		}
		catch (Throwable e)
		{
			log.log(Level.SEVERE, "AD_WF_Node_ID=" + AD_WF_Node_ID, e);
			setTextMsg(e.toString());
			addTextMsg(e);
			setWFState(StateEngine.STATE_Terminated);
			return false;
		}
		return true;
	}	//	performStart
	
	/**
	 * 	Get Persistent Object
	 *	@return po
	 */
	public PO getPO()
	{
		if (m_po != null)
			return m_po;
		if (getRecord_ID() == 0)
			return null;
		
		MTable table = MTable.get (getCtx(), getAD_Table_ID());
		m_po = table.getPO(getRecord_ID(), get_TrxName());
		return m_po;
	}	//	getPO

	/**
	 * Get process info
	 * @return {@link ProcessInfo}
	 */
	public ProcessInfo getProcessInfo()
	{
		return m_pi;
	}
	
	/**
	 * 	Set Text Msg (add to existing)
	 *	@param po base object
	 */
	public void setTextMsg (PO po)
	{
		if (po != null && po instanceof DocAction)
			setTextMsg(((DocAction)po).getSummary());
	}	//	setTextMsg	

	/**
	 * 	Set Text Msg (add to existing)
	 *	@param TextMsg msg
	 */
	@Override
	public void setTextMsg (String TextMsg)
	{
		String oldText = getTextMsg();
		if (oldText == null || oldText.length() == 0)
			super.setTextMsg (TextMsg);
		else if (TextMsg != null && TextMsg.length() > 0)
			super.setTextMsg (oldText + "\n - " + TextMsg);
	}	//	setTextMsg	

	/**
	 * 	Add to Text Msg
	 *	@param obj some object
	 */
	public void addTextMsg (Object obj)
	{
		if (obj == null)
			return;
		//
		StringBuilder TextMsg = new StringBuilder ();
		if (obj instanceof Exception)
		{
			Exception ex = (Exception)obj;
			if (ex.getMessage() != null && ex.getMessage().trim().length() > 0)
			{
				TextMsg.append(ex.toString());
			}
			else if (ex instanceof NullPointerException)
			{
				TextMsg.append(ex.getClass().getName());
			}
			while (ex != null)
			{
				StackTraceElement[] st = ex.getStackTrace();
				for (int i = 0; i < st.length; i++)
				{
					StackTraceElement ste = st[i];
					if (i == 0 || ste.getClassName().startsWith("org.compiere") || ste.getClassName().startsWith("org.adempiere"))
						TextMsg.append(" (").append(i).append("): ")
							.append(ste.toString())
							.append("\n");
				}
				if (ex.getCause() instanceof Exception)
					ex = (Exception)ex.getCause();
				else
					ex = null;
			}
		}
		else
		{
			TextMsg.append(obj.toString());
		}
		//
		String oldText = getTextMsg();
		if (oldText == null || oldText.length() == 0)
			super.setTextMsg(Util.trimSize(TextMsg.toString(),1000));
		else if (TextMsg != null && TextMsg.length() > 0)
			super.setTextMsg(Util.trimSize(oldText + "\n - " + TextMsg.toString(),1000));
	}	//	addTextMsg
	
	/**
	 * 	Set Process Execution (Error) Message
	 *	@param msg message
	 */
	public void setProcessMsg (String msg)
	{
		m_processMsg = msg;
		if (msg != null && msg.length() > 0)
			setTextMsg(msg);
	}	//	setProcessMsg
	
	/**
	 * 	Get Process Execution (Error) Message
	 *	@return msg
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
}	//	MWFProcess
