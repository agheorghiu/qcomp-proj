package virtualMachine;

import java.util.Random;

import representation.ComplexNumber;
import representation.Qubit;

import exceptions.OverflowException;


/**
 * 
 * Class which defines a basic register. Registers hold 16 qubits by default.
 * 
 * @author Andru
 *
 */
public class Register {
	private static final int REG_SIZE = 16;
	private Qubit[] qubits;
	private ComplexNumber[] amplitudes;
	private String name;
	private int index;
	
	public Register(String name) {
		this.qubits = new Qubit[REG_SIZE];
		this.amplitudes = new ComplexNumber[(int)Math.pow(2, REG_SIZE)];
		this.name = name;
		init();
	}
	
	public Register(String name, int n) {
		this.qubits = new Qubit[n];
		this.amplitudes = new ComplexNumber[(int)Math.pow(2, n)];
		this.name = name;
		init();
	}
	
	public void init() {
		int numAmplitudes = amplitudes.length;
		for (int i = 0; i < qubits.length; i++)
			qubits[i] = new Qubit(new ComplexNumber(1.0, 0.0), new ComplexNumber(0.0, 0.0));
		amplitudes[0] = new ComplexNumber(1.0, 0.0);
		for (int i = 1; i < numAmplitudes; i++)
			amplitudes[i] = new ComplexNumber(0.0, 0.0);
	}
	
	public String getName() {
		return name;
	}
	
	public Qubit[] getContent() {
		Qubit[] copy = new Qubit[qubits.length];
		for (int i = 0; i < qubits.length; i++)
			copy[i] = new Qubit(qubits[i].getFirst(), qubits[i].getSecond());
		return copy;
	}
	
	public ComplexNumber[] getAmplitudes() {
		ComplexNumber[] copy = new ComplexNumber[amplitudes.length];
		for (int i = 0; i < amplitudes.length; i++) {
			copy[i] = new ComplexNumber(amplitudes[i].getRe(), amplitudes[i].getIm());
		}
		return copy;
	}
	
	public void setAmplitudes(ComplexNumber[] amplitudes) throws OverflowException {
		this.amplitudes = amplitudes;
		boolean collapsed = false;
		int index = 0;
		for (int i = 0; i < amplitudes.length; i++) {
			if (amplitudes[i].squaredMagnitude() == 1.0) {
				collapsed = true;
				index = i;
				break;
			}
		}
		if (collapsed) {
			fromDecimal(index);
		}
		else
			qubits = null;
	}
	
	public void setContent(Qubit[] qubits) {
		this.qubits = qubits;
		int index = toDecimal();
		for (int i = 0; i < amplitudes.length; i++) {
			if (i != index)
				amplitudes[i] = new ComplexNumber(0.0, 0.0);
			else
				amplitudes[i] = new ComplexNumber(1.0, 0.0);
		}
	}
	
	public int toDecimal() {
		int value = 0;
		
		for (int i = 0; i < REG_SIZE; i++)
			if (qubits[i].isOne())
				value += Math.pow(2, i);
		
		return value;
	}
	
	public void fromDecimal(int value) throws OverflowException {
		int maxVal = (int) (Math.pow(2, REG_SIZE) - 1);
		int dupVal;
		
		if (value > maxVal || value < 0)
			throw new OverflowException();
		
		qubits = new Qubit[REG_SIZE];
		
		dupVal = value;
		for (int i = 0; i < REG_SIZE; i++) {
			if (dupVal % 2 == 0)
				qubits[i] = new Qubit(new ComplexNumber(1.0, 0.0), new ComplexNumber(0.0, 0.0));
			else
				qubits[i] = new Qubit(new ComplexNumber(0.0, 0.0), new ComplexNumber(1.0, 0.0));
			dupVal /= 2;
		}
	}
	
	public void collapse() throws OverflowException {
		Random generator = new Random();
		int numAmplitudes = amplitudes.length;
		int selected = -1;
		double[] probabilities = new double[numAmplitudes];
		double randomValue = generator.nextDouble(), inf = 0, sup = 0;
		
		for (int i = 0; i < numAmplitudes; i++) {
			inf = sup;
			probabilities[i] = amplitudes[i].squaredMagnitude();
			sup = inf + probabilities[i];
			if (randomValue >= inf && randomValue < sup) {
				selected = i;
				break;
			}
		}
		
		if (selected > -1) {
			fromDecimal(selected);
			setContent(this.qubits);
		}
	}
	
	public double norm() {
		double norm = 0;
		for (int i = 0; i < amplitudes.length; i++)
			norm += amplitudes[i].squaredMagnitude();
		return Math.sqrt(norm);
	}
	
	public void normalize() {
		double normTerm = 1 / norm();
		for (int i = 0; i < amplitudes.length; i++)
			amplitudes[i].timesConstant(normTerm);
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < REG_SIZE; i++) {
			str += qubits[i] + "\n";
		}
		return str;
	}
}
