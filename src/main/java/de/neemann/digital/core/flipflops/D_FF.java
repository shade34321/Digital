package de.neemann.digital.core.flipflops;

import de.neemann.digital.core.BitsException;
import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.part.AttributeKey;
import de.neemann.digital.core.part.Part;
import de.neemann.digital.core.part.PartAttributes;
import de.neemann.digital.core.part.PartTypeDescription;

/**
 * @author hneemann
 */
public class D_FF extends Node implements Part {

    public static final PartTypeDescription DESCRIPTION = new PartTypeDescription(D_FF.class, "D", "C").addAttribute(AttributeKey.Bits);
    private final int bits;
    private ObservableValue dVal;
    private ObservableValue clockVal;
    private ObservableValue q;
    private ObservableValue qn;
    private boolean lastClock;
    private long value;

    public D_FF(PartAttributes attributes) {
        bits = attributes.getBits();
        this.q = new ObservableValue("Q", bits);
        this.qn = new ObservableValue("~Q", bits);
    }

    @Override
    public void readInputs() throws NodeException {
        boolean clock = clockVal.getBool();
        if (clock && !lastClock)
            value = dVal.getValue();
        lastClock = clock;
    }

    @Override
    public void writeOutputs() throws NodeException {
        q.setValue(value);
        qn.setValue(~value);
    }

    @Override
    public void setInputs(ObservableValue... inputs) throws BitsException {
        dVal = inputs[0];
        dVal.addObserver(this);
        clockVal = inputs[1];
        clockVal.addObserver(this);

        if (dVal.getBits() != bits)
            throw new BitsException("wrongBitCount", dVal);

        if (clockVal.getBits() != 1)
            throw new BitsException("clockIsABit", clockVal);
    }

    @Override
    public ObservableValue[] getOutputs() {
        return new ObservableValue[]{q, qn};
    }

}