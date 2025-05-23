/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.editor;

import java.util.ArrayList;

import org.adempiere.webui.component.Menupopup;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.Icon;
import org.adempiere.webui.window.WFieldSuggestion;
import org.compiere.model.GridField;
import org.compiere.model.Lookup;
import org.compiere.model.MFieldSuggestion;
import org.compiere.model.MRole;
import org.compiere.model.MTable;
import org.compiere.model.MToolBarButtonRestrict;
import org.compiere.model.MZoomCondition;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;

/**
 * Popup context menu for {@link WEditor}.
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 25, 2007
 */
public class WEditorPopupMenu extends Menupopup implements EventListener<Event>
{
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 4574171758155591250L;

	/** Menu item attribute to store context menu event name (zoom, requery, etc) */
	public static final String EVENT_ATTRIBUTE = "EVENT";
    public static final String ZOOM_EVENT = "ZOOM";
    public static final String REQUERY_EVENT = "REQUERY";
    public static final String PREFERENCE_EVENT = "VALUE_PREFERENCE";
    public static final String NEW_EVENT = "NEW_RECORD";
    public static final String UPDATE_EVENT = "UPDATE_RECORD";
    public static final String SHOWLOCATION_EVENT = "SHOW_LOCATION";
    public static final String CHANGE_LOG_EVENT = "CHANGE_LOG";
    public static final String EDITOR_EVENT = "EDITOR";
    public static final String RESET_EVENT = "RESET";
    public static final String ASSISTANT_EVENT = "ASSISTANT";
    public static final String DRILL_EVENT = "DRILL";
   
    private boolean newEnabled = true;
    private boolean updateEnabled = true;
    private boolean zoomEnabled  = true;
    private boolean requeryEnabled = true;
    private boolean preferencesEnabled = true;
	private boolean showLocation = true;
	private boolean drillEnabled = true;
    
    private Menuitem zoomItem;
    private Menuitem requeryItem;
    private Menuitem prefItem;
    private Menuitem newItem;
    private Menuitem updateItem;   
	private Menuitem showLocationItem;
	private Menuitem drillItem;
    
    private ArrayList<ContextMenuListener> menuListeners = new ArrayList<ContextMenuListener>();
    
