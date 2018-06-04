package org.intermine.bio.dataconversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.intermine.metadata.ConstraintOp;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.Sequence;
import org.intermine.objectstore.ObjectStoreWriterFactory;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryObjectReference;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.SingletonResults;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreWriter;

public class NCBIProteinFastaLoaderTaskTest extends TestCase {

	private ObjectStoreWriter osw;
    private static final Logger LOG = Logger.getLogger(NCBIFastaLoaderTaskTest.class);
    private String dataSetTitle = "ncbi test title";
    private final String dataSourceName = "test-source";
    
	@Override
    public void setUp() throws Exception {
        osw = ObjectStoreWriterFactory.getObjectStoreWriter("osw.bio-test");
        osw.getObjectStore().flushObjectById();
    }
	
	@Override
    public void tearDown() throws Exception {
        LOG.info("in tear down");
        if (osw.isInTransaction()) {
            osw.abortTransaction();
        }
        Query q = new Query();
        QueryClass qc = new QueryClass(InterMineObject.class);
        q.addFrom(qc);
        q.addToSelect(qc);
        SingletonResults res = osw.getObjectStore().executeSingleton(q);
        LOG.info("created results");
        Iterator<Object> resIter = res.iterator();
        osw.beginTransaction();
        while (resIter.hasNext()) {
            InterMineObject o = (InterMineObject) resIter.next();
            System.out.println("deleting: " + o.getId());
            osw.delete(o);
        }
        osw.commitTransaction();
        LOG.info("committed transaction");
        osw.close();
        LOG.info("closed objectstore");
    }
	
//	public void testExtraProcessing() {
//		fail("Not yet implemented");
//	}
//
//	public void testGetDescriptionSequence() {
//		NCBIProteinFastaLoaderTask
//		fail("Not yet implemented");
//	}
//
//	public void testGetProteinName() {
//		fail("Not yet implemented");
//	}
	
	public void testFastaLoad() throws Exception {
		NCBIProteinFastaLoaderTask flt = new NCBIProteinFastaLoaderTask();
        flt.setFastaTaxonId("10029");
        flt.setIgnoreDuplicates(true);
        flt.setSequenceType("protein");
        flt.setClassName("org.intermine.model.bio.Protein");
        flt.setIntegrationWriterAlias("integration.bio-test");
        flt.setSourceName("fasta-test");
        flt.setDataSetTitle(dataSetTitle);
        flt.setDataSourceName(dataSourceName);
        flt.setClassAttribute("primaryAccession");

        File[] files = new File[1];
        files[0] = File.createTempFile("NCBIProteinFastaLoaderTaskTest", "tmp");
        FileWriter fw = new FileWriter(files[0]);
        InputStream is =
            getClass().getClassLoader().getResourceAsStream("NCBIProtein.fasta");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        while ((line = br.readLine()) != null) {
            fw.write(line + "\n");
        }

        fw.close();
        files[0].deleteOnExit();
        flt.setFileArray(files);
        flt.execute();

        //Check the results to see if we have some data...
        /*ObjectStore os = osw.getObjectStore();

        Query q = new Query();
        QueryClass queryClass = new QueryClass(Protein.class);
        QueryClass seqQueryClass = new QueryClass(Sequence.class);
        q.addToSelect(queryClass);
        q.addToSelect(seqQueryClass);
        q.addFrom(queryClass);
        q.addFrom(seqQueryClass);

        QueryObjectReference qor = new QueryObjectReference(queryClass, "sequence");
        ContainsConstraint cc = new ContainsConstraint(qor, ConstraintOp.CONTAINS, seqQueryClass);

        q.setConstraint(cc);

        Results r = os.execute(q);

        // assertEquals(2, r.size());

        Protein protein = (Protein) ((List) r.get(0)).get(0);

        assertEquals("91176210", protein.getPrimaryAccession());
        assertEquals("10029", protein.getOrganism().getTaxonId());

        DataSet dataSet = protein.getDataSets().iterator().next();
        assertEquals(dataSetTitle, dataSet.getName());
        assertEquals(dataSourceName, dataSet.getDataSource().getName());

        
        // >gi|91176210|ref|YP_537126.1| NADH dehydrogenase subunit 3 (mitochondrion) [Cricetulus griseus]
        
        assertEquals("MNLIMAISTNIILSLVLISVAFWLPQLNTYTEKAGPYECGFDPMSSARLPFSMKFFLVAI"
        			 + "TFLLFDLEIALLLPLPWAMQSMTINMMLTISFLFLSILGLGLAYEWKQKGLEWTE", protein.getSequence().getResidues().toString());*/
	}

}
