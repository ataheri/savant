/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package savant.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.jidesoft.dialog.AbstractDialogPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base class for all sections on the SettingsDialog.
 *
 * @author mfiume, tarkvara
 */
public abstract class Section extends AbstractDialogPage {
    protected final Log LOG = LogFactory.getLog(Section.class);

    /**
     * ActionListener which enables the Apply button when something has been typed.
     */
    protected ActionListener enablingActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enableApplyButton();
        }
    };

    /**
     * KeyListener which enables the Apply button when something has been typed.
     */
    protected KeyAdapter enablingKeyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            enableApplyButton();
        }
    };


    public Section() {
        super("","",null);
        setLayout(new GridBagLayout());
    }

    public abstract void applyChanges();

    public void enableApplyButton() {
        fireButtonEvent(com.jidesoft.dialog.ButtonEvent.ENABLE_BUTTON, com.jidesoft.dialog.MultiplePageDialog.APPLY);
    }

    public void disableApplyButton() {
        fireButtonEvent(com.jidesoft.dialog.ButtonEvent.DISABLE_BUTTON, com.jidesoft.dialog.MultiplePageDialog.APPLY);
    }

    public void populate(){};

    public GridBagConstraints getFullRowConstraints() {
        return new GridBagConstraints(0, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0);
    }
}
