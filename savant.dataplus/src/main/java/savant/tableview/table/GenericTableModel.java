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
package savant.tableview.table;

import com.jidesoft.grid.DefaultContextSensitiveTableModel;
import java.util.Vector;

/**
 *
 * @author mfiume
 */
public class GenericTableModel extends DefaultContextSensitiveTableModel {

    Vector columnClasses;

    public GenericTableModel(Vector data, Vector columnNames, Vector columnClasses) {
        super(data, columnNames);
        this.columnClasses = columnClasses;
    }

    public GenericTableModel(Vector data, Vector columnNames) {
        this(data, columnNames,null);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnClasses == null || columnClasses.size() == 0) return String.class;
        return (Class) columnClasses.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
