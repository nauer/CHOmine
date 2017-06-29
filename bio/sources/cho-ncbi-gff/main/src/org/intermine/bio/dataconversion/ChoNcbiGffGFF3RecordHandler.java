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

import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.xml.full.Item;
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.Gene;
import org.apache.log4j.Logger;


/**
 * A converter/retriever for the ChoNcbiGff dataset via GFF files.
 */

public class ChoNcbiGffGFF3RecordHandler extends GFF3RecordHandler
{
    protected static final Logger LOG = Logger.getLogger(ChoNcbiGffGFF3RecordHandler.class);

    /**
     * Create a new ChoNcbiGffGFF3RecordHandler for the given data model.
     * @param model the model for which items will be created
     */
    public ChoNcbiGffGFF3RecordHandler (Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(GFF3Record record) {
        // This method is called for every line of GFF3 file(s) being read.  Features and their
        // locations are already created but not stored so you can make changes here.  Attributes
        // are from the last column of the file are available in a map with the attribute name as
        // the key.   For example:
        //
        //     Item feature = getFeature();
        //     String symbol = record.getAttributes().get("symbol");
        //     feature.setAttribute("symbol", symbol);
        //
        // Any new Items created can be stored by calling addItem().  For example:
        //
        //     String geneIdentifier = record.getAttributes().get("gene");
        //     gene = createItem("Gene");
        //     gene.setAttribute("primaryIdentifier", geneIdentifier);
        //     addItem(gene);
        //
        // You should make sure that new Items you create are unique, i.e. by storing in a map by
        // some identifier.
        Item feature = getFeature();
        System.out.println("FEATURE");
        System.out.println(feature);

        String type = record.getType();
        //String symbol = record.getAttributes().get("symbol");
        //feature.setAttribute("symbol", symbol);

        LOG.error("BEFORE CDS" + type);
        LOG.error(record);
        // Type CDS
        if ("CDS".equals(type))
        {
            LOG.error("IN CDS");
            //System.
            System.out.println("GETATTRIBUTES");
            System.out.println(record.getAttributes());
            System.out.println(record.getAttributes().get("Genbank"));

            // List<String> refseqId = record.getAttributes().get("Genbank");
            // System.out.println("REFSEQID");
            // System.out.println(refseqId);
            //
            // protein = createItem("Protein");
            // protein.setAttribute("primaryIdentifier", refseqId);
            // addItem(protein);


        }

    }

}
