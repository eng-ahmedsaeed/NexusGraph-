package org.example;

import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {
        // HashMap<Integer, String> xml1 = new HashMap<>();
//
//        xml1.put(1, "<root>");
//        xml1.put(2, "    <name>Ahmed</name>");
//        xml1.put(3, "    <age>21</age>");
//        xml1.put(4, "</root>");
        // XMLValidator validator = new XMLValidator(xml1);
//        validator.validate();
//         validator.PrintErrors();
//        validator.PrintFixes();
//        HashMap<Integer, String> fixedXML = validator.applyFixes();
//        System.out.println("Fixed XML:");
//        for (int i = 1; i <= fixedXML.size(); i++) {
//            System.out.println(fixedXML.get(i));
//        }
//        HashMap<Integer, String> xml3 = new HashMap<>();
//
//
//        xml3.put(1, "<root>");
//        xml3.put(2, "<name>Ahmed</name>");
//        xml3.put(3, "</age>"); // Mismatched closing tag
//        xml3.put(4, "</root>");
//        validator.XMLSetter(xml3);
//      validator.validate();
//       validator.PrintErrors();
//        validator.PrintFixes();
//        HashMap<Integer, String> fixedXML = validator.applyFixes();
//        System.out.println("Fixed XML:");
//        for (int i = 1; i <= fixedXML.size(); i++) {
//            System.out.println(fixedXML.get(i));
//        }

//        HashMap<Integer, String> xml6 = new HashMap<>();
//
//        xml6.put(1, "<root>");
//        xml6.put(2, "    </name>"); // Extra closing tag
//        xml6.put(3, "</root>");
//        validator.XMLSetter(xml6);
//        validator.validate();
//        validator.PrintErrors();
//        validator.PrintFixes();
//        HashMap<Integer, String> fixedXML = validator.applyFixes();
//        System.out.println("Fixed XML:");
//        for (int i = 1; i <= fixedXML.size(); i++) {
//            System.out.println(fixedXML.get(i));
//        }


//        HashMap<Integer, String> xmlLines = new HashMap<>();
//
//        xmlLines.put(1, "<users>");
//        xmlLines.put(2, "    <user>");
//        xmlLines.put(3, "        <id>1</id>");
//        xmlLines.put(4, "        <name>user1</name>");
//        xmlLines.put(5, "        <posts>");
//        xmlLines.put(6, "            <post>");
//        xmlLines.put(7, "                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod");
//        xmlLines.put(8, "                tempor incididunt ut labore et dolore magna aliqua.");
//        xmlLines.put(9, "            </post>");
//        xmlLines.put(10, "            <post>");
//        xmlLines.put(11, "                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi");
//        xmlLines.put(12, "                ut aliquip ex ea commodo consequat.");
//        xmlLines.put(13, "            </post>");
//        xmlLines.put(14, "        </posts>");
//        xmlLines.put(15, "        <followers>");
//        xmlLines.put(16, "            <follower>");
//        xmlLines.put(17, "                <name>2</id>");
//        xmlLines.put(18, "            </follower>");
//        xmlLines.put(19, "            <follower>");
//        xmlLines.put(20, "                <id>4</id>");
//        xmlLines.put(21, "            </follower>");
//        xmlLines.put(22, "    </user>");
//        xmlLines.put(23, "</users>");
//        validator.XMLSetter(xmlLines);
//        validator.validate();
//        validator.PrintErrors();
//        validator.PrintFixes();
//        HashMap<Integer, String> fixedXML = validator.applyFixes();
//        System.out.println("Fixed XML:");
//        for (int i = 1; i <= fixedXML.size(); i++) {
//            System.out.println(fixedXML.get(i));
//        }

        // System.out.println("XML validated successfully");

        // System.out.println("\n========== TEST 1: VALID XML ==========");

//        HashMap<Integer, String> xml1 = new HashMap<>();
//        xml1.put(1, "<root>");
//        xml1.put(2, "  <name>Ahmed</name>");
//        xml1.put(3, "</root>");
//
//        XMLValidator validator = new XMLValidator(xml1);
//        validator.validate();
//        validator.PrintErrors();
//        validator.PrintFixes();
//        HashMap<Integer, String> fixedXML = validator.applyFixes();
//        System.out.println("Fixed XML:");
//        for (int i = 1; i <= fixedXML.size(); i++) {
//            System.out.println(fixedXML.get(i));
//}

//
//        /// /////////////////////////////////testing the graph2phot//////////////////
//        ArrayList<Pair<String, String>> edges = new ArrayList<>();
//        edges.add(new Pair<>("Alice", "Bob"));
//        edges.add(new Pair<>("Alice", "Charlie"));
//        edges.add(new Pair<>("Alice", "Diana"));
//        edges.add(new Pair<>("Alice", "Eve"));
//
//        edges.add(new Pair<>("Bob", "Alice"));
//        edges.add(new Pair<>("Bob", "Charlie"));
//        edges.add(new Pair<>("Bob", "Frank"));
//        edges.add(new Pair<>("Bob", "Grace"));
//
//        edges.add(new Pair<>("Charlie", "Alice"));
//        edges.add(new Pair<>("Charlie", "Bob"));
//        edges.add(new Pair<>("Charlie", "Heidi"));
//        edges.add(new Pair<>("Charlie", "Ivan"));
//
//        edges.add(new Pair<>("Diana", "Alice"));
//        edges.add(new Pair<>("Diana", "Eve"));
//        edges.add(new Pair<>("Diana", "Judy"));
//
//        edges.add(new Pair<>("Eve", "Alice"));
//        edges.add(new Pair<>("Eve", "Frank"));
//        edges.add(new Pair<>("Eve", "Mallory"));
//
//// Secondary cluster
//        edges.add(new Pair<>("Frank", "Bob"));
//        edges.add(new Pair<>("Frank", "Grace"));
//        edges.add(new Pair<>("Frank", "Heidi"));
//
//        edges.add(new Pair<>("Grace", "Bob"));
//        edges.add(new Pair<>("Grace", "Frank"));
//        edges.add(new Pair<>("Grace", "Ivan"));
//
//        edges.add(new Pair<>("Heidi", "Charlie"));
//        edges.add(new Pair<>("Heidi", "Frank"));
//        edges.add(new Pair<>("Heidi", "Judy"));
//
//        edges.add(new Pair<>("Ivan", "Charlie"));
//        edges.add(new Pair<>("Ivan", "Grace"));
//        edges.add(new Pair<>("Ivan", "Kevin"));
//
//        edges.add(new Pair<>("Judy", "Diana"));
//        edges.add(new Pair<>("Judy", "Heidi"));
//        edges.add(new Pair<>("Judy", "Laura"));
//
//// Third cluster
//        edges.add(new Pair<>("Kevin", "Ivan"));
//
//        edges.add(new Pair<>("Kevin", "Niaj"));
//
//        edges.add(new Pair<>("Laura", "Judy"));
//        edges.add(new Pair<>("Laura", "Mallory"));
//        edges.add(new Pair<>("Laura", "Olivia"));
//
//        edges.add(new Pair<>("Mallory", "Eve"));
//        edges.add(new Pair<>("Mallory", "Kevin"));
//        edges.add(new Pair<>("Mallory", "Peggy"));
//
//
//        edges.add(new Pair<>("Niaj", "Olivia"));
//        edges.add(new Pair<>("Niaj", "Rupert"));
//
//        edges.add(new Pair<>("Olivia", "Laura"));
//
//        edges.add(new Pair<>("Olivia", "Sybil"));
//
//// Peripheral but connected
//        edges.add(new Pair<>("Peggy", "Mallory"));
//
//        edges.add(new Pair<>("Rupert", "Niaj"));
//        edges.add(new Pair<>("Rupert", "Trent"));
//
//        edges.add(new Pair<>("Sybil", "Olivia"));
//        edges.add(new Pair<>("Sybil", "Victor"));
//
//        edges.add(new Pair<>("Trent", "Peggy"));
//        edges.add(new Pair<>("Trent", "Rupert"));
//
//
//        edges.add(new Pair<>("Victor", "Sybil"));
//
//
//        edges.add(new Pair<>("Walter", "Trent"));
//
//
//        Graph2Photo g = new Graph2Photo(edges);
//        g.Graph2Photoprint("png");
//
//        }
    }
}
