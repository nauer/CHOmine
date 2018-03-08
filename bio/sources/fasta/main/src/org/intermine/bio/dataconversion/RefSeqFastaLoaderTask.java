package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2016 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */


import org.intermine.model.bio.BioEntity;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.Organism;
import org.intermine.objectstore.ObjectStoreException;
import org.biojava.bio.seq.Sequence;
import org.biojava3.core.sequence.ProteinSequence;

/**
 * A loader that works for NCBI protein FASTA files:
 * http://en.wikipedia.org/wiki/Fasta_format
 * @author Norbert Auer
 */
public class RefSeqFastaLoaderTask extends FastaLoaderTask
{
    //protected static final Logger LOG = Logger.getLogger(NCBIFastaLoaderTask.class);

    // gi|91176213|ref|YP_537129.1| NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
    //private static final Pattern p = Pattern.compile("ref\\|([^|]*)\\|$");

    /**
     * Do any extra processing needed for this record (extra attributes, objects, references etc.)
     * This method is called before the new objects are stored
     * @param bioJavaSequence the BioJava Sequence
     * @throws ObjectStoreException if a store() fails during processing
     */
      protected String getIdentifier(ProteinSequence bioJavaSequence)
      {
        // NC_007936.1 Cricetulus griseus mitochondrion, complete genome
    	  String name = bioJavaSequence.getOriginalHeader();

        String[] bits = name.split("\\s+");

        return bits[0];
      }

    @Override
    protected void  extraProcessing(ProteinSequence bioJavaSequence, org.intermine.model.bio.Sequence ncbiProteinSequence, BioEntity bioEntity, Organism organism, DataSet dataSet)
    		throws ObjectStoreException
    {
    	//String header = bioJavaSequence.getOriginalHeader();

    	String refseqId = getIdentifier(bioJavaSequence);

    	if (refseqId != null)
    	{
        String[] bits = refseqId.split("\\.");
    		bioEntity.setFieldValue("refseqAccession", bits[0]);
        bioEntity.setFieldValue("refseqAccessionVersion", bits[1]);
    	}
//    	System.out.print("DEBUG: ");
//    	System.out.print(bioJavaSequence.getOriginalHeader());
//    	System.out.print(" - ");
//    	System.out.println(refseqId);

	}
}
