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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package savant.medsavant;

import org.broad.igv.feature.genome.Genome.ChromosomeComparator;
import org.ut.biolab.medsavant.db.api.MedSavantDatabase.DefaultVariantTableSchema;
import savant.api.data.PointRecord;
import savant.api.data.VariantRecord;
import savant.api.data.VariantType;

/**
 *
 * @author Andrew
 */
public class MedSavantVariantRecord implements VariantRecord {
    
    private String chrom;
    private int position;
    private String ref;
    private String alt;    
    private org.ut.biolab.medsavant.vcf.VariantRecord.VariantType type;
    private int index;
    private String genotype;
    private String name;

    public MedSavantVariantRecord(Object[] arr, int index) {
        this.chrom = (String)arr[DefaultVariantTableSchema.INDEX_OF_CHROM];
        this.position = (Integer)arr[DefaultVariantTableSchema.INDEX_OF_POSITION];
        this.ref = (String)arr[DefaultVariantTableSchema.INDEX_OF_REF];
        this.alt = (String)arr[DefaultVariantTableSchema.INDEX_OF_ALT];
        this.type = org.ut.biolab.medsavant.vcf.VariantRecord.VariantType.valueOf((String)arr[DefaultVariantTableSchema.INDEX_OF_VARIANT_TYPE]);
        this.genotype = (String)arr[DefaultVariantTableSchema.INDEX_OF_GT];
        this.name = (String)arr[DefaultVariantTableSchema.INDEX_OF_DBSNP_ID];
        this.index = index;
    }
    
    @Override
    public VariantType getVariantType() {
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

    @Override
    public String getRefBases() {
        return ref;
    }

    @Override
    public String[] getAltAlleles() {
        return alt.split(",");
    }

    @Override
    public int getParticipantCount() {
        return 1;
    }

    @Override
    public VariantType[] getVariantsForParticipant(int index) {
        if(index == this.index){
            return new VariantType[]{getVariantType()};
        } else {
            return new VariantType[]{VariantType.NONE};
        }
    }

    @Override
    public int[] getAllelesForParticipant(int index) {
        if(index == this.index){
            String[] gt = genotype.split("/|\\\\|\\|");
            if(gt.length != 2){
                return new int[]{0};
            }
            int a = Integer.parseInt(gt[0]);
            int b = Integer.parseInt(gt[1]);
            if(a == b){
                return new int[]{a};
            } else {
                return new int[]{a,b};
            }
        } else {
            return new int[]{0};
        }
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
    
    public String toString(){
        return getName();
    }
    
}
