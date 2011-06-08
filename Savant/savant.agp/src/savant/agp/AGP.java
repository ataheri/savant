/*
 *    Copyright 2010 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package savant.agp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.plugin.PluginAdapter;
import savant.plugin.SavantPanelPlugin;

public class AGP extends SavantPanelPlugin {
    private static final Log LOG = LogFactory.getLog(AGP.class);

    HTTPBrowser browser;

    @Override
    public void init(JPanel parent, PluginAdapter adapter) {
        try {
            parent.setLayout(new BorderLayout());
            browser = new HTTPBrowser(new URL("http://compbio.cs.utoronto.ca/savant/data/asdexome/"));
            parent.add(browser, BorderLayout.CENTER);
        } catch (IOException x) {
            parent.add(new JLabel("Unable to load 1000 genomes plugin: " + x.getMessage()));
        }
    }

    @Override
    protected void doStart() throws Exception {
    }

    @Override
    protected void doStop() throws Exception {
        if (browser != null) {
            browser.closeConnection();
        }
    }

    /**
     * @return title to be displayed in panel
     */
    @Override
    public String getTitle() {
        return "Autism Genome Project";
    }
}