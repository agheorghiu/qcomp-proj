import gates.CNOT;
import gates.Hadamard;
import java.util.Random;
import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import ptolemy.plot.Plot;
import ptolemy.plot.PlotFrame;
import representation.*;

/**
 * 
 * Class to compare the running times of the Hashmap and Array implementations of the register when operators are applied.
 * 
 * @author Andru, Charlie, Sam
 * 
 */

public class RegisterComparison {
    
    /**
     * 
     * Method to fill random nS states of the register to equal probabilities
     * 
     * @param nQ number of qubits
     * @param nS number of states
     * @param a ARegister register implementation
     * @param h QRegister register implementation
     */
    private static void fill(int nQ, int nS, ARegister a, QRegister h){
        
        a.nullifyStates(a.getOneStates(0));
        a.nullifyStates(a.getZeroStates(0));
                        
        h.nullifyStates(h.getOneStates(0));
        h.nullifyStates(h.getZeroStates(0));
        
        int maxStates = 1<<nQ;
        boolean picked[] = new boolean[maxStates];
        
        Random rand = new Random();
        for (int i=1; i<=nS; i++){
            int place = 0;
            
            do{
               place = rand.nextInt(maxStates);
            }while (picked[place]);
            
            picked[place] = true;
            a.setState(place, Complex.one());
            h.setState(place, Complex.one());
        }
        
        a.normalise();
        h.normalise();
    }
    
    public static void main(String[] args) {
        
        int mQ = 15; //maximum number of qubits to be used
        int maxS = 100; //maximum states to be used        
               
        int forms = 50; //how many random forms of register are to be used
        int reps = 20; //how many times each form is to be tested
        
        long times[][][][] = new long[mQ][maxS+1][3][2];
        
        Runtime runtime = Runtime.getRuntime();
        long mem[][] = new long[mQ][maxS+1];
        
        for(int nQ=1; nQ<=mQ; nQ++){     
            
            //set up graphs
            Plot t = new Plot();
            t.setTitle("Plot of processing times for "+(nQ)+" qubits");
            t.setXLabel("Number of non-zero states");
            t.setYLabel("Processing time (ns)");
            
            Plot Mem = new Plot();
            Mem.setTitle("Plot of additional memory in used for "+(nQ)+" qubits");
            Mem.setXLabel("Number of non-zero states");
            Mem.setYLabel("Additional memory in use");
            
            //initial memry usage
            long baseline = runtime.totalMemory();
        
            ARegister a = new ARegister(nQ);
            QRegister h = new QRegister();

            int mS = 1 <<nQ;
            if (mS>maxS)
                mS = maxS;

            for(int nS=0; nS<=mS; nS++){
                
                for (int i=0; i<forms; i++){
                    
                    //fill the master registers to be cloned for each repetition
                    fill(nQ, nS, a, h);
                    
                    for (int j=0; j<reps; j++){
                        
                        //clone registers
                        ARegister A = (ARegister)a.clone();
                        QRegister H = (QRegister)h.clone();

                        //make operators
                        OperatorFactory fa = new OperatorFactory(A);
                        Hadamard ah = (Hadamard)fa.makeOperator("Hadamard");
                        ah.setIndex(nQ-1);

                        OperatorFactory fh = new OperatorFactory(H);
                        Hadamard hh = (Hadamard)fh.makeOperator("Hadamard");
                        hh.setIndex(nQ-1);

                        //apply gates and measure times
                        //times are scaled to when added it gives the average time without having to calculate it afterwards                           
                        long start = System.nanoTime();
                        ah.apply();
                        long finish = System.nanoTime();
                        times[nQ-1][nS][0][0] = times[nQ-1][nS][0][0] + ((finish-start)/(forms*reps));

                        start = System.nanoTime();
                        hh.apply();
                        finish = System.nanoTime();
                        times[nQ-1][nS][0][1] = times[nQ-1][nS][0][1] + ((finish-start)/(forms*reps));
                        
                        A = (ARegister)a.clone();
                        H = (QRegister)h.clone();
                        
                        Projector pa = new Projector(A);
                        Projector ph = new Projector(H);
                        
                        pa.setIndex(nQ-1);
                        ph.setIndex(nQ-1);
                        
                        start = System.nanoTime();
                        pa.apply();
                        finish = System.nanoTime();
                        times[nQ-1][nS][2][0] = times[nQ-1][nS][2][0] + ((finish-start)/(forms*reps));
                        
                        start = System.nanoTime();
                        ph.apply();
                        finish = System.nanoTime();
                        times[nQ-1][nS][2][1] = times[nQ-1][nS][2][1] + ((finish-start)/(forms*reps));
                        
                        //CNOT Gate
                        if (nQ>1){
                            //clone registers
                            A = (ARegister)a.clone(); 
                            H = (QRegister)h.clone();

                            //make operators
                            fa = new OperatorFactory(A); 
                            CNOT ac = (CNOT)fa.makeOperator("CNOT");
                            ac.setIndices(nQ-1, nQ-2);

                            fh = new OperatorFactory(H);
                            CNOT hc = (CNOT)fh.makeOperator("CNOT");
                            hc.setIndices(nQ-1, nQ-2);

                            //apply gates and measure times
                            //times are scaled to when added it gives the average time without having to calculate it afterwards
                            start = System.nanoTime();
                            ac.apply();
                            finish = System.nanoTime();
                            times[nQ-1][nS][1][0] = times[nQ-1][nS][1][0] + ((finish-start)/(forms*reps));

                            start = System.nanoTime();
                            hc.apply();
                            finish = System.nanoTime();
                            times[nQ-1][nS][1][1] = times[nQ-1][nS][1][1] + ((finish-start)/(forms*reps));
                        }
                            
                    }//finish loop over repeats
                }//finish loop over forms
                
                mem[nQ-1][nS] = runtime.totalMemory()-baseline;
                
                //add points to graph
                t.addPoint(0, nS, times[nQ-1][nS][0][0], true);
                t.addPoint(1, nS, times[nQ-1][nS][0][1], true);
                t.addPoint(2, nS, times[nQ-1][nS][1][0], true);
                t.addPoint(3, nS, times[nQ-1][nS][1][1], true);
                t.addPoint(4, nS, times[nQ-1][nS][2][0], true);
                t.addPoint(5, nS, times[nQ-1][nS][2][1], true);
                                
                Mem.addPoint(0, nS, mem[nQ-1][nS], true);
                
            }//finish loop over states  
            
            //finish graphs
            t.addLegend(0, "Hadamard- Array");
            t.addLegend(1, "Hadamard- Hashmap");
            t.addLegend(2, "CNOT- Array");
            t.addLegend(3, "CNOT- Hashmap");
            t.addLegend(4, "Projector- Array");
            t.addLegend(5, "Projector- Hashmap");
            
            PlotFrame ft = new PlotFrame("Comparison", t);
            ft.setSize(800, 600);
            ft.setVisible(true);
            
            PlotFrame fm = new PlotFrame("Memory Usage", Mem);
            fm.setSize(800, 600);
            fm.setVisible(true);
                        
        }//finish loop over qubits       
                
    }
}
