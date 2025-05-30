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
package org.adempiere.webui.window;

import static org.adempiere.webui.LayoutUtils.isLabelAboveInputForSmallWidth;
import static org.compiere.model.SystemIDs.WINDOW_ACCOUNTCOMBINATION;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.adwindow.ADTabpanel;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.ToolBar;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.StatusBarPanel;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.Icon;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.DataStatusListener;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.GridWindowVO;
import org.compiere.model.MAccount;
import org.compiere.model.MAccountLookup;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MQuery;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;

/**
 *	Dialog to enter Account Info
 *
 * 	@author Low Heng Sin
 */
public final class WAccountDialog extends Window
	implements EventListener<Event>, DataStatusListener, ValueChangeListener
{
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 3041802296879719489L;

	private Callback<Integer> m_callback;
	/* SysConfig USE_ESC_FOR_TAB_CLOSING */
	private boolean isUseEscForTabClosing = MSysConfig.getBooleanValue(MSysConfig.USE_ESC_FOR_TAB_CLOSING, false, Env.getAD_Client_ID(Env.getCtx()));
	
	/**
	 * 	Constructor
	 *  @param title title
	 *  @param mAccount account info
	 *  @param C_AcctSchema_ID as
	 *  @param callback
	 */
	public WAccountDialog (String title,
		MAccountLookup mAccount, int C_AcctSchema_ID, Callback<Integer> callback)
	{
		super ();
		this.setTitle(title);
		if (!ThemeManager.isUseCSSForWindowSize())
		{
			ZKUpdateUtil.setWindowHeightX(this, 500);
			ZKUpdateUtil.setWindowWidthX(this, 750);
		}
		else
		{
			addCallback(AFTER_PAGE_ATTACHED, t-> {
				ZKUpdateUtil.setCSSHeight(this);
				ZKUpdateUtil.setCSSWidth(this);
				this.invalidate();
			});
		}

		if (log.isLoggable(Level.CONFIG)) log.config("C_AcctSchema_ID=" + C_AcctSchema_ID
			+ ", C_ValidCombination_ID=" + mAccount.C_ValidCombination_ID);
		m_mAccount = mAccount;
		m_C_AcctSchema_ID = C_AcctSchema_ID;
		m_callback = callback;
		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		try
		{
			init();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.toString());
		}
		if (initAccount())
			AEnv.showCenterScreen(this);
		else
			dispose();
	}	//	WAccountDialog

	/** Window No					*/
	private int					m_WindowNo;
	/**	Journal Entry				*
	private boolean				m_onlyNonDocControlled = false;
	/** Selection changed			*/
	protected boolean			m_changed = false;

	/** Accounting Schema           */
	private MAcctSchema	m_AcctSchema = null;
	/** MWindow for AccountCombination  */
	private GridWindow             m_mWindow = null;
	/** MTab for AccountCombination     */
	private GridTab                m_mTab = null;
	/** GridController                  */
	private ADTabpanel      m_adTabPanel = null;

	/** Account used                */
	private MAccountLookup		m_mAccount = null;
	/** Result                      */
	private int 				m_C_ValidCombination_ID;
	/** Acct Schema					*/
	private int					m_C_AcctSchema_ID = 0;
	/** Client                      */
	private int                 m_AD_Client_ID;
	/** Where clause for combination search */
	private MQuery				m_query;
	/** Current combination */
	private int IDvalue = 0;
	/**	Logger			*/
	private static final CLogger log = CLogger.getCLogger(WAccountDialog.class);

	//  Editors for Query
	private WEditor 			f_Alias, f_Combination,
		f_AD_Org_ID, f_Account_ID, f_SubAcct_ID,
		f_M_Product_ID, f_C_BPartner_ID, f_C_Campaign_ID, f_C_LocFrom_ID, f_C_LocTo_ID,
		f_C_Project_ID, f_C_SalesRegion_ID, f_AD_OrgTrx_ID, f_C_Activity_ID,
		f_User1_ID, f_User2_ID;
	//
	private Label f_Description = new Label ("");

	//private int					m_line = 0;
	private boolean				m_newRow = true;
	//
	@SuppressWarnings("unused")
	private Vbox panel = new Vbox();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private StatusBarPanel statusBar = new StatusBarPanel();
	private Hbox northPanel = new Hbox();
	private Div parameterPanel = new Div();
	private Grid parameterLayout = new Grid();
	private ToolBar toolBar = new ToolBar();
	private ToolBarButton bRefresh = new ToolBarButton();
	private ToolBarButton bSave = new ToolBarButton();
	private ToolBarButton bIgnore = new ToolBarButton();
	private Row m_row;
	private Rows m_rows;

	private boolean m_smallWidth;

	/**
	 *	Create components and layout dialog
	 *  <pre>
	 *  - north
	 *    - parameterPanel
	 *    - toolBar
	 *  - center
	 *    - adtabpanel
	 *  - south
	 *    - confirmPanel
	 *    - statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	protected void init() throws Exception
	{
		//
		ZKUpdateUtil.setHflex(parameterPanel, "min");
		toolBar.setOrient("vertical");
		toolBar.setStyle("border: none; padding: 5px");
		ZKUpdateUtil.setHflex(toolBar, "min");

		if (ThemeManager.isUseFontIconForImage())
			bSave.setIconSclass(Icon.getIconSclass(Icon.SAVE));
		else
			bSave.setImage(ThemeManager.getThemeResource("images/Save24.png"));
		bSave.setTooltiptext(Msg.getMsg(Env.getCtx(),"AccountNewUpdate"));
		bSave.addEventListener(Events.ON_CLICK, this);
		if (ThemeManager.isUseFontIconForImage())
			bRefresh.setIconSclass(Icon.getIconSclass(Icon.REFRESH));
		else
			bRefresh.setImage(ThemeManager.getThemeResource("images/Refresh24.png"));
		bRefresh.setTooltiptext(Msg.getMsg(Env.getCtx(),"Refresh"));
		bRefresh.addEventListener(Events.ON_CLICK, this);
		if (ThemeManager.isUseFontIconForImage())
			bIgnore.setIconSclass(Icon.getIconSclass(Icon.IGNORE));
		else
			bIgnore.setImage(ThemeManager.getThemeResource("images/Ignore24.png"));
		bIgnore.setTooltiptext(Msg.getMsg(Env.getCtx(),"Ignore"));
		bIgnore.addEventListener(Events.ON_CLICK, this);
		if (ThemeManager.isUseFontIconForImage())
		{
			LayoutUtils.addSclass("medium-toolbarbutton", bSave);
			LayoutUtils.addSclass("medium-toolbarbutton", bRefresh);
			LayoutUtils.addSclass("medium-toolbarbutton", bIgnore);
		}
		//
		toolBar.appendChild(bRefresh);
		toolBar.appendChild(bIgnore);
		toolBar.appendChild(bSave);
		//
		northPanel.appendChild(parameterPanel);
		northPanel.appendChild(toolBar);
		ZKUpdateUtil.setWidth(northPanel, "100%");

		m_adTabPanel = new ADTabpanel();

		Borderlayout layout = new Borderlayout();
		layout.setParent(this);
		ZKUpdateUtil.setHeight(layout, "100%");
		ZKUpdateUtil.setWidth(layout, "100%");
		layout.setStyle("background-color: transparent; position: relative;");

		North nRegion = new North();
		nRegion.setParent(layout);
		ZKUpdateUtil.setHflex(northPanel, "false");
		ZKUpdateUtil.setVflex(parameterPanel, "min");
		nRegion.appendChild(northPanel);
		nRegion.setStyle("border: none");
		nRegion.setCollapsible(true);
		nRegion.setSplittable(true);
		nRegion.setAutoscroll(true);
		nRegion.setTitle(Msg.getMsg(Env.getCtx(),"Parameter"));

		Center cRegion = new Center();
		cRegion.setParent(layout);
		ZKUpdateUtil.setHflex(m_adTabPanel, "true");
		ZKUpdateUtil.setVflex(m_adTabPanel, "true");
		cRegion.appendChild(m_adTabPanel);
		ZKUpdateUtil.setVflex(cRegion, "1");

		South sRegion = new South();
		sRegion.setParent(layout);
		Div div = new Div();
		div.appendChild(confirmPanel);
		confirmPanel.setStyle("margin-top: 5px; margin-bottom: 5px");
		div.appendChild(statusBar);
		sRegion.appendChild(div);
		sRegion.setStyle("background-color: transparent; border: none");
		ZKUpdateUtil.setVflex(sRegion, "min");
		ZKUpdateUtil.setVflex(div, "min");
		ZKUpdateUtil.setVflex(confirmPanel, "min");
		ZKUpdateUtil.setVflex(statusBar, "min");

		confirmPanel.addActionListener(Events.ON_CLICK, this);

		this.setBorder("normal");
		this.setClosable(false);

		this.setSizable(true);
		this.setMaximizable(true);
		this.setSclass("account-dialog");
		
		if (ClientInfo.isMobile()) {
			ClientInfo.onClientInfo(this, this::onClientInfo);
		}
		
		addEventListener(Events.ON_CANCEL, e -> onCancel());
	}	//	init

	/**
	 *  Load account (valid combination) details
	 *  @return true if initialized
	 */
	private boolean initAccount()
	{
		m_AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		//	Get AcctSchema Info
		m_AcctSchema = new MAcctSchema (Env.getCtx(), m_C_AcctSchema_ID, null);
		Env.setContext(Env.getCtx(), m_WindowNo, "C_AcctSchema_ID", m_C_AcctSchema_ID);

		//  Model
		int AD_Window_ID = WINDOW_ACCOUNTCOMBINATION;		//	Maintain Account Combinations
		GridWindowVO wVO = AEnv.getMWindowVO (m_WindowNo, AD_Window_ID, 0);
		if (wVO == null)
			return false;
		// Force window/tab to be read-only
		wVO.WindowType = GridWindowVO.WINDOWTYPE_QUERY;
		wVO.Tabs.get(0).IsReadOnly = true;
		m_mWindow = new GridWindow (wVO);
		m_mTab = m_mWindow.getTab(0);
		// Make sure is the tab is loaded - teo_sarca [ 1659124 ]
		if (!m_mTab.isLoadComplete())
			m_mWindow.initTab(0);

		//  ParameterPanel restrictions
		m_mTab.getField("Alias").setDisplayLength(15);
		m_mTab.getField("Combination").setDisplayLength(15);
		//  Grid restrictions
		m_mTab.getField("AD_Client_ID").setDisplayed(false);
		m_mTab.getField("C_AcctSchema_ID").setDisplayed(false);
		m_mTab.getField("IsActive").setDisplayed(false);
		m_mTab.getField("IsFullyQualified").setDisplayed(false);
		//  don't show fields not being displayed in this environment
		for (int i = 0; i < m_mTab.getFieldCount(); i++)
		{
			GridField field = m_mTab.getField(i);
			if (!field.isDisplayed (true))      //  check context
				field.setDisplayed (false);
		}

		//  GridController
		m_adTabPanel.init(null, m_mTab);

		//  Prepare Parameter
		parameterLayout.makeNoStrip();
		parameterLayout.setOddRowSclass("even");
		parameterLayout.setParent(parameterPanel);
		parameterLayout.setStyle("background-color: transparent; margin:none; border:none; padding:none;");

		layoutParameters();

		//	Finish
		m_query = new MQuery();
		m_query.addRestriction("C_AcctSchema_ID", MQuery.EQUAL, m_C_AcctSchema_ID);
		if (m_mAccount.C_ValidCombination_ID == 0)
			m_mTab.setQuery(MQuery.getEqualQuery("1", "2"));
		else
		{
			MQuery query = new MQuery();
			query.addRestriction("C_AcctSchema_ID", MQuery.EQUAL, m_C_AcctSchema_ID);
			query.addRestriction("C_ValidCombination_ID", MQuery.EQUAL, m_mAccount.C_ValidCombination_ID);
			m_mTab.setQuery(query);
		}
		m_mTab.query(false);
		m_adTabPanel.getGridTab().addDataStatusListener(this);
		m_adTabPanel.activate(true);
		if (!m_adTabPanel.isGridView())
			m_adTabPanel.switchRowPresentation();

		statusBar.setStatusLine(m_AcctSchema.toString());
		statusBar.setStatusDB("");

		//	Initial value
		if (m_mAccount.C_ValidCombination_ID != 0) {
			m_mTab.navigate(0);
			if (f_Account_ID.getValue() instanceof Integer) {
				Env.setContext(Env.getCtx(), m_WindowNo, "Account_ID", (Integer)f_Account_ID.getValue());
				Env.setContext(Env.getCtx(), m_WindowNo, 0, "Account_ID", (Integer)f_Account_ID.getValue());
				if (f_SubAcct_ID != null) {
					f_SubAcct_ID.dynamicDisplay();
				}
			}
		}

		//auto collapse parameter region
		if (isAutoCollapseParameterPane() && northPanel.getParent() instanceof North northRegion)
			northRegion.setOpen(false);

		return true;
	}	//	initAccount

	/**
	 * Layout parameter panel
	 */
	protected void layoutParameters() {
		m_smallWidth = ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1);
		
		m_rows = new Rows();
		m_rows.setParent(parameterLayout);
		if (isLabelAboveInputForSmallWidth())
			LayoutUtils.addSclass("form-label-above-input", parameterLayout);

		//	Alias
		if (m_AcctSchema.isHasAlias())
		{
			GridField alias = m_mTab.getField("Alias");
			if (f_Alias == null)
				f_Alias = WebEditorFactory.getEditor(alias, false);
			addLine(alias, f_Alias, false);
		}	//	Alias

		//	Combination
		m_newRow = isLabelAboveInputForSmallWidth();
		GridField combination = m_mTab.getField("Combination");
		if (f_Combination == null)
			f_Combination = WebEditorFactory.getEditor(combination, false);
		addLine(combination, f_Combination, false);
		m_newRow = true;

		/**
		 *	Create Fields in Element Order
		 */
		MAcctSchemaElement[] elements = m_AcctSchema.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			if (isLabelAboveInputForSmallWidth())
				m_newRow = true;
			MAcctSchemaElement ase = elements[i];
			String type = ase.getElementType();
			boolean isMandatory = ase.isMandatory();
			//
			if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Organization))
			{
				GridField field = m_mTab.getField("AD_Org_ID");
				if (f_AD_Org_ID == null)
					f_AD_Org_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_AD_Org_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
			{
				GridField field = m_mTab.getField("Account_ID");
				if (f_Account_ID == null)
				{
					f_Account_ID = WebEditorFactory.getEditor(field, false);
					f_Account_ID.addValueChangeListener(this);
				}
				addLine(field, f_Account_ID, isMandatory);				
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_SubAccount))
			{
				GridField field = m_mTab.getField("C_SubAcct_ID");
				if (f_SubAcct_ID == null)
					f_SubAcct_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_SubAcct_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Product))
			{
				GridField field = m_mTab.getField("M_Product_ID");
				if (f_M_Product_ID == null)
					f_M_Product_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_M_Product_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_BPartner))
			{
				GridField field = m_mTab.getField("C_BPartner_ID");
				if (f_C_BPartner_ID == null)
					f_C_BPartner_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_BPartner_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Campaign))
			{
				GridField field = m_mTab.getField("C_Campaign_ID");
				if (f_C_Campaign_ID == null)
					f_C_Campaign_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_Campaign_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_LocationFrom))
			{
				GridField field = m_mTab.getField("C_LocFrom_ID");
				if (f_C_LocFrom_ID == null)
					f_C_LocFrom_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_LocFrom_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_LocationTo))
			{
				GridField field = m_mTab.getField("C_LocTo_ID");
				if (f_C_LocTo_ID == null)
					f_C_LocTo_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_LocTo_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Project))
			{
				GridField field = m_mTab.getField("C_Project_ID");
				if (f_C_Project_ID == null)
					f_C_Project_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_Project_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_SalesRegion))
			{
				GridField field = m_mTab.getField("C_SalesRegion_ID");
				if (f_C_SalesRegion_ID == null)
					f_C_SalesRegion_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_SalesRegion_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_OrgTrx))
			{
				GridField field = m_mTab.getField("AD_OrgTrx_ID");
				if (f_AD_OrgTrx_ID == null)
					f_AD_OrgTrx_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_AD_OrgTrx_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Activity))
			{
				GridField field = m_mTab.getField("C_Activity_ID");
				if (f_C_Activity_ID == null)
					f_C_Activity_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_C_Activity_ID, isMandatory);
			}
			//	User1
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList1))
			{
				GridField field = m_mTab.getField("User1_ID");
				if (f_User1_ID == null)
					f_User1_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_User1_ID, isMandatory);
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList2))
			{
				GridField field = m_mTab.getField("User2_ID");
				if (f_User2_ID == null)
					f_User2_ID = WebEditorFactory.getEditor(field, false);
				addLine(field, f_User2_ID, isMandatory);
			}
		}	//	Create Fields in Element Order

		//	Add description
		m_newRow = true;
		Row row = new Row();
		f_Description.setStyle("font-decoration: italic;");
		Cell cell = new Cell();
		cell.setColspan(4);
		cell.appendChild(f_Description);
		row.appendChild(cell);
		row.setStyle("background-color: transparent; padding: 10px");
		m_rows.appendChild(row);
	}

	/**
	 *	Add Editor to parameterPanel alternate right/left depending on m_newRow.<br/>
	 *  Editor will listen to value change event of field.
	 *  @param field field
	 *  @param editor editor
	 *  @param mandatory mandatory
	 */
	private void addLine (GridField field, WEditor editor, boolean mandatory)
	{
		if (log.isLoggable(Level.FINE)) log.fine("Field=" + field);
		Label label = editor.getLabel();
		editor.setReadWrite(true);
		editor.setMandatory(mandatory);
		//  MField => VEditor
		field.addPropertyChangeListener(editor);

		//	label
		if (m_newRow)
		{
			m_row = new Row();
			m_row.setStyle("background-color: transparent");
			m_rows.appendChild(m_row);
		}

		if (ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1))
		{
			Vlayout vlayout = new Vlayout();
			vlayout.setHflex("1");
			vlayout.setSpacing("0px");
			vlayout.appendChild(label);
			vlayout.appendChild(editor.getComponent());
			m_row.appendCellChild(vlayout, isLabelAboveInputForSmallWidth() ? 4 : 2);
		}
		else
		{
			Div div = new Div();
			div.setStyle("text-align: right");
			div.appendChild(label);
			m_row.appendChild(div);
	
			m_row.appendChild(editor.getComponent());
		}
		editor.fillHorizontal();
		editor.dynamicDisplay();		
		
		//
		m_newRow = !m_newRow;
	}	//	addLine

	/**
	 *	Load Information
	 *  @param C_ValidCombination_ID valid combination
	 *  @param C_AcctSchema_ID acct schema
	 */
	private void loadInfo (int C_ValidCombination_ID, int C_AcctSchema_ID)
	{
		if (log.isLoggable(Level.FINE)) log.fine("C_ValidCombination_ID=" + C_ValidCombination_ID);
		String sql = "SELECT * FROM C_ValidCombination WHERE C_ValidCombination_ID=? AND C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_ValidCombination_ID);
			pstmt.setInt(2, C_AcctSchema_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				if (f_Alias != null)
					f_Alias.setValue(rs.getString("Alias"));
				f_Combination.setValue(rs.getString("Combination"));
				//
				loadInfoOf (rs, f_AD_Org_ID, "AD_Org_ID");
				loadInfoOf (rs, f_Account_ID, "Account_ID");
				loadInfoOf (rs, f_SubAcct_ID, "C_SubAcct_ID");
				//
				loadInfoOf (rs, f_M_Product_ID, "M_Product_ID");
				loadInfoOf (rs, f_C_BPartner_ID, "C_BPartner_ID");
				loadInfoOf (rs, f_C_Campaign_ID, "C_Campaign_ID");
				loadInfoOf (rs, f_C_LocFrom_ID, "C_LocFrom_ID");
				loadInfoOf (rs, f_C_LocTo_ID, "C_LocTo_ID");
				loadInfoOf (rs, f_C_Project_ID, "C_Project_ID");
				loadInfoOf (rs, f_C_SalesRegion_ID, "C_SalesRegion_ID");
				loadInfoOf (rs, f_AD_OrgTrx_ID, "AD_OrgTrx_ID");
				loadInfoOf (rs, f_C_Activity_ID, "C_Activity_ID");
				loadInfoOf (rs, f_User1_ID, "User1_ID");
				loadInfoOf (rs, f_User2_ID, "User2_ID");
				//
				f_Description.setValue (rs.getString("Description"));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
	}	//	loadInfo

	/**
	 *	Set Value of Editor
	 *  @param rs result set
	 *  @param editor editor
	 *  @param name name
	 *  @throws SQLException
	 */
	private void loadInfoOf (ResultSet rs, WEditor editor, String name) throws SQLException
	{
		if (editor == null)
			return;
		int intValue = rs.getInt(name);
		if (rs.wasNull())
			editor.setValue(null);
		else
			editor.setValue(Integer.valueOf(intValue));
	}	//	loadInfoOf


	/**
	 *	dispose
	 */
	public void dispose()
	{
		saveSelection();
		//  GridController
		if (m_adTabPanel != null)
			m_adTabPanel.detach();
		m_adTabPanel = null;
		//  Model
		m_mTab = null;
		if (m_mWindow != null)
			m_mWindow.dispose();
		m_mWindow = null;

		Env.clearWinContext(m_WindowNo);
		this.onClose();
	}	//	dispose

	
	/* (non-Javadoc)
	 * @see org.adempiere.webui.component.Window#onPageDetached(org.zkoss.zk.ui.Page)
	 */
	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (m_callback != null) {
			m_callback.onCallback(getValue());
		}
	}

	/**
	 *	Save Selection
	 */
	private void saveSelection()
	{
		if (m_changed && m_adTabPanel != null)
		{
			int row = m_adTabPanel.getGridTab().getCurrentRow();
			if (row >= 0)
				m_C_ValidCombination_ID = ((Integer)m_mTab.getValue(row, "C_ValidCombination_ID")).intValue();
			if (log.isLoggable(Level.CONFIG)) log.config("(" + row + ") - " + m_C_ValidCombination_ID);
		}
	}	//	saveSelection

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().getId().equals("Ok"))
		{
			// Compare all data to propose creation/update of combination
			MAccount combiOrg = new MAccount(Env.getCtx(), IDvalue > 0 ? IDvalue : m_mAccount.C_ValidCombination_ID, null);
			boolean needconfirm = false;
			if (needConfirm(f_AD_Org_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_Account_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_SubAcct_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_BPartner_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_M_Product_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_Activity_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_LocFrom_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_LocTo_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_Campaign_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_AD_OrgTrx_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_Project_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_C_SalesRegion_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_User1_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_User2_ID, combiOrg))
				needconfirm = true;

			if (needconfirm) {
				Dialog.ask(m_WindowNo, "CreateNewAccountCombination?", new Callback<Boolean>() {
					public void onCallback(Boolean result) {
						if (result) {
							if (action_Save()) {
								m_changed = true;
								dispose();
							}
						}
					}
				});
			} else {
				m_changed = true;
				dispose();
			}
			
		}
		else if (event.getTarget().getId().equals("Cancel"))
		{
			onCancel();
		}
		//
		else if (event.getTarget() == bSave)
			action_Save();
		else if (event.getTarget() == bIgnore)
			action_Ignore();
		//	all other
		else
			action_Find (true);
	}

	/**
	 * onCancel event
	 */
	private void onCancel() {
		// do not allow to close tab for Events.ON_CTRL_KEY event
		if(isUseEscForTabClosing)
			SessionManager.getAppDesktop().setCloseTabWithShortcut(false);

		m_changed = false;
		dispose();
	}

	/**
	 * @param editor
	 * @param combiOrg
	 * @return true if value has change
	 */
	protected boolean needConfirm(WEditor editor, MAccount combiOrg)
	{
		if (editor != null ) {
			String columnName = editor.getColumnName();
			String oldValue = combiOrg.get_ValueAsString(columnName);
			String newValue = "";
			if (editor.getValue() != null)
				newValue = editor.getValue().toString();
			if (log.isLoggable(Level.FINE)) log.fine("columnName : " + columnName + " : " + oldValue + " - " + newValue);

			return ! oldValue.equals(newValue);
		}

		return false;
	}

	/**
	 *	Status Change Listener
	 *  @param e event
	 */
	@Override
	public void dataStatusChanged (DataStatusEvent e)
	{
		if (log.isLoggable(Level.CONFIG)) log.config(e.toString());
		String info = (String)m_mTab.getValue("Description");
		if (Executions.getCurrent() != null)
			f_Description.setValue (info);
	}	//	statusChanged


	/**
	 *	Action Find.<br/>
	 *	- create where clause<br/>
	 *	- query database
	 *  @param includeAliasCombination include alias combination
	 */
	private void action_Find (boolean includeAliasCombination)
	{
		//	Create where Clause
		MQuery query = null;
		if (m_query != null)
			query = m_query.deepCopy();
		else
			query = new MQuery();
		//	Alias
		if (includeAliasCombination && f_Alias != null && !isEmpty(f_Alias.getValue()))
		{
			String value = f_Alias.getValue().toString().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			query.addRestriction("UPPER(Alias)", MQuery.LIKE, value);
		}
		//	Combination (mandatory)
		if (includeAliasCombination && !isEmpty(f_Combination.getValue()))
		{
			String value = f_Combination.getValue().toString().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			query.addRestriction("UPPER(Combination)", MQuery.LIKE, value);
		}
		//	Org (mandatory)
		if (f_AD_Org_ID != null && !isEmpty(f_AD_Org_ID.getValue()))
			query.addRestriction("AD_Org_ID", MQuery.EQUAL, f_AD_Org_ID.getValue());
		//	Account (mandatory)
		if (f_Account_ID != null && !isEmpty(f_Account_ID.getValue()))
			query.addRestriction("Account_ID", MQuery.EQUAL, f_Account_ID.getValue());
		if (f_SubAcct_ID != null && !isEmpty(f_SubAcct_ID.getValue()))
			query.addRestriction("C_SubAcct_ID", MQuery.EQUAL, f_SubAcct_ID.getValue());

		//	Product
		if (f_M_Product_ID != null && !isEmpty(f_M_Product_ID.getValue()))
			query.addRestriction("M_Product_ID", MQuery.EQUAL, f_M_Product_ID.getValue());
		//	BPartner
		if (f_C_BPartner_ID != null && !isEmpty(f_C_BPartner_ID.getValue()))
			query.addRestriction("C_BPartner_ID", MQuery.EQUAL, f_C_BPartner_ID.getValue());
		//	Campaign
		if (f_C_Campaign_ID != null && !isEmpty(f_C_Campaign_ID.getValue()))
			query.addRestriction("C_Campaign_ID", MQuery.EQUAL, f_C_Campaign_ID.getValue());
		//	Loc From
		if (f_C_LocFrom_ID != null && !isEmpty(f_C_LocFrom_ID.getValue()))
			query.addRestriction("C_LocFrom_ID", MQuery.EQUAL, f_C_LocFrom_ID.getValue());
		//	Loc To
		if (f_C_LocTo_ID != null && !isEmpty(f_C_LocTo_ID.getValue()))
			query.addRestriction("C_LocTo_ID", MQuery.EQUAL, f_C_LocTo_ID.getValue());
		//	Project
		if (f_C_Project_ID != null && !isEmpty(f_C_Project_ID.getValue()))
			query.addRestriction("C_Project_ID", MQuery.EQUAL, f_C_Project_ID.getValue());
		//	SRegion
		if (f_C_SalesRegion_ID != null && !isEmpty(f_C_SalesRegion_ID.getValue()))
			query.addRestriction("C_SalesRegion_ID", MQuery.EQUAL, f_C_SalesRegion_ID.getValue());
		//	Org Trx
		if (f_AD_OrgTrx_ID != null && !isEmpty(f_AD_OrgTrx_ID.getValue()))
			query.addRestriction("AD_OrgTrx_ID", MQuery.EQUAL, f_AD_OrgTrx_ID.getValue());
		//	Activity
		if (f_C_Activity_ID != null && !isEmpty(f_C_Activity_ID.getValue()))
			query.addRestriction("C_Activity_ID", MQuery.EQUAL, f_C_Activity_ID.getValue());
		//	User 1
		if (f_User1_ID != null && !isEmpty(f_User1_ID.getValue()))
			query.addRestriction("User1_ID", MQuery.EQUAL, f_User1_ID.getValue());
		//	User 2
		if (f_User2_ID != null && !isEmpty(f_User2_ID.getValue()))
			query.addRestriction("User2_ID", MQuery.EQUAL, f_User2_ID.getValue());

		//	Query
		m_mTab.setQuery(query);
		m_mTab.query(false);
		statusBar.setStatusDB(String.valueOf(m_mTab.getRowCount()));

		//auto collapse parameter region
		if (isAutoCollapseParameterPane() && northPanel.getParent() instanceof North northRegion)
			northRegion.setOpen(false);

	}	//	action_Find

	private boolean isAutoCollapseParameterPane() {
		if (ClientInfo.isMobile())
			return MSysConfig.getBooleanValue(MSysConfig.ZK_INFO_MOBILE_AUTO_COLLAPSED_PARAMETER_PANEL, true, Env.getAD_Client_ID(Env.getCtx()));
		else
			return MSysConfig.getBooleanValue(MSysConfig.ZK_INFO_AUTO_COLLAPSED_PARAMETER_PANEL, false, Env.getAD_Client_ID(Env.getCtx()));
	}

	/**
	 *	Create/Save Account
	 */
	private boolean action_Save()
	{
		/**
		 *	Check completeness (mandatory fields) ... and for duplicates
		 */
		StringBuilder sb = new StringBuilder();
		StringBuilder sql = new StringBuilder ("SELECT C_ValidCombination_ID, Alias FROM C_ValidCombination WHERE ");
		Object value = null;
		if (m_AcctSchema.isHasAlias())
		{
			value = f_Alias.getValue().toString();
			if (isEmpty(value) && f_Alias.isMandatory())
				sb.append(Msg.translate(Env.getCtx(), "Alias")).append(", ");
		}
		MAcctSchemaElement[] elements = m_AcctSchema.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			MAcctSchemaElement ase = elements[i];
			String type = ase.getElementType();
			//
			if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Organization))
			{
				value = f_AD_Org_ID.getValue();
				sql.append("AD_Org_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
			{
				value = f_Account_ID.getValue();
				sql.append("Account_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_SubAccount))
			{
				value = f_SubAcct_ID.getValue();
				sql.append("C_SubAcct_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Product))
			{
				value = f_M_Product_ID.getValue();
				sql.append("M_Product_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_BPartner))
			{
				value = f_C_BPartner_ID.getValue();
				sql.append("C_BPartner_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Campaign))
			{
				value = f_C_Campaign_ID.getValue();
				sql.append("C_Campaign_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_LocationFrom))
			{
				value = f_C_LocFrom_ID.getValue();
				sql.append("C_LocFrom_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_LocationTo))
			{
				value = f_C_LocTo_ID.getValue();
				sql.append("C_LocTo_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Project))
			{
				value = f_C_Project_ID.getValue();
				sql.append("C_Project_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_SalesRegion))
			{
				value = f_C_SalesRegion_ID.getValue();
				sql.append("C_SalesRegion_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_OrgTrx))
			{
				value = f_AD_OrgTrx_ID.getValue();
				sql.append("AD_OrgTrx_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_Activity))
			{
				value = f_C_Activity_ID.getValue();
				sql.append("C_Activity_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList1))
			{
				value = f_User1_ID.getValue();
				sql.append("User1_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			else if (type.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList2))
			{
				value = f_User2_ID.getValue();
				sql.append("User2_ID");
				if (isEmpty(value))
					sql.append(" IS NULL AND ");
				else
					sql.append("=").append(value).append(" AND ");
			}
			//
			if (ase.isMandatory() && isEmpty(value))
				sb.append(ase.getName()).append(", ");
		}	//	Fields in Element Order

		if (sb.length() != 0)
		{
			Dialog.error(m_WindowNo, "FillMandatory", sb.substring(0, sb.length()-2));
			return false;
		}
		if (f_AD_Org_ID == null || f_AD_Org_ID.getValue() == null)
		{
			Dialog.error(m_WindowNo, "FillMandatory", Msg.getElement(Env.getCtx(), "AD_Org_ID"));
			return false;
		}
		if (f_Account_ID == null || f_Account_ID.getValue() == null)
		{
			Dialog.error(m_WindowNo, "FillMandatory", Msg.getElement(Env.getCtx(), "Account_ID"));
			return false;
		}

		/**
		 *	Check if already exists
		 */
		sql.append("AD_Client_ID=? AND C_AcctSchema_ID=?");
		if (log.isLoggable(Level.FINE)) log.fine("Check = " + sql.toString());
		String Alias = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_AD_Client_ID);
			pstmt.setInt(2, m_AcctSchema.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				IDvalue = rs.getInt(1);
				Alias = rs.getString(2);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			IDvalue = 0;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (log.isLoggable(Level.FINE)) log.fine("ID=" + IDvalue + ", Alias=" + Alias);

		if (Alias == null)
			Alias = "";

		//	We have an account like this already - check alias
		if (IDvalue != 0 && m_AcctSchema.isHasAlias()
			&& !f_Alias.getValue().toString().equals(Alias))
		{
			sql = new StringBuilder("UPDATE C_ValidCombination SET Alias=");
			if (f_Alias.getValue().toString().length() == 0)
				sql.append("NULL");
			else
				sql.append("'").append(f_Alias.getValue()).append("'");
			sql.append(" WHERE C_ValidCombination_ID=").append(IDvalue);
			int i = 0;
			PreparedStatement stmt = null;
			try
			{
				stmt = DB.prepareStatement(sql.toString(),
						ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
				i = stmt.executeUpdate();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			finally
			{
				DB.close(stmt);
				stmt = null;
			}
			if (i == 0)
				Dialog.error(m_WindowNo, "AccountNotUpdated");
		}

		//	load and display
		if (IDvalue != 0)
		{
			loadInfo (IDvalue, m_AcctSchema.getC_AcctSchema_ID());
			action_Find (false);
			return true;
		}

		if (log.isLoggable(Level.CONFIG))
			log.config("New");
		Alias = null;
		if (f_Alias != null)
			Alias = f_Alias.getValue().toString();
		int C_SubAcct_ID = 0;
		if (f_SubAcct_ID != null && !isEmpty(f_SubAcct_ID.getValue()))
			C_SubAcct_ID = ((Integer)f_SubAcct_ID.getValue()).intValue();
		int M_Product_ID = 0;
		if (f_M_Product_ID != null && !isEmpty(f_M_Product_ID.getValue()))
			M_Product_ID = ((Integer)f_M_Product_ID.getValue()).intValue();
		int C_BPartner_ID = 0;
		if (f_C_BPartner_ID != null && !isEmpty(f_C_BPartner_ID.getValue()))
			C_BPartner_ID = ((Integer)f_C_BPartner_ID.getValue()).intValue();
		int AD_OrgTrx_ID = 0;
		if (f_AD_OrgTrx_ID != null && !isEmpty(f_AD_OrgTrx_ID.getValue()))
			AD_OrgTrx_ID = ((Integer)f_AD_OrgTrx_ID.getValue()).intValue();
		int C_LocFrom_ID = 0;
		if (f_C_LocFrom_ID != null && !isEmpty(f_C_LocFrom_ID.getValue()))
			C_LocFrom_ID = ((Integer)f_C_LocFrom_ID.getValue()).intValue();
		int C_LocTo_ID = 0;
		if (f_C_LocTo_ID != null && !isEmpty(f_C_LocTo_ID.getValue()))
			C_LocTo_ID = ((Integer)f_C_LocTo_ID.getValue()).intValue();
		int C_SRegion_ID = 0;
		if (f_C_SalesRegion_ID != null && !isEmpty(f_C_SalesRegion_ID.getValue()))
			C_SRegion_ID = ((Integer)f_C_SalesRegion_ID.getValue()).intValue();
		int C_Project_ID = 0;
		if (f_C_Project_ID != null && !isEmpty(f_C_Project_ID.getValue()))
			C_Project_ID=  ((Integer)f_C_Project_ID.getValue()).intValue();
		int C_Campaign_ID = 0;
		if (f_C_Campaign_ID != null && !isEmpty(f_C_Campaign_ID.getValue()))
			C_Campaign_ID = ((Integer)f_C_Campaign_ID.getValue()).intValue();
		int C_Activity_ID = 0;
		if (f_C_Activity_ID != null && !isEmpty(f_C_Activity_ID.getValue()))
			C_Activity_ID = ((Integer)f_C_Activity_ID.getValue()).intValue();
		int User1_ID = 0;
		if (f_User1_ID != null && !isEmpty(f_User1_ID.getValue()))
			User1_ID = ((Integer)f_User1_ID.getValue()).intValue();
		int User2_ID = 0;
		if (f_User2_ID != null && !isEmpty(f_User2_ID.getValue()))
			User2_ID = ((Integer)f_User2_ID.getValue()).intValue();

		MAccount acct = MAccount.get (Env.getCtx(), m_AD_Client_ID,
			((Integer)f_AD_Org_ID.getValue()).intValue(),
			m_AcctSchema.getC_AcctSchema_ID(),
			((Integer)f_Account_ID.getValue()).intValue(), C_SubAcct_ID,
			M_Product_ID, C_BPartner_ID, AD_OrgTrx_ID,
			C_LocFrom_ID, C_LocTo_ID, C_SRegion_ID,
			C_Project_ID, C_Campaign_ID, C_Activity_ID,
			User1_ID, User2_ID, 0, 0, null);
		if (acct != null && acct.get_ID() == 0)
			acct.saveEx();

		//  Show Info
		if (acct == null || acct.get_ID() == 0)
			loadInfo (0, 0);
		else
		{
			//	Update Account with optional Alias
			if (Alias != null && Alias.length() > 0)
			{
				acct.setAlias(Alias);
				acct.saveEx();
			}
			loadInfo (acct.get_ID(), m_AcctSchema.getC_AcctSchema_ID());
		}
		IDvalue = acct.get_ID();
		action_Find (false);
		return true;
	}	//	action_Save

	/**
	 * @param value
	 * @return true if value is null or empty string
	 */
	private boolean isEmpty(Object value) {
		if (value == null)
			return true;

		if (value instanceof String)
			return ((String)value).trim().length() == 0;

		return false;
	}


	/**
	 * Ignore changes
	 */
	private void action_Ignore()
	{
		if (f_Alias != null)
			f_Alias.setValue("");
		f_Combination.setValue("");
		f_Description.setValue("");
		//
		//	Org (mandatory)
		f_AD_Org_ID.setValue(null);
		//	Account (mandatory)
		f_Account_ID.setValue(null);
		if (f_SubAcct_ID != null)
			f_SubAcct_ID.setValue(null);

		//	Product
		if (f_M_Product_ID != null)
			f_M_Product_ID.setValue(null);
		//	BPartner
		if (f_C_BPartner_ID != null)
			f_C_BPartner_ID.setValue(null);
		//	Campaign
		if (f_C_Campaign_ID != null)
			f_C_Campaign_ID.setValue(null);
		//	Loc From
		if (f_C_LocFrom_ID != null)
			f_C_LocFrom_ID.setValue(null);
		//	Loc To
		if (f_C_LocTo_ID != null)
			f_C_LocTo_ID.setValue(null);
		//	Project
		if (f_C_Project_ID != null)
			f_C_Project_ID.setValue(null);
		//	SRegion
		if (f_C_SalesRegion_ID != null)
			f_C_SalesRegion_ID.setValue(null);
		//	Org Trx
		if (f_AD_OrgTrx_ID != null)
			f_AD_OrgTrx_ID.setValue(null);
		//	Activity
		if (f_C_Activity_ID != null)
			f_C_Activity_ID.setValue(null);
		//	User 1
		if (f_User1_ID != null)
			f_User1_ID.setValue(null);
		//	User 2
		if (f_User2_ID != null)
			f_User2_ID.setValue(null);
	}	//	action_Ignore

	/**
	 *	Get selected account
	 *  @return account (C_ValidCombination_ID)
	 */
	public Integer getValue()
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_ValidCombination_ID=" + m_C_ValidCombination_ID + ", Changed=" + m_changed);
		if (!m_changed || m_C_ValidCombination_ID == 0)
			return null;
		return Integer.valueOf(m_C_ValidCombination_ID);
	}

	/**
	 * 	valueChange - Account Changed
	 *	@param evt event
	 */
	@Override
	public void valueChange(ValueChangeEvent evt) {
		Object newValue = evt.getNewValue();
		if (newValue instanceof Integer) {
			Env.setContext(Env.getCtx(), m_WindowNo, "Account_ID", ((Integer)newValue).intValue());
			Env.setContext(Env.getCtx(), m_WindowNo, 0, "Account_ID", ((Integer)newValue).intValue());
			if (f_SubAcct_ID != null) {
				f_SubAcct_ID.setValue(null);
				f_SubAcct_ID.dynamicDisplay();
			}
		}
	}
		
	/**
	 * onClientInfo event
	 */
	protected void onClientInfo() {
		if (parameterLayout != null && parameterLayout.getRows() != null) {
			boolean smallWidth = ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1);
			if (smallWidth != m_smallWidth) {
				parameterLayout.getRows().detach();
				layoutParameters();
				if (ThemeManager.isUseCSSForWindowSize()) {
					ZKUpdateUtil.setCSSHeight(this);
					ZKUpdateUtil.setCSSWidth(this);
				}
				this.invalidate();
			}
		}
	}
}	//	WAccountDialog
