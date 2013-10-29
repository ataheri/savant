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
package savant.medsavant;

import java.util.ArrayList;
import java.util.List;
import org.broad.igv.feature.genome.Genome.ChromosomeComparator;
import savant.api.data.PointRecord;
import savant.api.data.VariantRecord;
import savant.api.data.VariantType;

/**
 *
 * @author Andrew
 */
public class MergedMedSavantVariantRecord implements VariantRecord {
    
    private int count;
    private int position;
    private String chrom;
    private String name = "";
    private VariantType[][] variantTypes;
    private String ref;
    private VariantType defaultVariantType = VariantType.NONE;
    private List<String> altAlleles = new ArrayList<String>();
    private int[][] participantAlts;
    
    public MergedMedSavantVariantRecord(Object[] base, int participantCount){
        this.chrom = (String)base[MedSavantDataSource.RECORD_INDEX_CHROM];
        this.position = (Integer)base[MedSavantDataSource.RECORD_INDEX_POSITION];
        this.ref = (String)base[MedSavantDataSource.RECORD_INDEX_REF];
        this.count = participantCount;
        this.variantTypes = new VariantType[count][];
        this.participantAlts = new int[count][];
    }
    
    public void addRecord(Object[] arr, int index){

        //name
        if(name.length() == 0){
            this.name = (String)arr[MedSavantDataSource.RECORD_INDEX_DBSNP_ID];
        }
        
        //alt alleles
        String[] alts = ((String)arr[MedSavantDataSource.RECORD_INDEX_ALT]).split(",");
        for(int i = 0; i < alts.length; i++){
            if(!altAlleles.contains(alts[i])){
                altAlleles.add(alts[i]);
            }
        }
        String[] gt = ((String)arr[MedSavantDataSource.RECORD_INDEX_GT]).split("/|\\\\|\\|");
        int[] participantAlt = new int[gt.length];
        VariantType[] participantVariant = new VariantType[gt.length];
        boolean foundNonZero = false;
        for(int i = 0; i < gt.length; i++){
            Integer x = Integer.parseInt(gt[i]);
            if(x == 0){
                participantAlt[i] = 0;
                participantVariant[i] = VariantType.NONE;
            } else {
                participantAlt[i] = altAlleles.indexOf(alts[x-1])+1;
                participantVariant[i] = convertVariantType(org.ut.biolab.medsavant.vcf.VariantRecord.VariantType.valueOf((String)arr[MedSavantDataSource.RECORD_INDEX_VARIANT_TYPE]), alts[x-1]);
                defaultVariantType = participantVariant[i];
                foundNonZero = true;
            }
        }
        if(foundNonZero){
            participantAlts[index] = participantAlt;
            variantTypes[index] = participantVariant;
        } 
        
    }

    @Override
    public VariantType getVariantType() {
        return defaultVariantType;
    }

    @Override
    public String getRefBases() {
        return ref;
    }

    @Override
    public String[] getAltAlleles() {
        String[] alts = new String[altAlleles.size()];
        for(int i = 0; i < altAlleles.size(); i++){
            alts[i] = altAlleles.get(i);
        }
        return alts;
    }

    @Override
    public int getParticipantCount() {
        return count;
    }

    @Override
    public VariantType[] getVariantsForParticipant(int index) {
        if(variantTypes[index] == null){
            return new VariantType[]{VariantType.NONE};
        }
        return variantTypes[index];
    }

    @Override
    public int[] getAllelesForParticipant(int index) {
        if(participantAlts[index] == null){
            return new int[]{0};
        }
        return participantAlts[index];
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getReference() {
        return chrom;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof PointRecord)) return -1;
        PointRecord other = (PointRecord)o;
        int chromCompare = (new ChromosomeComparator()).compare(chrom, other.getReference());
        if(chromCompare != 0) return chromCompare;
        return ((Integer)position).compareTo(other.getPosition());
    }
    
    public static VariantType convertVariantType(org.ut.biolab.medsavant.vcf.VariantRecord.VariantType type, String alt) {
        switch(type){
            case Insertion:
                return VariantType.INSERTION;
            case Deletion:
                return VariantType.DELETION;
            case SNP:
                if(alt != null && alt.length() > 0){
                    String a = alt.substring(0, 1).toLowerCase();
                    if(a.equals("a")){
                        return VariantType.SNP_A;
                    } else if (a.equals("c")){
                        return VariantType.SNP_C;
                    } else if (a.equals("g")){
                        return VariantType.SNP_G;
                    } else if (a.equals("t")){
                        return VariantType.SNP_T;
                    }
                }
            default:
                return VariantType.OTHER;
        }
    }
    
    
}