    /**
     * @param zoom
     * @param requery
     * @param preferences
     */
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences)
    {
        this(zoom, requery, preferences, false, false, false, false, null);
    }
    
    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord)
    {
    	this(zoom, requery, preferences, newRecord, false, false, null);
    }
    
    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord)
    {
    	this(zoom, requery, preferences, newRecord, updateRecord, false, null);
    }

    @Deprecated
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation)
    {
    	this(zoom, requery, preferences, newRecord, updateRecord, false, null);
    }

    /**
     * @param zoom
     * @param requery
     * @param preferences
     * @param newRecord
     * @param updateRecord
     * @param showLocation
     * @param lookup
     */
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation, Lookup lookup)
    {
    	this(zoom, requery, preferences, newRecord, updateRecord, showLocation, false, lookup);
    }
    
    /**
     * @param zoom - enable zoom in menu - disabled if the lookup cannot zoom
     * @param requery - enable requery in menu
     * @param preferences - enable preferences in menu
     * @param newRecord - enable new record (ignored and recalculated if lookup is received)
     * @param updateRecord - enable update record (ignored and recalculated if lookup is received)
     * @param showLocation - enable show location in menu
     * @param drillEnabled - enable drill assistant menu
     * @param lookup - when this parameter is received then new and update are calculated based on the zoom and quickentry
     */
    public WEditorPopupMenu(boolean zoom, boolean requery, boolean preferences, boolean newRecord, boolean updateRecord, boolean showLocation, boolean drillEnabled, Lookup lookup)
    {
    	super();
    	this.zoomEnabled = zoom;
    	this.requeryEnabled = requery;
    	this.preferencesEnabled = preferences;
    	this.newEnabled = newRecord;
    	this.updateEnabled = updateRecord;
    	this.showLocation = showLocation;
    	this.drillEnabled = drillEnabled;

    	String tableName = null;
    	if (lookup != null && lookup.getColumnName() != null)
    		tableName = lookup.getColumnName().substring(0, lookup.getColumnName().indexOf("."));

		if (lookup != null) {
    		int winID = lookup.getZoom();
    		int winIDPO = lookup.getZoom(false) ;
    		Boolean canAccess = MRole.getDefault().getWindowAccess(winID);
    		Boolean canAccessPO = null;
			if (winIDPO > 0)
				canAccessPO = MRole.getDefault().getWindowAccess(winIDPO);
			if ((winID <= 0 || canAccess == null) && (canAccessPO == null || canAccessPO == false)) {
    	    	this.zoomEnabled = false;
    	    	this.newEnabled = false;
    	    	this.updateEnabled = false;

    			// check possible zoom conditions to enable back zoom
    	    	MTable table = MTable.get(Env.getCtx(), tableName);
    	    	for (MZoomCondition zoomCondition : MZoomCondition.getConditions(table.getAD_Table_ID())) {
    	    		Boolean canAccessZoom = MRole.getDefault().getWindowAccess(zoomCondition.getAD_Window_ID());
    	    		if (canAccessZoom != null && canAccessZoom) {
    	    			this.zoomEnabled = true;
    	    			if (hasQuickEntryField(zoomCondition.getAD_Window_ID(), 0, tableName)) {
    	    				if (MToolBarButtonRestrict.isNewButtonRestricted(zoomCondition.getAD_Window_ID()))
    	    					this.newEnabled = false;
    	    				else
    	    					this.newEnabled = true;
    	    				this.updateEnabled = true;
    	    			}

    	    			break;
    	    		}
    	    	}
    		} else {
    			if (hasQuickEntryField(winID,winIDPO,tableName)) {
    				if (   !MToolBarButtonRestrict.isNewButtonRestricted(winID)
    					|| (winIDPO > 0 && winIDPO != winID && !MToolBarButtonRestrict.isNewButtonRestricted(winIDPO)))
            	    	this.newEnabled = true;
    				else
    					this.newEnabled = false;
        	    	this.updateEnabled = true;
    			} else {
        	    	this.newEnabled = false;
        	    	this.updateEnabled = false;
    			}
    		}
    	}
    	init();
    }

    /**
     * @param winID
     * @param winIDPO
     * @param tableName
     * @return true if window has quick entry fields (i.e AD_Field.IsQuickEntry=Y)
     */
    protected boolean hasQuickEntryField(int winID, int winIDPO, String tableName) {
    	return DB.getSQLValueEx(null,
    			"SELECT COUNT(*) "
    					+ "FROM   AD_Field f "
    					+ "       JOIN AD_Tab t "
    					+ "         ON ( t.AD_Tab_ID = f.AD_Tab_ID ) "
    					+ "WHERE  t.AD_Window_ID IN (?,?) "
    					+ "       AND f.IsActive = 'Y' "
    					+ "       AND t.IsActive = 'Y' "
    					+ "       AND f.IsQuickEntry = 'Y' "
    					+ "       AND (t.TabLevel = 0 "
    					+ "          AND   t.AD_Table_ID IN (SELECT AD_Table_ID FROM AD_Table WHERE TableName = ? )) ",
    					winID,winIDPO,tableName) > 0;
    }

    /**
     * @return true if zoom is enable
     */
	public boolean isZoomEnabled() {
    	return zoomEnabled;
    }
    
	/**
	 * Init context menu items
	 */
    private void init()
    {
        if (zoomEnabled)
        {
            zoomItem = new Menuitem();
            zoomItem.setAttribute(EVENT_ATTRIBUTE, ZOOM_EVENT);
            zoomItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Zoom")).intern());
            if (ThemeManager.isUseFontIconForImage())
            	zoomItem.setIconSclass(Icon.getIconSclass(Icon.ZOOM));
            else
            	zoomItem.setImage(ThemeManager.getThemeResource("images/Zoom16.png"));
            zoomItem.addEventListener(Events.ON_CLICK, this);
            
            this.appendChild(zoomItem);
        }
        
        if (requeryEnabled)
        {
            requeryItem = new Menuitem();
            requeryItem.setAttribute(EVENT_ATTRIBUTE, REQUERY_EVENT);
            requeryItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")).intern());
            if (ThemeManager.isUseFontIconForImage())
            	requeryItem.setIconSclass(Icon.getIconSclass(Icon.REFRESH));
            else
            	requeryItem.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
            requeryItem.addEventListener(Events.ON_CLICK, this);
            this.appendChild(requeryItem);
        }
        
        if (preferencesEnabled)
        {
            prefItem = new Menuitem();
            prefItem.setAttribute(EVENT_ATTRIBUTE, PREFERENCE_EVENT);
            prefItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "ValuePreference")).intern());
            if (ThemeManager.isUseFontIconForImage())
            	prefItem.setIconSclass(Icon.getIconSclass(Icon.VPREFERENCE));
            else
            	prefItem.setImage(ThemeManager.getThemeResource("images/VPreference16.png"));
            prefItem.addEventListener(Events.ON_CLICK, this);
            this.appendChild(prefItem);
        }
        
        if (newEnabled)
        {
        	newItem = new Menuitem();
        	newItem.setAttribute(EVENT_ATTRIBUTE, NEW_EVENT);
        	newItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "New")).intern());
        	if (ThemeManager.isUseFontIconForImage())
        		newItem.setIconSclass(Icon.getIconSclass(Icon.NEW));
        	else
        		newItem.setImage(ThemeManager.getThemeResource("images/New16.png"));
        	newItem.addEventListener(Events.ON_CLICK, this);
        	this.appendChild(newItem);
        }
        
        if (updateEnabled)
        {
        	updateItem = new Menuitem();
        	updateItem.setAttribute(EVENT_ATTRIBUTE, UPDATE_EVENT);
        	updateItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Update")).intern());
        	if (ThemeManager.isUseFontIconForImage())
        		updateItem.setIconSclass(Icon.getIconSclass(Icon.INFO_BPARTNER));
        	else
        		updateItem.setImage(ThemeManager.getThemeResource("images/InfoBPartner16.png"));
        	updateItem.addEventListener(Events.ON_CLICK, this);
        	this.appendChild(updateItem);
        }
        //
        if (showLocation)
        {
        	showLocationItem = new Menuitem();
        	showLocationItem.setAttribute(EVENT_ATTRIBUTE, SHOWLOCATION_EVENT);
        	showLocationItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "ShowLocation")).intern());
        	if (ThemeManager.isUseFontIconForImage())
        		showLocationItem.setIconSclass(Icon.getIconSclass(Icon.INFO_BPARTNER));
        	else
        		showLocationItem.setImage(ThemeManager.getThemeResource("images/InfoBPartner16.png"));
        	showLocationItem.addEventListener(Events.ON_CLICK, this);
        	this.appendChild(showLocationItem);
        }
        
        if(drillEnabled)
        {
        	drillItem = new Menuitem();
        	drillItem.setAttribute(EVENT_ATTRIBUTE, DRILL_EVENT);
        	drillItem.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "DrillAssistant")).intern());
        	if (ThemeManager.isUseFontIconForImage())
        		drillItem.setIconSclass(Icon.getIconSclass(Icon.WINDOW));
            else
            	drillItem.setImage(ThemeManager.getThemeResource("images/mWindow.png"));
        	drillItem.addEventListener(Events.ON_CLICK, this);
        	this.appendChild(drillItem);
        }
    }
    
    /**
     * Add context menu listener
     * @param listener {@link ContextMenuListener}
     */
    public void addMenuListener(ContextMenuListener listener)
    {
    	if (!menuListeners.contains(listener))
    		menuListeners.add(listener);
    }

    @Override
    public void onEvent(Event event)
    {
        String evt = (String)event.getTarget().getAttribute(EVENT_ATTRIBUTE);
        
        if (evt != null)
        {
            ContextMenuEvent menuEvent = new ContextMenuEvent(evt);
            menuEvent.setTarget(event.getTarget());
            ContextMenuListener[] listeners = new ContextMenuListener[0];
            listeners = menuListeners.toArray(listeners);
            for (ContextMenuListener listener : listeners)
            {
                listener.onMenu(menuEvent);
            }
        }
    }

    /**
     * Add field suggestion context menu item
     * @param field
     */
	public void addSuggestion(final GridField field) {
		if (!MRole.getDefault().isTableAccessExcluded(MFieldSuggestion.Table_ID)) {
			Menuitem editor = new Menuitem(Msg.getElement(Env.getCtx(), "AD_FieldSuggestion_ID"));
			if (ThemeManager.isUseFontIconForImage())
				editor.setIconSclass(Icon.getIconSclass(Icon.FIELD_SUGGESTION));
			else
				editor.setImage(ThemeManager.getThemeResource("images/FieldSuggestion16.png"));
			editor.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					WFieldSuggestion fieldSuggestion = new WFieldSuggestion(field.getAD_Field_ID());
					fieldSuggestion.setPage(WEditorPopupMenu.this.getPage());
					fieldSuggestion.doHighlighted();
				}
			});
			appendChild(editor);
		}
	}

	/**
	 * Remove drill assistant menu item
	 */
	public void showDrillAssistant(boolean show) {
        if (drillItem != null)
            drillItem.setVisible(show);
	}	

	/**
	 * Remove the new and update items from the menu - for ChosenList
	 */
	public void removeNewUpdateMenu() {
		if (newItem != null)
			removeChild(newItem);
		if (updateItem != null)
			removeChild(updateItem);
	}	
}
