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
public class NCBIProteinFastaLoaderTask extends FastaLoaderTask
{
    //protected static final Logger LOG = Logger.getLogger(NCBIFastaLoaderTask.class);

    // gi|91176213|ref|YP_537129.1| NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
    //private static final Pattern p = Pattern.compile("ref\\|([^|]*)\\|$");

      /**
       * For the given BioJava Sequence object, return the protein name to be used when creating
       * the corresponding BioEntity.
       * if | is present the middle bit is returned, eg gi|91176213|ref|YP_537129.1| NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
       * @param bioJavaSequence the Sequenece
       * @return an identifier
       */
      protected String getProteinName(ProteinSequence bioJavaSequence)
      {
          // gi|91176213|ref|YP_537129.1| NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
    	  String name = bioJavaSequence.getOriginalHeader();

          if (name.contains("ref"))
          {
              String[] bits = name.split("ref\\|");
              if (bits.length < 2) {
                  return null;
              }
              name = bits[bits.length - 1];
              
              name = name.split("\\|")[0];
          }
          else
            return null;

          return name;
      }

    /**
     * Do any extra processing needed for this record (extra attributes, objects, references etc.)
     * This method is called before the new objects are stored
     * @param bioJavaSequence the BioJava Sequence
     * @param ncbiProteinSequence the Protein Sequence
     * @param bioEntity the object that references the Sequence
     * @param organism the Organism object for the new InterMineObject
     * @param dataSet the DataSet object
     * @throws ObjectStoreException if a store() fails during processing
     */
    
    @Override
    protected void  extraProcessing(ProteinSequence bioJavaSequence, org.intermine.model.bio.Sequence ncbiProteinSequence, BioEntity bioEntity, Organism organism, DataSet dataSet)
    		throws ObjectStoreException 
    {
    	//String header = bioJavaSequence.getOriginalHeader();
		
    	String refseqId = getProteinName(bioJavaSequence);
	
    	if (refseqId != null)
    	{
    		bioEntity.setFieldValue("refseqAccession", refseqId);
    	}
//    	System.out.print("DEBUG: ");
//    	System.out.print(bioJavaSequence.getOriginalHeader());
//    	System.out.print(" - ");
//    	System.out.println(refseqId);
	
	}
}
