# DDoS-attacks-on-SDN-Detection

Detecting Distributed Denial of Service Attacks in a Software Defined Network using Self Organising Maps:

According to OpenNetworking.org, Software-Defined Networking (SDN) is an emerging architecture that decouples control plane from forwarding plane resulting in a
network that is dynamic, manageable, cost-effective, and adaptable, making it ideal for the high-bandwidth, dynamic nature of today’s applications.

SDN is a promising way of network management with various advantages but it has its own share of network issues. Due to the centralized nature of the controller, it is
susceptible to various network attacks capable of compromising the whole network just by attacking the controller. DDoS is one such attack which can cause the con-
troller to crash as the switches shall not be able to find matching flows in forwarding table route the packet to controller, thus flooding it with large number of messages
in short duration.

SDN and DDoS attacks:

In SDN, controller controls the packets movement and switches just forward the packet based on flows installed in their forwarding table by the controller. These
switches communicate with the controller through OpenFlow Messages based on OpenFlow protocol. If any new packet arrives that doesnt match the installed
flows, it is sent to controller for processing and further actions. One way of performing DDoS attack on SDN is sending large number of new packets from different
sources or port thus consuming controllers processing capabilities. Unavailability of the controller to process legitimate new packets will breakdown the complete SDN
network. Thus in a reliable SDN,rules/strategies to detect and mitigate DDoS are a must.

USING SELF-ORGANISING MAPS FOR DETECTION:

Entropy based Intrusion detection systems probabilistic methods try to detect the attacks on the basis of specific patterns designed from known attacks. SOM has
the ability to identify patterns even if the dataset is highly noisy. Thus we are proposing a DDoS detection technique based on the Self organizing maps, which
due to its unsupervised nature clustering, has the advantage of detecting unknown attacks too. Efficiency in the detection can be achieved by selecting the most crucial
features in the packets which help in identifying a DDoS attack.
Kohenen Self Organizing Maps [3] is an artificial neural network which transforms a given ndimensional pattern of data into 1 or 2 dimensional topological map/grid.
Here data with similar characteristics are grouped together in region close to one another. It is an unsupervised learning technique as no class labels are provided for
classifying the dataIt is a competition based learning technique because all neurons compete against each other to be placed at output layer, only one winner emerges.
SOM algorithm has been proposed by Kohenen [4]whose algorithm is mentioned briefly below:
  1. Initialization Phase: All neuron vectors weights are initialized with random values in this phase.
  2. Sampling: A single sample is chosen as a representative of the whole neighborhood and fed to neuron grid.
  3. Competition: Decisive factor for winner is taken to be Euclidean distance
                  defined byi(x) = argmin||x − wj||, j = 1, 2, ..., l.
                  Ehere l is the number of neurons in the grid.
  4. Adaptation: After winning neuron is decided, all the neurons adjust their weights. Neighborhood neurons are more affected than far away neurons.
                 Wj (t + 1) = Wj (t) + nj (t)(x(t) − Wj (t)). Where t represents the current instant, n(t) is the learning rate which gradually
                 decreases with time t, and j (t)is the neighborhood function which determines the grade of learning.
  5. Repeat these 4 steps till no more changes are observed in the topological map.
  
  
FEATURE SELECTION AND TRAINING THE SOM:
  For training the SOM, the dataset available in PCap format has been transformed into .arff files using JnetPcap [7] library after extracting required features for input in
the SOM implemented in java ML Library [8]. Flows have been created from packets on the basis of source and destination addresses using pkt2flow library.
Following are the 5 features chosen for classifying the traffic using the analysis shown in [9]-
    (a) Average Number of Packets per flow : Legitimate traffic usually involves large number of packets whereas IP spoofed packets are generally
                                             3 in number.
                                             avg pf = (no of pkts)/(no of flows).
    (b) Average Bytes per flow : Attack traffic has small packet size to increase their efficiency.
                                 avg bytes = (no of rcvdbytes)/no of flows .
    (c) Percentage of Pair-flows : Another characteristic of DDoS attacks is that it increases the number of single flows as reply is not sent to the fake
                                   IPs back P P f = (2 ∗ no of pairflows)/Num of flows.
    (d) Growth of Single Flows : Sudden increase in the single flows indicates a DDoS attack beginning.
                                 GSf = (no of flows − 2 ∗ no of pairflows)/No of flows.
    (e) Growth of Different ports : Port spoofing technique where random ports are generated on same IP can be detected by this feature.
                                     GDp = no of ports/interval.
                                     
                                     
                                     
TESTING THE SOM OFFLINE USING SCAPY TOOL- Scapy is a powerful interactive packet manipulation program. It is able to forge or decode packets of a wide number of protocols, send them on the wire, capture them, match requests and replies, and much more. It can easily handle
most classical tasks like scanning, tracerouting, probing, unit tests, attacks or network discovery.It performs certain tasks that are exclusive to Scapy like
sending invalid frames, injecting your own 802.11 frames, combining technics (VLAN hopping+ARP cache poisoning, VOIP decoding on WEP encrypted
channel. Scapy was used to generate DoS attack packets.Generated pcap files were converted in .arff file after the required features’ extraction

DETECTING ATTACK DURING REALTIME TRAFFIC- Floodlight is an OpenFlow controller based on Java Language. We have partially implemented the testing environment by creating a separate module in floodlight located at net.org.openflowcontroller.DDoSTesting. The controller
fetches switch statistics from all the switches through OFFlowStatisticsRequest object which we are sending every 5 sec. From these aggreagated flowbased switch statistics,required features for comparison are calculated and sent as input to SOM. If SOM classifies the sent input as attack,then the concerned
switch is labelled as ’under attack’. Mininet is a network emulator.A virtual openflow network was created using mininet. Our Topology includes 3 Ovsk type switches,each connected to 2
hosts respectively.


Ref: R. Braga, E. M., and Passito, A. Lightweight ddos
flooding attack detection using nox/openflow. In Local Computer Networks (LCN), 2010 IEEE 35th Conference (2010),
pp. 408–415.
