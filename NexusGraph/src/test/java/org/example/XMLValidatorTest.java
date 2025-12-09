package org.example;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class XMLValidatorTest {

    @Test
    void testValidXML() {
        HashMap<Integer, String> xml = new HashMap<>();
        xml.put(1, "<root>");
        xml.put(2, "    <user>");
        xml.put(3, "        <id>1</id>");
        xml.put(4, "        <name>John</name>");
        xml.put(5, "    </user>");
        xml.put(6, "</root>");

        XMLValidator validator = new XMLValidator(xml);
        validator.validate();
        assertTrue(validator.errors.isEmpty(), "No errors should be found in valid XML");
    }

    @Test
    void testMismatchedClosingTag() {
        HashMap<Integer, String> xml = new HashMap<>();
        xml.put(1, "<root>");
        xml.put(2, "    <user>");
        xml.put(3, "        <id>1</id>");
        xml.put(4, "        <name>John</name>");
        xml.put(5, "    </usr>");  // Wrong closing tag
        xml.put(6, "</root>");

        XMLValidator validator = new XMLValidator(xml);
        validator.validate();
        assertFalse(validator.errors.isEmpty(), "Errors should be found for mismatched closing tag");
        assertTrue(validator.errors.containsValue("Tag mismatch expected</user>"));
    }

    @Test
    void testMissingClosingTag() {
        HashMap<Integer, String> xml = new HashMap<>();
        xml.put(1, "<root>");
        xml.put(2, "    <user>");
        xml.put(3, "        <id>1</id>");
        xml.put(4, "        <name>John</name>");
        xml.put(5, "    </user>");
        // Missing </root>

        XMLValidator validator = new XMLValidator(xml);
        validator.validate();
        assertFalse(validator.errors.isEmpty(), "Errors should be found for missing closing tag");
        assertTrue(validator.errors.containsValue("expecting closing tag </root>"));
    }

    @Test
    void testClosingWithoutOpening() {
        HashMap<Integer, String> xml = new HashMap<>();
        xml.put(1, "</root>");  // No opening <root>
        xml.put(2, "<user>");
        xml.put(3, "    <id>1</id>");
        xml.put(4, "</user>");

        XMLValidator validator = new XMLValidator(xml);
        validator.validate();
        assertFalse(validator.errors.isEmpty(), "Errors should be found for closing tag without opening");
        assertTrue(validator.errors.containsValue("wrong closing tag for unopened tag"));
    }

    @Test
    void testNestedMismatch() {
        HashMap<Integer, String> xml = new HashMap<>();
        xml.put(1, "<root>");
        xml.put(2, "    <user>");
        xml.put(3, "        <id>1</id>");
        xml.put(4, "    </root>");  // Wrong closing for <user>
        xml.put(5, "    </user>");  // Extra closing

        XMLValidator validator = new XMLValidator(xml);
        validator.validate();
        System.out.println(validator.errors);

        assertTrue(validator.errors.containsValue("Tag mismatch expected</user>"));
        assertTrue(validator.errors.containsValue("Tag mismatch expected</root>"));
    }
}
