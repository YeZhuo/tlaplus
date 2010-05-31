/**
 * 
 */
package org.lamport.tla.toolbox.ui.contribution;

import java.awt.Color;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.lamport.tla.toolbox.Activator;
import org.lamport.tla.toolbox.spec.Spec;
import org.lamport.tla.toolbox.util.AdapterFactory;
import org.lamport.tla.toolbox.util.pref.IPreferenceConstants;
import org.lamport.tla.toolbox.util.pref.PreferenceStoreHelper;

/**
 * @author lamport
 *
 */
public class SizeControlContribution extends WorkbenchWindowControlContribution
{
    // the element
    private Composite composite;
    // label to display status
    private Label sizeLabel;

    /**
     * 
     */
    public SizeControlContribution()
    {
        // We have no idea what this string does.
        // The corresponding one for ParseStatusContributionItem
        // does not appear elsewhere in the code.
        super("specDirectorySize");
        Activator.getDefault().setSizeControlContribution(this);

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.ControlContribution#createControl(org.eclipse.swt.widgets.Composite)
     */
    protected Control createControl(Composite parent)
    {
        // If the composite is good just return it.
        if (composite != null && !composite.isDisposed())
        {
            return composite;
        }

        // Create a composite to place the label in
        composite = new Composite(parent, SWT.NONE);
        // composite.setData(this);

        // Give some room around the control
        FillLayout layout = new FillLayout();
        layout.marginHeight = 2;
        layout.marginWidth = 20;
        composite.setLayout(layout);

        // label informing the user that this reflects the parse status of the spec
        Label description = new Label(composite, SWT.NONE);
        description.setText("Storage (KB): ");
        description.setSize(50, 20);
        // description.setBackground(description.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

        // Create label inside composite.
        sizeLabel = new Label(composite, SWT.BORDER | SWT.CENTER);
        sizeLabel.setToolTipText("Size of .toolbox directory");
        sizeLabel.setSize(100, 20);
        sizeLabel.setBackground(description.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        // update status
        updateSize();
        return composite;
    }

    // Updates status from the specification currently loaded in the SpecManager
    public void updateSize()
    {
        if (sizeLabel == null || sizeLabel.isDisposed())
        {
            return;
        }

        Spec spec = Activator.getSpecManager().getSpecLoaded();
        if (spec == null)
        {
            composite.setVisible(false);
            return;
        }

        IPreferenceStore preferenceStore = PreferenceStoreHelper.getProjectPreferenceStore(spec.getProject());
        String size = preferenceStore.getString(IPreferenceConstants.P_PROJECT_TOOLBOX_DIR_SIZE);
        sizeLabel.setText(size);

        // Make invisible if less than the I_MIN_DISPLAYED_SIZE preference.
        if (Long.parseLong(size) < Activator.getDefault().getPreferenceStore().getInt(
                IPreferenceConstants.I_MIN_DISPLAYED_SIZE))
        {
            composite.setVisible(false);
            return;
        }
        // sizeLabel.setBackground(sizeLabel.getDisplay().getSystemColor(AdapterFactory.getStatusAsSWTBGColor(spec)));
        // sizeLabel.setForeground(sizeLabel.getDisplay().getSystemColor(AdapterFactory.getStatusAsSWTFGColor(spec)));
        sizeLabel.redraw();
        composite.setVisible(true);
    }

}
