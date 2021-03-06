package de.neemann.digital.integration;

import de.neemann.digital.draw.elements.Circuit;
import de.neemann.digital.draw.graphics.Export;
import de.neemann.digital.draw.graphics.GraphicsImage;
import de.neemann.digital.draw.library.ElementLibrary;
import de.neemann.digital.draw.shapes.ShapeFactory;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author hneemann
 */
public class TestShapes extends TestCase {

    /**
     * Loads a circuit with all available elements and writes it
     * to a PNG.
     *
     * @throws Exception
     */
    public void testShapes() throws Exception {
        File filename = new File(Resources.getRoot(), "dig/shapes.dig");
        ElementLibrary library = new ElementLibrary();
        ShapeFactory shapeFactory = new ShapeFactory(library);
        Circuit circuit = Circuit.loadCircuit(filename, shapeFactory);

        // try to write circuit to graphics instance
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Export(circuit,
                (out, min, max) -> GraphicsImage.create(out, min, max, "PNG", 1))
                .export(baos);

        assertTrue(baos.size() > 30000);

    }
}
