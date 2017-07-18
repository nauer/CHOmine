package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2017 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */


import org.biojava.bio.seq.Sequence;
import org.intermine.model.bio.BioEntity;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.Organism;
import org.intermine.objectstore.ObjectStoreException;

/**
 * A loader that works for FASTA files with an NCBI formatted header:
 * http://www.ncbi.nlm.nih.gov/blast/fasta.shtml
 * http://en.wikipedia.org/wiki/Fasta_format
 * @author Kim Rutherford
 */
//public class NCBIProteinFastaLoaderTask extends FastaLoaderTask
//{
//    //protected static final Logger LOG = Logger.getLogger(NCBIFastaLoaderTask.class);
//    private static final String ORG_HEADER = " Homo sapiens ";
//    private static final String CHROMOSOME_HEADER = "chromosome";
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected String getIdentifier(Sequence bioJavaSequence) {
//        Annotation anno = bioJavaSequence.getAnnotation();
//        String header = anno.getProperty("description_line").toString();
//        String desc = bioJavaSequence.getAnnotation().getProperty("description").toString();
//        // >gi|568815597|ref|NC_000001.11| Homo sapiens chromosome 1, GRCh38.p2 Primary Assembly
//        // gi|251831106|ref|NC_012920.1| Homo sapiens mitochondrion, complete genome
//        for (String headerString : header.split("\\|")) {
//            if (headerString.contains("mitochondrion")) {
//                return "MT";
//            }
//            // we want the phrase with "chromosome" in it
//            if (headerString.contains(CHROMOSOME_HEADER)) {
//                // chop off the part after the comma
//                String[] headerSubStrings = headerString.split(",");
//                // chop off everything but the chromosome number
//                String identifier = headerSubStrings[0].substring(ORG_HEADER.length()
//                        + CHROMOSOME_HEADER.length());
//                return identifier.trim();
//
//            }
//        }
//        // nothing found
//        throw new RuntimeException("Couldn't find chromosome identifier " + header);
//    }
//}

public class NCBIProteinFastaLoaderTask extends FastaLoaderTask
{
	
	 /**
	  * For the given BioJava Sequence object, return the description to be used when creating
	  * the corresponding BioEntity.
	  * i.e. NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
	  * @param bioJavaSequence the sequenece
	  * @return an identifier
	  */	  
	  protected String getDescription(Sequence bioJavaSequence)
	  {          
	      String desc = bioJavaSequence.getAnnotation().getProperty("description").toString();
	
	      return desc;
	  }

      
      /**
       * For the given BioJava Sequence object, return the protein RefSeq name to be used when creating
       * the corresponding BioEntity.
       * if | is present the middle bit is returned, eg gi|91176213|ref|YP_537129.1| NADH dehydrogenase subunit 5 (mitochondrion) [Cricetulus griseus]
       * @param bioJavaSequence the sequenece
       * @return an identifier
       */	  
      protected String getProteinName(Sequence bioJavaSequence)
      {
          String name = bioJavaSequence.getName();

          if (name.contains("|"))
          {
              String[] bits = name.split("\\|");
              if (bits.length < 2) {
                  return null;
              }
              name = bits[bits.length - 1];
          }
          else
            return null;

          return name;
      }
      
      
    /**
     * Do any extra processing needed for this record (extra attributes, objects, references etc.)
     * This method is called before the new objects are stored
     * @param bioJavaSequence the BioJava Sequence
     * @param sequence the Sequence
     * @param bioEntity the object that references the Sequence
     * @param organism the Organism object for the new InterMineObject
     * @param dataSet the DataSet object
     * @throws ObjectStoreException if a store() fails during processing
     */
    @Override
    protected void extraProcessing(Sequence bioJavaSequence,
            org.intermine.model.bio.Sequence sequence,
            BioEntity bioEntity, Organism organism, DataSet dataSet)
        throws ObjectStoreException
        {
          // String header = bioJavaSequence.getName();
          String desc = getDescription(bioJavaSequence);
          
          int StartOrganism = desc.indexOf("[");
          
          // Remove optional organism part i.e. [Cricetulus griseus]
          if (StartOrganism > 0)
        	  desc = desc.substring(0, StartOrganism).trim();

          String refseqId = getProteinName(bioJavaSequence);

          if (refseqId != null)
          {
              bioEntity.setFieldValue("refseqAccession", refseqId);
              // Use RefSeq id instead of deprecated gi id
              bioEntity.setFieldValue("primaryIdentifier", refseqId);
              bioEntity.setFieldValue("name", desc);
          }
        }
    
    
}

