import java.util.*;
import org.jnetpcap.Pcap; 
import org.jnetpcap.packet.PcapPacket; 
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PcapFeature2 { 
 
    private static int count = 0, numPacket = 0, flag = 0, pf=0, lenAll = 0;
    private static Ip4 ip = new Ip4();
    private static byte[] sIP = new byte[4];
    private static byte[] dIP = new byte[4];
    private static String sourceIP="", destIP="", sIPflow="", dIPflow="";
    private static long time1, time2;
    private static Set<Integer> ports;
    private static final Tcp tcp = new Tcp();
    
    public static void main(String[] args) throws IOException { 
        final File folder = new File("/home/prakrati/Desktop/dataSetFinal/packetToFlows");
        int npf = 0;
        double avgPacket = 0.0, ppf, avglen = 0.0, gsf;
        ports = new HashSet<Integer>();
        File file = new File("/home/prakrati/Desktop/dataSets/DataSet.arff");
        FileWriter fw = new FileWriter(file.getAbsoluteFile()) ;
        @SuppressWarnings("resource")
        BufferedWriter bw = new BufferedWriter(fw);
       
        for (final File fileEntry : folder.listFiles()) {
            flag = 0;
            count += 1;
            final StringBuilder errbuf = new StringBuilder();
           
            String content = "", fileOpen = "/home/prakrati/Desktop/dataSetFinal/packetToFlows/";
            fileOpen+=fileEntry.getName();
             
            final Pcap pcap = Pcap.openOffline(fileOpen, errbuf); 
            if (pcap == null) { 
                System.err.println(errbuf); // Error is stored in errbuf, if any 
                return; 
            } 
            PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() { 
               
            public void nextPacket(PcapPacket packet, String user) { 
                sIP = packet.getHeader(ip).source();
                sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                dIP = packet.getHeader(ip).destination();
                destIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);
              
                time2 = packet.getCaptureHeader().timestampInMillis();
                if(numPacket == 0 && count == 0)
                    time1 = time2;
                if( numPacket == 0 ) {
                    if (packet.hasHeader(Tcp.ID)) { 
                          packet.getHeader(tcp);
                          ports.add(tcp.destination());
                          ports.add(tcp.source());
                    }
                    sIPflow = sourceIP;
                    dIPflow = destIP;
                  }    
                if( sourceIP.equalsIgnoreCase(dIPflow) && destIP.equalsIgnoreCase(sIPflow) ) {
                    flag = 1; 
                }           
                numPacket += 1;
                lenAll = lenAll + packet.size();
               } 
           }; 
           pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, null);
           pcap.close();
           if( flag == 1 )
               pf += 1;   
           avgPacket = avgPacket + numPacket;
           avglen = avglen + lenAll/(double)numPacket;
           numPacket = 0;
             
           if ( count == 12 ) {
               avglen = avglen/12.0;
               npf = 24 - 2 * pf;                            //non-pair flows
               avgPacket = avgPacket/12.0;
               ppf = (2*pf)/24.0;
               gsf = (0.1*npf)/(time2-time1);
               
               content = Double.toString(avglen) + "," + Double.toString(avgPacket) + "," +
                       Double.toString(ppf) + "," + Double.toString(gsf) + ","+ Double.toString(ports.size()/(1.0*(time2-time1)))
                       + "," + "0";
               ports.clear();
               bw.write( content );
               bw.write('\n');
               count = 0;
               content = "";
               pf = 0;
               avgPacket = 0;
               lenAll = 0;                   
           }     
        }
        bw.close();
    }   
}