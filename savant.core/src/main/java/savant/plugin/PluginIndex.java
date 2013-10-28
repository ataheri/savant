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
package savant.plugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.util.NetworkUtils;

/**
 * Given a file (typically the plugin.xml file from our web-site), create an index of plugin versions.
 *
 * @author tarkvara
 */
public class PluginIndex {
    private static final Log LOG = LogFactory.getLog(PluginIndex.class);
    private Map<String, URL> urls;

    public PluginIndex(URL url) throws IOException {
        urls = new HashMap<String, URL>();
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(NetworkUtils.openStream(url));
            boolean done = false;
            String id = null;
            do {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT:
                        String elemName = reader.getLocalName();
                        if (elemName.equals("leaf")) {
                            id = reader.getAttributeValue(null, "id");
                        } else if (elemName.equals("url")) {
                            if (id != null) {
                                try {
                                    urls.put(id, new URL(reader.getElementText()));
                                } catch (MalformedURLException x) {
                                    LOG.info("Unable to parse \"" + reader.getElementText() + "\" as a plugin URL.");
                                }
                                id = null;
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        reader.close();
                        done = true;
                        break;
                }
            } while (!done);
        } catch (XMLStreamException x) {
            throw new IOException("Unable to get version number from web-site.", x);
        }
    }

    public URL getPluginURL(String id) {
        return urls.get(id);
    }
}
