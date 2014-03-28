import gates.CNOT;
import gates.Hadamard;
import java.util.Random;
import operators.Operator;
import operators.OperatorFactory;
import ptolemy.plot.Plot;
import ptolemy.plot.PlotFrame;
import representation.*;

public class RegisterComparison {
    
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
        
        int mQ = 32;
        int maxS = 10;        
               
        int forms = 50;
        int reps = 20;
        
        long times[][][][] = new long[mQ][maxS+1][2][2];
        
        Runtime runtime = Runtime.getRuntime();
        long mem[][] = new long[mQ][maxS+1];
        
        for(int nQ=1; nQ<=mQ; nQ++){     
            
            Plot t = new Plot();
            t.setTitle("Plot of processing times for "+(nQ)+" qubits");
            t.setXLabel("Number of non-zero states");
            t.setYLabel("Processing time (ns)");
            
            Plot Mem = new Plot();
            Mem.setTitle("Plot of additional memory in used for "+(nQ)+" qubits");
            Mem.setXLabel("Number of non-zero states");
            Mem.setYLabel("Additional memory in use");
            
            long baseline = runtime.totalMemory();
        
            ARegister a = new ARegister(nQ);
            QRegister h = new QRegister();

            int mS = 1 <<nQ;
            if (mS>maxS)
                mS = maxS;

            for(int nS=0; nS<=mS; nS++){
                
                for (int i=0; i<forms; i++){
                    fill(nQ, nS, a, h);
                    for (int j=0; j<reps; j++){
                        ARegister A = (ARegister)a.clone();
                        QRegister H = (QRegister)h.clone();

                        OperatorFactory fa = new OperatorFactory(A);
                        Hadamard ah = (Hadamard)fa.makeOperator("Hadamard");
                        ah.setIndex(nQ-1);

                        OperatorFactory fh = new OperatorFactory(H);
                        Hadamard hh = (Hadamard)fh.makeOperator("Hadamard");
                        hh.setIndex(nQ-1);

                        long start = System.nanoTime();
                        ah.apply();
                        long finish = System.nanoTime();
                        times[nQ-1][nS][0][0] = times[nQ-1][nS][0][0] + ((finish-start)/(forms*reps));

                        start = System.nanoTime();
                        hh.apply();
                        finish = System.nanoTime();
                        times[nQ-1][nS][0][1] = times[nQ-1][nS][0][1] + (finish-start);
                        
                        if (nQ>1){
                            A = (ARegister)a.clone();
                            H = (QRegister)h.clone();

                            fa = new OperatorFactory(A);
                            CNOT ac = (CNOT)fa.makeOperator("CNOT");
                            ac.setIndices(nQ-1, nQ-2);

                            fh = new OperatorFactory(H);
                            CNOT hc = (CNOT)fh.makeOperator("CNOT");
                            hc.setIndices(nQ-1, nQ-2);

                            start = System.nanoTime();
                            ac.apply();
                            finish = System.nanoTime();
                            times[nQ-1][nS][1][0] = times[nQ-1][nS][1][0] + ((finish-start)/(forms*reps));

                            start = System.nanoTime();
                            hc.apply();
                            finish = System.nanoTime();
                            times[nQ-1][nS][1][1] = times[nQ-1][nS][1][1] + (finish-start);
                        }
                            
                    }//finish loop over repeats
                }//finish loop over forms
                
                mem[nQ-1][nS] = runtime.totalMemory()-baseline;
                
                t.addPoint(0, nS, times[nQ-1][nS][0][0], true);
                t.addPoint(1, nS, times[nQ-1][nS][0][1], true);
                t.addPoint(2, nS, times[nQ-1][nS][1][0], true);
                t.addPoint(3, nS, times[nQ-1][nS][1][1], true);
                                
                Mem.addPoint(0, nS, mem[nQ-1][nS], true);
                
            }//finish loop over states  
            
            t.addLegend(0, "Hadamard- Array");
            t.addLegend(1, "Hadamard- Hashmap");
            t.addLegend(2, "CNOT- Array");
            t.addLegend(3, "CNOT- Hashmap");
            
            PlotFrame ft = new PlotFrame("Comparison", t);
            ft.setSize(800, 600);
            ft.setVisible(true);
            
            PlotFrame fm = new PlotFrame("Memory Usage", Mem);
            fm.setSize(800, 600);
            fm.setVisible(true);
                        
        }//finish loop over qubits       
                
    }
}
