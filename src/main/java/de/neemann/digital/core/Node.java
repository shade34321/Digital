package de.neemann.digital.core;

/**
 * Implements a node.
 * A node represents a part in the circuit which was a non zero propagation time.
 * So every gate is a extension of a node.
 * <br/>
 * During the simulation the method {@link #readInputs()} is called.
 * This method has to read all necessary inputs to perform the operation, but is not allowed to update one
 * of the nodes outputs. All necessary data to do so has to be stored in member variables.
 * After all {@link #readInputs()} methods of all nodes are called, the model executer {@link Model#doMicroStep(boolean)}
 * starts to call the {@link #writeOutputs()} methods of all nodes.
 * During this call the outputs have to be updated using the data stored and without reading the inputs again.
 *
 * @author hneemann
 */
public abstract class Node implements Observer {

    private final boolean hasState;
    private Model model;
    private int version;

    /**
     * Creates new stateless Node
     */
    public Node() {
        this(false);
    }

    /**
     * Creates a new node
     *
     * @param hasState true if node has a state
     */
    public Node(boolean hasState) {
        this.hasState = hasState;
    }

    /**
     * Sets the model for this node.
     *
     * @param model the model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void hasChanged() {
        if (model == null)
            throw new RuntimeException("noModelSet");

        if (model.getStepCounter() != version) {
            model.addToUpdateList(this);
            version = model.getStepCounter();
        }
    }

    /**
     * Only read the input!
     * It is not allowed to write to one of the outputs!!!
     *
     * @throws NodeException NodeException
     */
    public abstract void readInputs() throws NodeException;

    /**
     * Only write to the output!
     * It is not allowed to read from one of the inputs!!!
     *
     * @throws NodeException NodeException
     */
    public abstract void writeOutputs() throws NodeException;

    /**
     * Is called to register all the nodes belonging to this node to the model.
     * this implementation simply registers itself to the model.
     *
     * @param model the model
     */
    public void registerNodes(Model model) {
        model.add(this);
    }

    /**
     * @return true if the element has a state and is not only combinatorial
     */
    public boolean hasState() {
        return hasState;
    }

}
