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
import org.biojava3.core.sequence.ProteinSequence;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreWriter;

public class Test extends TestCase {
	private ObjectStoreWriter osw;
    private static final Logger LOG = Logger.getLogger(NCBIFastaLoaderTaskTest.class);
    private String dataSetTitle = "ncbi test title";
    private final String dataSourceName = "test-source";
	
    @Override
    public void setUp() throws Exception {
        osw = ObjectStoreWriterFactory.getObjectStoreWriter("osw.bio-test");
        osw.getObjectStore().flushObjectById();
    }
    
    /*@Override
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
    }*/
    
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
        /*InputStream is =
            getClass().getClassLoader().getResourceAsStream("NCBIProtein.fasta");
        
        System.out.print(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        while ((line = br.readLine()) != null) {
            fw.write(line + "\n");
            LOG.error(line);
        }*/
        fw.write(">XP_007605693.1 PREDICTED: trans-acting T-cell-specific transcription factor GATA-3 isoform X2 [Cricetulus griseus]\n" +
        		"MEVTADQPRWVSHHHPAVLNGQHPDTHHPGLGHSYMEAQYPLTEEVDVLFNIDGQGNHVPSYYGNSVRAT\n" +
        		"VQRYPPTHHGSQVCRPPLLHGSLPWLDGGKALSSHHTASPWNLSPFSKTSIHHGSPGPLSVYPPASSSSL\n" +
        		"AAGHSSPHLFTFPPTPPKDVSPDPSLSTPGSAGSARQDEKECLKYQVPLPESMKLETSHSRGSMTTLGGA\n" +
        		"SSSAHHPITTYPPYVPEYSSGLFPPSSLLGGSPTGFGCKSRPKARSSTGRECVNCGATSTPLWRRDGTGH\n" +
        		"YLCNACGLYHKMNGQNRPLIKPKRRLSAARRAGTSCANCQTTTTTLWRRNANGDPVCNACGLYYKLHNIN\n" +
        		"RPLTMKKEGIQTRNRKMSSKSKKCKKVHDALEDFPKSSSFNPAALSRHMSSLSHISPFSHSSHMLTTPTP\n" +
        		"MHPPSGLSFGPHHPSSMVTAMG\n");
        /*fw.write(">gi|625180359|ref|XP_007605693.1| PREDICTED: trans-acting T-cell-specific transcription factor GATA-3 isoform X2 [Cricetulus griseus]\n" + 
        		"MEVTADQPRWVSHHHPAVLNGQHPDTHHPGLGHSYMEAQYPLTEEVDVLFNIDGQGNHVPSYYGNSVRAT\n" + 
        		"VQRYPPTHHGSQVCRPPLLHGSLPWLDGGKALSSHHTASPWNLSPFSKTSIHHGSPGPLSVYPPASSSSL\n" + 
        		"AAGHSSPHLFTFPPTPPKDVSPDPSLSTPGSAGSARQDEKECLKYQVPLPESMKLETSHSRGSMTTLGGA\n" + 
        		"SSSAHHPITTYPPYVPEYSSGLFPPSSLLGGSPTGFGCKSRPKARSSTGRECVNCGATSTPLWRRDGTGH\n" + 
        		"YLCNACGLYHKMNGQNRPLIKPKRRLSAARRAGTSCANCQTTTTTLWRRNANGDPVCNACGLYYKLHNIN\n" + 
        		"RPLTMKKEGIQTRNRKMSSKSKKCKKVHDALEDFPKSSSFNPAALSRHMSSLSHISPFSHSSHMLTTPTP\n" + 
        		"MHPPSGLSFGPHHPSSMVTAMG\n"); */ 
                //+ 
        		//">gi|1032828861|ref|XP_016831531.1| PREDICTED: trans-acting T-cell-specific transcription factor GATA-3 isoform X1 [Cricetulus griseus]\n" + 
        		//"MEVTADQPRWVSHHHPAVLNGQHPDTHHPGLGHSYMEAQYPLTEEVDVLFNIDGQGNHVPSYYGNSVRAT\n" + 
        		//"VQRYPPTHHGSQVCRPPLLHGSLPWLDGGKALSSHHTASPWNLSPFSKTSIHHGSPGPLSVYPPASSSSL\n" + 
        		//"AAGHSSPHLFTFPPTPPKDVSPDPSLSTPGSAGSARQDEKECLKYQVPLPESMKLETSHSRGSMTTLGGA\n" + 
        		//"SSSAHHPITTYPPYVPEYSSGLFPPSSLLGGSPTGFGCKSRPKARSSTEGRECVNCGATSTPLWRRDGTG\n" + 
        		//"HYLCNACGLYHKMNGQNRPLIKPKRRLSAARRAGTSCANCQTTTTTLWRRNANGDPVCNACGLYYKLHNI\n" + 
        		//"NRPLTMKKEGIQTRNRKMSSKSKKCKKVHDALEDFPKSSSFNPAALSRHMSSLSHISPFSHSSHMLTTPT\n" + 
        		//"PMHPPSGLSFGPHHPSSMVTAMG\n");
        
        fw.close();
        files[0].deleteOnExit();
        flt.setFileArray(files);
        
        flt.execute();
     
        String hexmd5="98d05a5d29f4a8d04222c679bba87ad5";
        //assertEquals()
        //Check the results to see if we have some data...
        ObjectStore os = osw.getObjectStore();

        
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
        
        
        //flt.getProteinName(ProteinSequence bioJavaSequence)
        
        
	}
	
}