/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package savant.api.util;

import java.util.Set;
import savant.api.adapter.GenomeAdapter;
import savant.api.adapter.RangeAdapter;
import savant.controller.RangeController;
import savant.controller.ReferenceController;
import savant.controller.event.RangeChangeCompletedListener;
import savant.data.types.Genome;
import savant.util.Range;

/**
 * Utilities for navigating Savant
 * @author mfiume
 */
public class NavigationUtils {

    private static RangeController rc = RangeController.getInstance();
    private static ReferenceController refc = ReferenceController.getInstance();

    /**
     * Tell whether a genome has been loaded yet
     * @return Whether or not a genome has been loaded yet
     */
    public static boolean isGenomeLoaded() {
        return refc.isGenomeLoaded();
    }

    /**
     * Get the loaded genome.
     * @return The loaded genome
     */
    public static GenomeAdapter getGenome() {
        return refc.getGenome();
    }

    /**
     * Set the genome
     * @param genome The genome to set
     */
    public static void setGenome(GenomeAdapter genome) {
        refc.setGenome((Genome) genome);
    }

    /**
     * Get the name of the current reference
     * @return The name of the current reference
     */
    public static String getCurrentReferenceName() {
        return refc.getReferenceName();
    }

    /**
     * Get a list of reference names for this genome
     * @return
     */
    public static Set<String> getReferenceNames() {
        return refc.getReferenceNames();
    }

    /**
     * Get the current reference's range.
     * @return The maximumViewableRange
     */
    public static RangeAdapter getCurrentReferenceRange() {
        return rc.getMaxRange();
    }

    /**
     * Navigate to the start of the specified reference
     * @param ref The name of the reference to navigate to
     */
    public void navigateTo(String ref) {
        refc.setReference(ref);
    }

    /**
     * Navigate to the specified range
     * @param r The range to set as current
     */
    public static void navigateTo(RangeAdapter r) {
        rc.setRange((Range) r);
    }

    /**
     * Navigate to the specified range on the specified reference
     * @param reference The name of the reference that the range applies to
     * @param range The range to set as current
     */
    public static void navigateTo(String reference, RangeAdapter range) {
        rc.setRange(reference, (Range) range);
    }

    /**
     * Get the current range
     * @return The currentViewableRange
     */
    public static RangeAdapter getCurrentRange() {
        return rc.getRange();
    }

    /**
     * Subscribe a listener to be notified when the range changes
     * @param l The listener to subscribe
     */
    public static synchronized void addRangeChangeListener(RangeChangeCompletedListener l) {
        rc.addRangeChangeCompletedListener(l);
    }

    /**
     * Unsubscribe a listener from being notified when the range changes
     * @param l The listener to unsubscribe
     */
    public static synchronized void removeRangeChangeListener(RangeChangeCompletedListener l) {
        rc.removeRangeChangeCompletedListener(l);
    }
}
