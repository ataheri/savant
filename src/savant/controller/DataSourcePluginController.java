/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package savant.controller;

import java.util.ArrayList;
import java.util.List;
import savant.data.sources.DataSource;
import savant.plugin.SavantDataSourcePlugin;

/**
 *
 * @author mfiume
 */
public class DataSourcePluginController {

    private static List<SavantDataSourcePlugin> datasources = new ArrayList<SavantDataSourcePlugin>();
    
    private static DataSourcePluginController instance;
    
    public static DataSourcePluginController getInstance() {
        if (instance == null) {
            instance = new DataSourcePluginController();
        }
        return instance;
    }

    public List<SavantDataSourcePlugin> getDataSourcePlugins() {
        return datasources;
    }

    public void addDataSourcePlugin(SavantDataSourcePlugin p) {
        datasources.add(p);
    }

    public DataSource getDataSourceFromPlugin(SavantDataSourcePlugin p) {
        return p.getDataSource();
    }
}