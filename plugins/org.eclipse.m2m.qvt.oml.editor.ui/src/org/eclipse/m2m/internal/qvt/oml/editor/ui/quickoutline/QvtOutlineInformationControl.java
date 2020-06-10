/*******************************************************************************
 * Copyright (c) 2013, 2018 S. Boyko and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Sergey Boyko - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.editor.ui.quickoutline;

import java.text.MessageFormat;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.m2m.internal.qvt.oml.cst.ContextualPropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingDeclarationCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingMethodCS;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.QvtEditor;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.quickoutline.actions.LexicalSortingAction;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.quickoutline.actions.ShowFQNAction;
import org.eclipse.ocl.cst.SimpleNameCS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class QvtOutlineInformationControl extends AbstractInformationControl {
	private KeyAdapter myKeyAdapter;

	private OutlineContentProvider myOutlineContentProvider;
	private OutlineLabelProvider myLabelProvider;
	private OutlineSorter myOutlineSorter;

	private ShowFQNAction myShowFQNAction;
	private LexicalSortingAction myLexicalSortingAction;

	private final QvtEditor myEditor;
	private final ISourceViewer mySourceViewer;

	/**
	 * Creates a new Java outline information control.
	 *
	 * @param parent
	 * @param shellStyle
	 * @param treeStyle
	 * @param commandId
	 * @param sourceViewer
	 * @param editor
	 */
	public QvtOutlineInformationControl(Shell parent, int shellStyle, int treeStyle, String commandId, QvtEditor editor, ISourceViewer sourceViewer) {
		super(parent, shellStyle, treeStyle, commandId, true);
		myEditor = editor;
		mySourceViewer = sourceViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Text createFilterText(Composite parent) {
		Text text= super.createFilterText(parent);
		text.addKeyListener(getKeyAdapter());
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TreeViewer createTreeViewer(Composite parent, int style) {
		Tree tree= new Tree(parent, SWT.SINGLE | (style & ~SWT.MULTI));
		GridData gd= new GridData(GridData.FILL_BOTH);
		gd.heightHint= tree.getItemHeight() * 12;
		tree.setLayoutData(gd);

		final TreeViewer treeViewer= new TreeViewer(tree);

		myShowFQNAction = new ShowFQNAction(treeViewer, getDialogSettings());

		myLabelProvider= new OutlineLabelProvider(this);
		treeViewer.setLabelProvider(myLabelProvider);

		myLexicalSortingAction = new LexicalSortingAction(treeViewer, getDialogSettings());

		myOutlineContentProvider= new OutlineContentProvider(this, false);
		treeViewer.setContentProvider(myOutlineContentProvider);
		myOutlineSorter= new OutlineSorter(this);
		treeViewer.setComparator(myOutlineSorter);
		treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		treeViewer.getTree().addKeyListener(getKeyAdapter());

		return treeViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStatusFieldText() {
		TriggerSequence[] sequences= getInvokingCommandKeySequences();
		if (sequences == null || sequences.length == 0)
			return ""; //$NON-NLS-1$

		String keySequence= sequences[0].format();

		if (myOutlineContentProvider.isShowingImportedMembers()) {
			return MessageFormat.format(TextMessages.QvtOutlineInformationControl_statusFieldText_hideInheritedMembers, keySequence);
		}
		return MessageFormat.format(TextMessages.QvtOutlineInformationControl_statusFieldText_showInheritedMembers, keySequence);
	}

	@Override
	protected String getId() {
		return QvtOutlineInformationControl.class.getName();
	}

	public QvtEditor getEditor() {
		return myEditor;
	}

	public ISourceViewer getSourceViewer() {
		return mySourceViewer;
	}

	public ShowFQNAction getShowFQNAction() {
		return myShowFQNAction;
	}

	public LexicalSortingAction getLexicalSortingAction() {
		return myLexicalSortingAction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInput(Object information) {
		inputChanged(information, information);
	}

	private KeyAdapter getKeyAdapter() {
		if (myKeyAdapter == null) {
			myKeyAdapter= new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int accelerator = SWTKeySupport.convertEventToUnmodifiedAccelerator(e);
					TriggerSequence keySequence = KeySequence.getInstance(SWTKeySupport.convertAcceleratorToKeyStroke(accelerator));
					TriggerSequence[] sequences= getInvokingCommandKeySequences();
					if (sequences == null)
						return;
					for (int i= 0; i < sequences.length; i++) {
						if (sequences[i].equals(keySequence)) {
							e.doit= false;
							toggleShowInheritedMembers();
							return;
						}
					}
				}
			};
		}
		return myKeyAdapter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void handleStatusFieldClicked() {
		toggleShowInheritedMembers();
	}

	protected void toggleShowInheritedMembers() {
		myOutlineContentProvider.toggleShowInheritedMembers();
		updateStatusFieldText();
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.AbstractInformationControl#fillViewMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void fillViewMenu(IMenuManager viewMenu) {
		super.fillViewMenu(viewMenu);

		viewMenu.add(new Separator("View Options")); //$NON-NLS-1$
		viewMenu.add(myShowFQNAction);

		viewMenu.add(new Separator("Sorters")); //$NON-NLS-1$
		viewMenu.add(myLexicalSortingAction);
	}

	@Override
	protected boolean matchesTextPattern(Object element) {
		if (myLabelProvider.isShowFQN()) {
			SimpleNameCS simpleNameCS = null;
			if (element instanceof MappingMethodCS) {
				MappingMethodCS mappingMethodCS = (MappingMethodCS) element;
				MappingDeclarationCS mappingDeclarationCS = mappingMethodCS.getMappingDeclarationCS();
				if (mappingDeclarationCS != null) {
					simpleNameCS = mappingDeclarationCS.getSimpleNameCS();
				}
			} else if (element instanceof ContextualPropertyCS) {
				ContextualPropertyCS contextualPropertyCS = (ContextualPropertyCS) element;
				simpleNameCS = contextualPropertyCS.getSimpleNameCS();
			}
			if (simpleNameCS != null) {
				String simpleName = simpleNameCS.getValue();
				return (simpleName != null) && getStringMatcher().match(simpleName);
			}
		}
		return false;
	}
}