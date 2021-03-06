package de.neemann.digital.core.wiring;

import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.ObservableValues;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * The Driver
 *
 * @author hneemann
 */
public class Driver extends Node implements Element {

    /**
     * The Driver description
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(Driver.class,
            input("in"),
            input("sel"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.FLIP_SEL_POSITON);

    private final ObservableValue output;
    private final int bits;
    private ObservableValue input;
    private ObservableValue selIn;
    private long value;
    private boolean sel;

    /**
     * Creates a new instance
     *
     * @param attributes the attributes
     */
    public Driver(ElementAttributes attributes) {
        bits = attributes.get(Keys.BITS);
        output = new ObservableValue("out", bits, true);
    }

    @Override
    public void readInputs() throws NodeException {
        value = input.getValue();
        sel = selIn.getBool();
    }

    @Override
    public void writeOutputs() throws NodeException {
        output.set(value, isOutHigh(sel));
    }

    /**
     * Returns the highZ state depending of the sel state
     *
     * @param sel the selected input
     * @return the highZ state
     */
    protected boolean isOutHigh(boolean sel) {
        return !sel;
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        input = inputs.get(0).addObserverToValue(this).checkBits(bits, this);
        selIn = inputs.get(1).addObserverToValue(this).checkBits(1, this);
    }

    @Override
    public ObservableValues getOutputs() {
        return output.asList();
    }

}
