/******************************************************************************
 * Copyright (C) 2013 Heng Sin Low                                            *
 * Copyright (C) 2013 Trek Global                 							  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.panel;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.Icon;
import org.adempiere.webui.util.TreeItemAction;
import org.adempiere.webui.util.TreeNodeAction;
import org.adempiere.webui.util.TreeUtils;
import org.compiere.model.MTreeNode;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * Add open and new button to {@link TreeSearchPanel}
 * @author hengsin
 */
public class MenuTreeSearchPanel extends TreeSearchPanel {
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 3127547233019932429L;

	protected Toolbarbutton newBtn;
	protected Toolbarbutton openBtn;
	protected boolean isNew = false;

	/**
	 * @param tree
	 * @param event
	 * @param windowno
	 * @param tabno
	 */
	public MenuTreeSearchPanel(Tree tree, String event, int windowno, int tabno) {
		super(tree, event, windowno, tabno);
	}

	/**
	 * @param tree
	 * @param event
	 */
	public MenuTreeSearchPanel(Tree tree, String event) {
		super(tree, event);
	}

	/**
	 * @param tree
	 */
	public MenuTreeSearchPanel(Tree tree) {
		super(tree);
	}

	@Override
	protected void init() {
		super.init();
		layout.getFirstChild().detach();
		Hlayout hlayout = new Hlayout();
		hlayout.setValign("middle");
		hlayout.setSpacing("0px");
		hlayout.setSclass("menu-search-toggle-box");
				
		createNewButton();
		
		createOpenButton();
		
		hlayout.appendChild(newBtn);
		hlayout.appendChild(openBtn);
		
		layout.insertBefore(hlayout, layout.getFirstChild());
	}

	/**
	 * Create toolbar button to launch a menu item 
	 */
	protected void createOpenButton() {
		openBtn = new Toolbarbutton();
		if (ThemeManager.isUseFontIconForImage())
			openBtn.setIconSclass(Icon.getIconSclass(Icon.OPEN));
		else
			openBtn.setImage(ThemeManager.getThemeResource("images/Open16.png"));
		openBtn.setSclass("menu-search-toggle-on");
		openBtn.setDisabled(true);
		openBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				openBtn.setDisabled(true);
				openBtn.setSclass("menu-search-toggle-on");
				isNew = false;
				newBtn.setDisabled(false);
				newBtn.setSclass("menu-search-toggle-off");
				refreshAutoComplete();
			}
		});
		openBtn.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Open")));
	}

	/**
	 * Create toolbar button to create new record for a menu item
	 */
	protected void createNewButton() {
		newBtn = new Toolbarbutton();
		if (ThemeManager.isUseFontIconForImage())
			newBtn.setIconSclass(Icon.getIconSclass(Icon.NEW));
		else
			newBtn.setImage(ThemeManager.getThemeResource("images/New16.png"));
		newBtn.setSclass("menu-search-toggle-off");
		newBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				newBtn.setDisabled(true);
				newBtn.setSclass("menu-search-toggle-on");
				isNew = true;
				openBtn.setDisabled(false);
				openBtn.setSclass("menu-search-toggle-off");
				refreshAutoComplete();
			}
		});
		newBtn.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "New")));
	}
	
	@Override
	public void refreshSearchList() {
		treeNodeItemMap.clear();
		if (tree.getModel() == null) {
	    	TreeUtils.traverse(tree, new TreeItemAction() {
				public void run(Treeitem treeItem) {
					if (treeItem.isVisible())
						addTreeItem(treeItem);
				}
	    	});
		} else {
			TreeUtils.traverse(tree.getModel(), new TreeNodeAction() {
				public void run(DefaultTreeNode<?> treeNode) {
					addTreeItem(treeNode);
				}
	    	});
		}

		refreshAutoComplete();
	}

	/**
	 * Refresh auto complete items for {@link #cmbSearch}
	 */
	protected void refreshAutoComplete() {
		List<String> valueList = new ArrayList<String>();
		List<String> descriptionList = new ArrayList<String>();
		List<String> imageList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();

        for (Object value : treeNodeItemMap.values())
        {        	
        	if (value instanceof Treeitem)
        	{
        		Treeitem treeItem = (Treeitem) value;
        		if (isFolder(treeItem))
        			continue;
        		
        		if (isNew) {
        			if (!"window".equals(treeItem.getAttribute(AbstractMenuPanel.MENU_TYPE_ATTRIBUTE))) {
        				continue;
        			}
        		}
        		
        		valueList.add(getLabel(treeItem));
        		descriptionList.add(treeItem.getTooltiptext());
        		typeList.add(String.valueOf(treeItem.getAttribute(AbstractMenuPanel.MENU_TYPE_ATTRIBUTE)));
        		String image = getImage(treeItem);
        		if (image == null || image.length() == 0)
        		{
        			image = ThemeManager.getThemeResource("images/Folder16.png");
        		}
        		imageList.add(image);
        	}
        	else if (value instanceof DefaultTreeNode)
        	{
        		DefaultTreeNode<?> sNode = (DefaultTreeNode<?>) value;
        		MTreeNode mNode = (MTreeNode) sNode.getData();
        		if (!mNode.isLeaf())
        			continue;
        		
        		if (isNew) {
        			if (!mNode.isWindow()) {
        				continue;
        			}        			
        		}
        		
        		valueList.add(mNode.getName());
        		descriptionList.add(mNode.getDescription());
       			imageList.add(mNode.getImagePath());
        	}
        }

        treeDescription = descriptionList.toArray(new String[0]);
        treeValues = valueList.toArray(new String[0]);
        treeImages = imageList.toArray(new String[0]);
        treeTypes = typeList.toArray(new String[0]);               
        orderArrays();

        cmbSearch.setDescription(treeDescription);
        cmbSearch.setDict(treeValues);
        cmbSearch.setImages(treeImages);
        cmbSearch.setContents(treeTypes);
        cmbSearch.refresh("");
	}
	
	@Override
	protected void onPostSelectTreeitem() {
    	Clients.clearBusy();
    	Event event = null;
    	if (eventToFire.equals(Events.ON_CLICK))
    	{
    		if (tree.getSelectedItem().getTreerow().getFirstChild().getFirstChild() instanceof A)
    		{
    			event = new Event(Events.ON_CLICK, tree.getSelectedItem().getTreerow().getFirstChild().getFirstChild(), isNew);
    		}
    		else
    		{
    			event = new Event(Events.ON_CLICK, tree.getSelectedItem().getTreerow(), isNew);
    		}
    	}
    	else
    		event = new Event(eventToFire, tree, isNew);
    	Events.postEvent(event);
    	Events.echoEvent(ON_POST_FIRE_TREE_EVENT, this, null);
    }

	/**
	 * Sort the values, descriptions and images list for menu items
	 */
	protected void orderArrays()
	{
		String aux;
		for (int i = 1; i < treeValues.length; i++)
		{
			for (int j = 0; j < treeValues.length-i; j++)
			{
				if (treeValues[j].compareTo(treeValues[j+1]) > 0)
				{
					aux = treeValues[j];
					treeValues[j] = treeValues[j+1];
					treeValues[j+1]=aux;

					aux = treeDescription[j];
					treeDescription[j] = treeDescription[j+1];
					treeDescription[j+1]=aux;

					aux = treeTypes[j];
					treeTypes[j] = treeTypes[j+1];
					treeTypes[j+1]=aux;

					aux = treeImages[j];
					treeImages[j] = treeImages[j+1];
					treeImages[j+1]=aux;
				}
			}
		}
	}
}
