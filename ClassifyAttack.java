

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.javaml.clustering.SOM;
import net.sf.javaml.clustering.SOM.GridType;
import net.sf.javaml.clustering.SOM.LearningType;
import net.sf.javaml.clustering.SOM.NeighbourhoodFunction;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import net.sf.javaml.tools.data.ARFFHandler;

public class SOM3 {
    public static void main(String ar[]) throws FileNotFoundException {
        int correct = 0, wrong = 0;
        long time1, time2;
        
        Dataset[] clusters;
        EuclideanDistance ed = new EuclideanDistance();
        double min = Double.POSITIVE_INFINITY;
        System.out.println("Began main function");    
        SOM map = new SOM( 2, 2, GridType.RECTANGLES, 10000, 0.1, 4, LearningType.LINEAR, NeighbourhoodFunction.STEP );
        Dataset data = ARFFHandler.loadARFF(new File("/home/prakrati/Desktop/dataSets/DataSet.arff"), 5);
        NormalizeMidrange nm=new NormalizeMidrange(0,1);
        nm.filter(data);
        
        time1 = System.currentTimeMillis();
        clusters = map.cluster(data);
        time2 = System.currentTimeMillis();
        System.out.println("created clusters");
        System.out.println("Time taken for clustering = "+(time2-time1));
        Dataset data1 = ARFFHandler.loadARFF(new File("/home/prakrati/Desktop/dataSets/DataSetTEST.arff"),5);
        NormalizeMidrange nmr=new NormalizeMidrange(0,1);
        nmr.filter(data1);
        Object res = null;
        time1 = System.currentTimeMillis();
        for (Instance inst : data1) {
            for (int i = 0; i < clusters.length; i++) {
                for( int j=0;j<clusters[i].size();j++){
                    double d = ed.measure(clusters[i].get(j), inst);
                    if (d < min) {
                        min=d;
                        res =  clusters[i].get(j).classValue();
                    }
                }
            }
            Object realClassValue = inst.classValue();
            if (res.equals(realClassValue))
                 correct++;
            else
                 wrong++;
             if( res.toString().equalsIgnoreCase("0"))
                System.out.println("NOT AN ATTACK");
             else
                System.out.println("DDOS ATTACK");
        }
        time2 = System.currentTimeMillis();
        System.out.println("Time taken for classification = "+(time2-time1));
        System.out.println("ACCURACY = " + correct/(double)(correct+wrong));
        
    }
}