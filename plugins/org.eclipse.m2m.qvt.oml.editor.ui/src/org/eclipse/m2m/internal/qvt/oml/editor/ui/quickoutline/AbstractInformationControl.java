/*******************************************************************************
 * Copyright (c) 2013, 2018 S. Boyko and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Sergey Boyko - initial API and implementation
 *
 * This file is heavily based on org.eclipse.jdt.internal.ui.text.AbstractInformationControl
 * with changes to suit QVTo-ike naming and filtering policies.
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.editor.ui.quickoutline;

import org.eclipse.core.commands.Command;
import org.eclipse.core.expressions.Expression;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.m2m.internal.qvt.oml.cst.ClassifierDefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingDeclarationCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingMethodCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingModuleCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ModulePropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.TransformationHeaderCS;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.Activator;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.CSTHelper;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.hyperlinks.HyperlinkUtil;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.hyperlinks.QvtFileHyperlink;
import org.eclipse.ocl.cst.CSTNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.LegacyHandlerSubmissionExpression;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.keys.IBindingService;

public abstract class AbstractInformationControl extends PopupDialog implements IInformationControl, IInformationControlExtension, IInformationControlExtension2, DisposeListener {

	/** The control's text widget */
	private Text myFilterText;
	/** The control's tree widget */
	private TreeViewer myTreeViewer;
	/** The current string matcher */
	protected StringMatcher myStringMatcher;
	private Command myInvokingCommand;
	private TriggerSequence[] myInvokingCommandKeySequences;

	/**
	 * Fields that support the dialog menu
	 */
	private Composite myViewMenuButtonComposite;

	private IAction myShowViewMenuAction;
	private IHandlerActivation myShowViewMenuHandlerActivation;

	/**
	 * Field for tree style since it must be remembered by the instance.
	 */
	private int myTreeStyle;

	/**
	 * Creates a tree information control with the given shell as parent. The given
	 * styles are applied to the shell and the tree widget.
	 *
	 * @param parent the parent shell
	 * @param shellStyle the additional styles for the shell
	 * @param treeStyle the additional styles for the tree widget
	 * @param invokingCommandId the id of the command that invoked this control or <code>null</code>
	 * @param showStatusField <code>true</code> iff the control has a status field at the bottom
	 */
	public AbstractInformationControl(Shell parent, int shellStyle, int treeStyle, String invokingCommandId, boolean showStatusField) {
		super(parent, shellStyle, true, true, true, true, true, null, null);
		if (invokingCommandId != null) {
			ICommandService commandService= PlatformUI.getWorkbench().getService(ICommandService.class);
			myInvokingCommand= commandService.getCommand(invokingCommandId);
			if (myInvokingCommand != null && !myInvokingCommand.isDefined())
				myInvokingCommand= null;
			else
				// Pre-fetch key sequence - do not change because scope will change later.
				getInvokingCommandKeySequences();
		}
		myTreeStyle= treeStyle;
		// Title and status text must be set to get the title label created, so force empty values here.
		if (hasHeader())
			setTitleText(""); //$NON-NLS-1$
		setInfoText(""); //  //$NON-NLS-1$

		// Create all controls early to preserve the life cycle of the original implementation.
		create();

		// Status field text can only be computed after widgets are created.
		setInfoText(getStatusFieldText());
	}

	/**
	 * Create the main content for this information control.
	 *
	 * @param parent The parent composite
	 * @return The control representing the main content.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		myTreeViewer= createTreeViewer(parent, myTreeStyle);

		final Tree tree= myTreeViewer.getTree();
		tree.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e)  {
				if (e.character == 0x1B) // ESC
					dispose();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// do nothing
			}
		});

		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// do nothing
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				gotoSelectedElement();
			}
		});

		tree.addMouseMoveListener(new MouseMoveListener()    {
			TreeItem fLastItem= null;
			@Override
			public void mouseMove(MouseEvent e) {
				if (tree.equals(e.getSource())) {
					Object o= tree.getItem(new Point(e.x, e.y));
					if (o instanceof TreeItem) {
						if (!o.equals(fLastItem)) {
							fLastItem= (TreeItem)o;
							tree.setSelection(new TreeItem[] { fLastItem });
						} else if (e.y < tree.getItemHeight() / 4) {
							// Scroll up
							Point p= tree.toDisplay(e.x, e.y);
							Item item= myTreeViewer.scrollUp(p.x, p.y);
							if (item instanceof TreeItem) {
								fLastItem= (TreeItem)item;
								tree.setSelection(new TreeItem[] { fLastItem });
							}
						} else if (e.y > tree.getBounds().height - tree.getItemHeight() / 4) {
							// Scroll down
							Point p= tree.toDisplay(e.x, e.y);
							Item item= myTreeViewer.scrollDown(p.x, p.y);
							if (item instanceof TreeItem) {
								fLastItem= (TreeItem)item;
								tree.setSelection(new TreeItem[] { fLastItem });
							}
						}
					}
				}
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				if (tree.getSelectionCount() < 1)
					return;

				if (e.button != 1)
					return;

				if (tree.equals(e.getSource())) {
					Object o= tree.getItem(new Point(e.x, e.y));
					TreeItem selection= tree.getSelection()[0];
					if (selection.equals(o))
						gotoSelectedElement();
				}
			}
		});

		installFilter();

		addDisposeListener(this);
		return myTreeViewer.getControl();
	}

	/**
	 * Creates a tree information control with the given shell as parent. The given
	 * styles are applied to the shell and the tree widget.
	 *
	 * @param parent the parent shell
	 * @param shellStyle the additional styles for the shell
	 * @param treeStyle the additional styles for the tree widget
	 */
	public AbstractInformationControl(Shell parent, int shellStyle, int treeStyle) {
		this(parent, shellStyle, treeStyle, null, false);
	}

	protected abstract TreeViewer createTreeViewer(Composite parent, int style);

	/**
	 * Returns the name of the dialog settings section.
	 *
	 * @return the name of the dialog settings section
	 */
	protected abstract String getId();

	protected TreeViewer getTreeViewer() {
		return myTreeViewer;
	}

	/**
	 * Returns <code>true</code> if the control has a header, <code>false</code> otherwise.
	 * <p>
	 * The default is to return <code>false</code>.
	 * </p>
	 *
	 * @return <code>true</code> if the control has a header
	 */
	protected boolean hasHeader() {
		// default is to have no header
		return false;
	}

	protected Text getFilterText() {
		return myFilterText;
	}

	protected Text createFilterText(Composite parent) {
		myFilterText= new Text(parent, SWT.NONE);
		Dialog.applyDialogFont(myFilterText);

		GridData data= new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalAlignment= GridData.FILL;
		data.verticalAlignment= GridData.CENTER;
		myFilterText.setLayoutData(data);

		myFilterText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 0x0D) // return
					gotoSelectedElement();
				if (e.keyCode == SWT.ARROW_DOWN)
					myTreeViewer.getTree().setFocus();
				if (e.keyCode == SWT.ARROW_UP)
					myTreeViewer.getTree().setFocus();
				if (e.character == 0x1B) // ESC
					dispose();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// do nothing
			}
		});

		return myFilterText;
	}

	protected void createHorizontalSeparator(Composite parent) {
		Label separator= new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.LINE_DOT);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	protected void updateStatusFieldText() {
		setInfoText(getStatusFieldText());
	}

	/**
	 * Handles click in status field.
	 * <p>
	 * Default does nothing.
	 * </p>
	 */
	protected void handleStatusFieldClicked() {
	}

	protected String getStatusFieldText() {
		return ""; //$NON-NLS-1$
	}

	public StringMatcher getStringMatcher() {
		return myStringMatcher;
	}

	private void installFilter() {
		myFilterText.setText(""); //$NON-NLS-1$

		myFilterText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String text= ((Text) e.widget).getText();
				int length= text.length();
				if (length > 0 && text.charAt(length -1 ) != '*') {
					text= text + '*';
				}
				setMatcherString(text, true);
			}
		});
	}

	/**
	 * The string matcher has been modified. The default implementation
	 * refreshes the view and selects the first matched element
	 */
	protected void stringMatcherUpdated() {
		// refresh viewer to re-filter
		myTreeViewer.getControl().setRedraw(false);
		myTreeViewer.refresh();
		myTreeViewer.expandAll();
		selectFirstMatch();
		myTreeViewer.getControl().setRedraw(true);
	}

	/**
	 * Sets the patterns to filter out for the receiver.
	 * <p>
	 * The following characters have special meaning:
	 *   ? => any character
	 *   * => any string
	 * </p>
	 *
	 * @param pattern the pattern
	 * @param update <code>true</code> if the viewer should be updated
	 */
	protected void setMatcherString(String pattern, boolean update) {
		if (pattern.length() == 0) {
			myStringMatcher = null;
		} else {
			boolean ignoreCase= pattern.toLowerCase().equals(pattern);
			myStringMatcher = new StringMatcher(pattern, ignoreCase, false);
		}

		if (update) {
			stringMatcherUpdated();
		}
	}

	/**
	 * Implementers can modify
	 *
	 * @return the selected element
	 */
	protected Object getSelectedElement() {
		if (myTreeViewer == null)
			return null;

		return ((IStructuredSelection) myTreeViewer.getSelection()).getFirstElement();
	}

	private void gotoSelectedElement() {
		Object selectedElement = getSelectedElement();
		CSTNode cstNode = null;
		if (selectedElement instanceof MappingMethodCS) {
			MappingMethodCS mappingMethodCS = (MappingMethodCS) selectedElement;
			MappingDeclarationCS mappingDeclarationCS = mappingMethodCS.getMappingDeclarationCS();
			if (mappingDeclarationCS != null) {
				cstNode = getLinkDestinationNode(mappingDeclarationCS);
			}
		} else if (selectedElement instanceof ModulePropertyCS) {
			ModulePropertyCS modulePropertyCS = (ModulePropertyCS) selectedElement;
			cstNode = modulePropertyCS.getSimpleNameCS();
			if (cstNode == null) {
				cstNode = modulePropertyCS;
			}
		} else if (selectedElement instanceof MappingModuleCS) {
			MappingModuleCS mappingModuleCS = (MappingModuleCS) selectedElement;
			TransformationHeaderCS headerCS = mappingModuleCS.getHeaderCS();
			if (headerCS != null) {
				cstNode = headerCS.getPathNameCS();
			}
		} else if (selectedElement instanceof ClassifierDefCS) {
			ClassifierDefCS classifierDefCS = (ClassifierDefCS) selectedElement;
			cstNode = classifierDefCS.getSimpleNameCS();
			if (cstNode == null) {
				cstNode = classifierDefCS;
			}
		}
		if (cstNode != null) {
			dispose();
			URI sourceFileURI = CSTHelper.getSourceFile(cstNode);
			IRegion destReg = HyperlinkUtil.createRegion(cstNode);
			QvtFileHyperlink qvtFileHyperlink = new QvtFileHyperlink(new Region(0, 0), sourceFileURI, destReg, destReg);
			qvtFileHyperlink.open();
		}
	}

	private static CSTNode getLinkDestinationNode(MappingDeclarationCS mappingDeclCS) {
		CSTNode linkDestCS = mappingDeclCS.getSimpleNameCS();
		if(linkDestCS == null) {
			linkDestCS = mappingDeclCS;
		}

		return linkDestCS;
	}

	/**
	 * Selects the first element in the tree which
	 * matches the current filter pattern.
	 */
	protected void selectFirstMatch() {
		Tree tree = myTreeViewer.getTree();
		Object element = findElement(tree.getItems());
		if (element != null) {
			myTreeViewer.setSelection(new StructuredSelection(element), true);
		} else {
			myTreeViewer.setSelection(StructuredSelection.EMPTY);
		}
	}

	protected Object findElement(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			Object element = items[i].getData();
			if (isStringMatcherOk(element)) {
				return element;
			}
			element = findElement(items[i].getItems());
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	public boolean isStringMatcherOk(Object element) {
		if (myStringMatcher == null) {
			return true;
		}
		if (element != null) {
			ILabelProvider labelProvider = (ILabelProvider) myTreeViewer.getLabelProvider();
			String label = labelProvider.getText(element);
			return myStringMatcher.match(label) || matchesTextPattern(element);
		}
		return false;
	}

	protected boolean matchesTextPattern(Object element) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInformation(String information) {
		// this method is ignored, see IInformationControlExtension2
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void setInput(Object information);

	/**
	 * Fills the view menu.
	 * Clients can extend or override.
	 *
	 * @param viewMenu the menu manager that manages the menu
	 */
	protected void fillViewMenu(IMenuManager viewMenu) {
	}

	/*
	 * Overridden to call the old framework method.
	 *
	 * @see org.eclipse.jface.dialogs.PopupDialog#fillDialogMenu(IMenuManager)
	 */
	@Override
	protected void fillDialogMenu(IMenuManager dialogMenu) {
		super.fillDialogMenu(dialogMenu);
		fillViewMenu(dialogMenu);
	}

	protected void inputChanged(Object newInput, Object newSelection) {
		myFilterText.setText(""); //$NON-NLS-1$
		myTreeViewer.setInput(newInput);
		if (newSelection != null) {
			myTreeViewer.setSelection(new StructuredSelection(newSelection));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			open();
		} else {
			removeHandlerAndKeyBindingSupport();
			saveDialogBounds(getShell());
			getShell().setVisible(false);
		}
	}

	/*
	 * @see org.eclipse.jface.dialogs.PopupDialog#open()
	 */
	@Override
	public int open() {
		addHandlerAndKeyBindingSupport();
		return super.open();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void dispose() {
		close();
	}

	/**
	 * {@inheritDoc}
	 * @param event can be null
	 * <p>
	 * Subclasses may extend.
	 * </p>
	 */
	@Override
	public void widgetDisposed(DisposeEvent event) {
		removeHandlerAndKeyBindingSupport();
		myTreeViewer= null;
		myFilterText= null;
	}

	/**
	 * Adds handler and key binding support.
	 */
	protected void addHandlerAndKeyBindingSupport() {
		// Register action with command support
		if (myShowViewMenuHandlerActivation == null) {
			IHandlerService handlerService= PlatformUI.getWorkbench().getService(IHandlerService.class);
			Expression expression= new LegacyHandlerSubmissionExpression(null, getShell(), null);
			myShowViewMenuHandlerActivation= handlerService.activateHandler(myShowViewMenuAction.getActionDefinitionId(), new ActionHandler(myShowViewMenuAction), expression);
		}
	}

	/**
	 * Removes handler and key binding support.
	 */
	protected void removeHandlerAndKeyBindingSupport() {
		// Remove handler submission
		if (myShowViewMenuHandlerActivation != null) {
			IHandlerService handlerService= PlatformUI.getWorkbench().getService(IHandlerService.class);
			handlerService.deactivateHandler(myShowViewMenuHandlerActivation);
			myShowViewMenuHandlerActivation= null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasContents() {
		return myTreeViewer != null && myTreeViewer.getInput() != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSizeConstraints(int maxWidth, int maxHeight) {
		// ignore
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point computeSizeHint() {
		// return the shell's size - note that it already has the persisted size if persisting
		// is enabled.
		return getShell().getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(Point location) {
		/*
		 * If the location is persisted, it gets managed by PopupDialog - fine. Otherwise, the location is
		 * computed in Window#getInitialLocation, which will center it in the parent shell / main
		 * monitor, which is wrong for two reasons:
		 * - we want to center over the editor / subject control, not the parent shell
		 * - the center is computed via the initalSize, which may be also wrong since the size may
		 *   have been updated since via min/max sizing of AbstractInformationControlManager.
		 * In that case, override the location with the one computed by the manager. Note that
		 * the call to constrainShellSize in PopupDialog.open will still ensure that the shell is
		 * entirely visible.
		 */
		if (!getPersistLocation() || getDialogSettings() == null)
			getShell().setLocation(location);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(int width, int height) {
		getShell().setSize(width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDisposeListener(DisposeListener listener) {
		getShell().addDisposeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDisposeListener(DisposeListener listener) {
		getShell().removeDisposeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForegroundColor(Color foreground) {
		applyForegroundColor(foreground, getContents());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackgroundColor(Color background) {
		applyBackgroundColor(background, getContents());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFocusControl() {
		return getShell().getDisplay().getActiveShell() == getShell();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		getShell().forceFocus();
		myFilterText.setFocus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFocusListener(FocusListener listener) {
		getShell().addFocusListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFocusListener(FocusListener listener) {
		getShell().removeFocusListener(listener);
	}

	final protected Command getInvokingCommand() {
		return myInvokingCommand;
	}

	final protected TriggerSequence[] getInvokingCommandKeySequences() {
		if (myInvokingCommandKeySequences == null) {
			if (getInvokingCommand() != null) {
				IBindingService bindingService = PlatformUI.getWorkbench().getAdapter(IBindingService.class);
				myInvokingCommandKeySequences = bindingService.getActiveBindingsFor(getInvokingCommand().getId());
				return myInvokingCommandKeySequences;
			}
		}
		return myInvokingCommandKeySequences;
	}

	/*
	 * @see org.eclipse.jface.dialogs.PopupDialog#getDialogSettings()
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		String sectionName = getId();

		IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(sectionName);
		if (settings == null)
			settings = Activator.getDefault().getDialogSettings().addNewSection(sectionName);

		return settings;
	}

	/*
	 * Overridden to insert the filter text into the title and menu area.
	 */
	@Override
	protected Control createTitleMenuArea(Composite parent) {
		myViewMenuButtonComposite= (Composite) super.createTitleMenuArea(parent);

		// If there is a header, then the filter text must be created
		// underneath the title and menu area.

		if (hasHeader()) {
			myFilterText= createFilterText(parent);
		}

		// Create show view menu action
		myShowViewMenuAction= new Action("showViewMenu") { //$NON-NLS-1$
			/*
			 * @see org.eclipse.jface.action.Action#run()
			 */
			@Override
			public void run() {
				showDialogMenu();
			}
		};
		myShowViewMenuAction.setEnabled(true);
		myShowViewMenuAction.setActionDefinitionId("org.eclipse.ui.window.showViewMenu"); //$NON-NLS-1$

		return myViewMenuButtonComposite;
	}

	/*
	 * Overridden to insert the filter text into the title control
	 * if there is no header specified.
	 */
	@Override
	protected Control createTitleControl(Composite parent) {
		if (hasHeader()) {
			return super.createTitleControl(parent);
		}
		myFilterText= createFilterText(parent);
		return myFilterText;
	}

	/*
	 * @see org.eclipse.jface.dialogs.PopupDialog#setTabOrder(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void setTabOrder(Composite composite) {
		if (hasHeader()) {
			composite.setTabList(new Control[] { myFilterText, myTreeViewer.getTree() });
		} else {
			myViewMenuButtonComposite.setTabList(new Control[] { myFilterText });
			composite.setTabList(new Control[] { myViewMenuButtonComposite, myTreeViewer.getTree() });
		}
	}
}