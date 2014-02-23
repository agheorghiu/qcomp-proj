
import java.util.Random;

import representation.Complex;
import representation.IRegister;

/**
 *
 * @author Andru, Charlie, Sam
 */
public class RegisterComparison {

    /**
     * 
     * Method which takes in a ARegister object and a QRegister object using nQubits qubits and fills them in a given way.
     * Once filled, they can be cloned and kept as master copies while the clones are used to test the running time of different gates on the different registers.
     * 
     * @param nQubits number of qubits being worked with. Should be the same number of qubits that the areg was initialsed with.
     * @param density the fraction of the total number of possible states to be filled
     * @param areg the ARegister object
     * @param hreg the QRegister object
     * @param type how the registers are to be filled- 0: first nStates*density states will be filled; 1: nStates*density regularily spaced states will be filled; 2: nStates*density random states will be filled
     */
    private static void fillMasterRegs(int nQubits, double density, IRegister areg, IRegister hreg, int type){
        
        //find nicer way to do this!
        areg.nullifyStates(areg.getOneStates(0));
        areg.nullifyStates(areg.getZeroStates(0));
                        
        hreg.nullifyStates(hreg.getOneStates(0));
        hreg.nullifyStates(hreg.getZeroStates(0));
                        
        int nStates = 1 << nQubits;
        int fill = (int)((double)(nStates)*density);
        
        if (type == 0){ //fill the first fill states with equal amplitudes
            for (int i=0; i < fill; i++){
                areg.setState(i, Complex.one());
                hreg.setState(i, Complex.one());
            }
            areg.normalise();
            hreg.normalise();
        }
        
        else if (type == 1){ //fill regularily spaced states with equal amplitudes
            if (fill == 0) return;
            int spacing = nStates/fill;
            int i=0;
            while (i < nStates){
                areg.setState(i, Complex.one());
                hreg.setState(i, Complex.one());
                i += spacing;
            }
            areg.normalise();
        }
        
        else if (type == 2){ //fill random states with equal amplitudes
            boolean alreadyPicked[] = new boolean[nStates];
                        
            Random rand = new Random();
            
            for (int i=0; i<fill; i++){
                int place = 0;
                do
                    place = rand.nextInt(nStates);                    
                while (alreadyPicked[place]);
                alreadyPicked[place] = true;
                areg.setState(place, Complex.one()); 
                hreg.setState(place, Complex.one());
            }
            hreg.normalise();       
            areg.normalise();
        }
                            
    }
    
/*    
    public static void main(String[] args) {        
         
        int maxQ = 10;
        int numRandForms = 100;
        int rep = 20;
        
        long times[][][][][] = new long [2][maxQ][100][numRandForms][rep];
        
        for (int nQ = 1; nQ <= maxQ; nQ++){
            
            for (int d = 1; d <= 100; d++){
                
                for (int j = 0; j < numRandForms; j++){
                
                    ARegister areg = new ARegister(nQ);
                    QRegister hreg = new QRegister();

                    fillMasterRegs(nQ, (0.01*(double)d), areg, hreg, 2);

                    for (int i = 0; i < rep; i++){

                        ARegister A = (ARegister)(areg.clone());                    
                        OperatorFactory factA = new OperatorFactory(A);
                        Hadamard hA = (Hadamard)factA.makeOperator("Hadamard");
                        hA.setIndex(nQ-1);
                    
                        QRegister H = (QRegister)(hreg.clone());                    
                        OperatorFactory factH = new OperatorFactory(H);
                        Hadamard hH = (Hadamard)factH.makeOperator("Hadamard");
                        hH.setIndex(nQ-1);
                        
                        long aStart = System.nanoTime();
                        hA.apply();
                        long aFinish = System.nanoTime();
                        
                        long hStart = System.nanoTime();
                        hH.apply();
                        long hFinish = System.nanoTime();
                        
                        times[0][nQ-1][d-1][j][i] = aFinish-aStart; //time taken by array
                        times[1][nQ-1][d-1][j][i] = hFinish-hStart; //time taken by hash-map
                        
                    }
                }
            }
        } //finish looping over number of qubits
        
        double AvTime [][][] = new double [2][maxQ][100];
        
        for (int type = 0; type <= 1; type++){
            for (int nQ = 1; nQ <= maxQ; nQ++){
                for (int d = 1; d <= 100; d++){
                    
                    long tTotal = 0;
                    for (int i=0; i<numRandForms; i++){
                        for (int j=0; j<rep; j++){
                            tTotal = tTotal + times[type][nQ-1][d-1][i][j];
                        }
                    }
                    
                    AvTime[type][nQ-1][d-1] = (double)tTotal/(numRandForms*rep);
                    
                }
            }
        }//finish calculating average times        
               
        for (int i=0; i<maxQ; i++){
            
            Plot plot = new Plot();
            plot.setTitle("Plot of Hadamard processing time for "+(i+1)+" qubits");
            plot.setXLabel("Density of non-zero states");
            plot.setYLabel("Processing time (ns)");
            
            for (int j=0; j<100; j++){
                plot.addPoint(0, 0.01*(double)j, AvTime[0][i][j], true); 
                plot.addPoint(1, 0.01*(double)j, AvTime[1][i][j], true);
            }
            
            plot.addLegend(0, "Array implementation");
            plot.addLegend(1, "Hash-map implementation");
            
            PlotFrame frame = new PlotFrame("Comparison of Hash-map and Array implementations", plot);
            frame.setSize(800, 600);
            frame.setVisible(true);
        }
        
        Plot Switch = new Plot();
        Switch.setTitle("Plot of the percentage of non-zero states for which the Array Implementation becomes more efficient");
        Switch.setXLabel("Number of Qubits");
        Switch.setYLabel("Percentage");
        
        for (int i=0; i<maxQ; i++){
            System.out.println("\n\n ............... \n\n"+(i+1)+" Qubits:\n\n");
            System.out.println("\n ------Array Implementation------\n");
            for (int k=0; k<100; k++)
                System.out.println(AvTime[0][i][k]);
            System.out.println("\n ------Hash-map Implementation------\n");               
            for (int k=0; k<100; k++)
                System.out.println(AvTime[1][i][k]);
        }
    }
*/
}