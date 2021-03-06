package de.neemann.digital.core.memory;

import de.neemann.digital.core.*;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

import static de.neemann.digital.core.ObservableValues.ovs;
import static de.neemann.digital.core.element.PinInfo.input;

/**
 * A simple counter.
 *
 * @author hneemann
 */
public class Counter extends Node implements Element {

    /**
     * The counters {@link ElementTypeDescription}
     */
    public static final ElementTypeDescription DESCRIPTION
            = new ElementTypeDescription(Counter.class, input("C"), input("clr"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.LABEL);

    private final ObservableValue out;
    private final ObservableValue ovf;
    private final int ovfValue;
    private ObservableValue clockIn;
    private ObservableValue clrIn;
    private boolean lastClock;
    private int counter;

    /**
     * Creates a new instance
     *
     * @param attributes the elements attributes
     */
    public Counter(ElementAttributes attributes) {
        super(true);
        int bits = attributes.getBits();
        this.out = new ObservableValue("out", bits);
        this.ovf = new ObservableValue("ovf", 1);
        ovfValue = 1 << bits;
    }

    @Override
    public void readInputs() throws NodeException {
        boolean clock = clockIn.getBool();
        if (clock && !lastClock) {
            counter++;
        }
        lastClock = clock;
        if (clrIn.getBool())
            counter = 0;
    }

    @Override
    public void writeOutputs() throws NodeException {
        if (counter == ovfValue) {
            counter = 0;
            ovf.setValue(1);
        } else
            ovf.setValue(0);

        out.setValue(counter);
    }

    @Override
    public void setInputs(ObservableValues inputs) throws BitsException {
        clockIn = inputs.get(0).addObserverToValue(this).checkBits(1, this);
        clrIn = inputs.get(1).addObserverToValue(this).checkBits(1, this);
    }

    @Override
    public ObservableValues getOutputs() {
        return ovs(out, ovf);
    }

}
