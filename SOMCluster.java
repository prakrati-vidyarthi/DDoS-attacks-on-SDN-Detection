package ddos;

import net.sf.javaml.clustering.SOM;
import net.sf.javaml.clustering.SOM.GridType;
import net.sf.javaml.clustering.SOM.LearningType;
import net.sf.javaml.clustering.SOM.NeighbourhoodFunction;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.ARFFHandler;
//import net.sf.javaml.clustering.SOM.WeightVectors;

import java.io.File;
import java.io.FileNotFoundException;

public class SelfOrganisingTest {
	
		public static Dataset[] clusters;
	
		public static void main(String ar[]) throws FileNotFoundException {
		SOM map = new SOM( 5, 5, GridType.RECTANGLES, 1000, 0.1, 8, LearningType.LINEAR, NeighbourhoodFunction.STEP );
		Dataset data = ARFFHandler.loadARFF(new File("/home/prakrati/Desktop/dataSets/DataSet.arff"), -1);
		/* Cluster the data, it will be returned as an array of data sets, with
		  each data set representing a cluster. */
		
		clusters = map.cluster(data);
		for(int i=0; i<clusters.length; i++) {
			System.out.println("Cluster_"+i+" = " + clusters[i].size());
		}
	}
}