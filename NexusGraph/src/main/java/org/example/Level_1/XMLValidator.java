package org.example.Level_1;

import java.util.HashMap;
import java.util.Stack;

class TagInfo {
    String tag;
    int line;

    TagInfo(String tag, int line) {
        this.tag = tag;
        this.line = line;
    }
}

public class XMLValidator {

    private Stack<TagInfo> stack = new Stack<>();
    public HashMap<Integer, String> errors = new HashMap<>();
    public HashMap<Integer, String> fixerrors = new HashMap<>();
    private HashMap<Integer, String> XML = new HashMap<>();
    private int nextFixLine; // Track line number for missing closing tags

    static final private String openTagRegex = "^[a-zA-Z_:][a-zA-Z0-9_.:-]*$";
    static final private String closeTagRegex = "^/[a-zA-Z_:][a-zA-Z0-9_.:-]*$";
    static final private String textRegex = "^[^</>]*$";

    public XMLValidator(HashMap<Integer, String> XML) {
        this.XML = XML;
    }

    public void XMLSetter(HashMap<Integer, String> XML) {
        this.XML = XML;
    }

    public void validate() {
        stack.clear();
        errors.clear();
        fixerrors.clear();
        nextFixLine = XML.size() + 1;

        String s;
        boolean tagFlag = false;
        int index;
        int i;

        StringBuilder tag = new StringBuilder("");
        StringBuilder text = new StringBuilder("");

        for (i = 1; i <= XML.size(); i++) {

            text.setLength(0);
            tag.setLength(0);
            index = 0;

            s = XML.get(i);
            if (s == null) continue;
            
            StringBuilder sb = new StringBuilder(s);
            sb.append('\n');
            String lineOpenedTag = null;
            boolean hasContentAfterTag = false;

            while (sb.charAt(index) != '\n') {

                if (sb.charAt(index) == '<') {
                    tagFlag = true;
                    if (text.length() > 0 && text.toString().trim().length() > 0) {
                        hasContentAfterTag = true;
                    }
                    textChecker(text, i);
                    text.setLength(0);
                    index++;
                    continue;
                }

                else if (sb.charAt(index) == '>') {
                    tagFlag = false;
                    String currentTag = tag.toString();
                    boolean skipTagChecker = false;
                    
                    if (!currentTag.startsWith("/")) {
                        if (lineOpenedTag != null && hasContentAfterTag && !currentTag.equals(lineOpenedTag)) {
                            String wrongOpeningTag = "<" + currentTag + ">";
                            String correctClosingTag = "</" + lineOpenedTag + ">";
                            errors.put(i, "Wrong tag " + wrongOpeningTag + " - expected " + correctClosingTag + " (forgot '/'?)");
                            String currentLine = fixerrors.containsKey(i) ? fixerrors.get(i) : XML.get(i);
                            if (currentLine != null) {
                                String fixedLine = currentLine.replace(wrongOpeningTag, correctClosingTag);
                                fixerrors.put(i, fixedLine);
                            }
                            if (!stack.isEmpty() && stack.peek().tag.equals(lineOpenedTag)) {
                                stack.pop();
                            }
                            lineOpenedTag = null;
                            hasContentAfterTag = false;
                            skipTagChecker = true; // Already handled, don't process through tagChecker
                        } else {
                            lineOpenedTag = currentTag;
                            hasContentAfterTag = false;
                        }
                    } else {
                        String closingName = currentTag.substring(1);
                        if (lineOpenedTag != null && lineOpenedTag.equals(closingName)) {
                            lineOpenedTag = null; // Properly closed on same line
                        } else if (lineOpenedTag != null && hasContentAfterTag && !closingName.equals(lineOpenedTag)) {
                            String wrongClosingTag = "</" + closingName + ">";
                            String correctClosingTag = "</" + lineOpenedTag + ">";
                            errors.put(i, "Wrong closing tag " + wrongClosingTag + " - expected " + correctClosingTag);
                            String currentLine = fixerrors.containsKey(i) ? fixerrors.get(i) : XML.get(i);
                            if (currentLine != null) {
                                String fixedLine = currentLine.replace(wrongClosingTag, correctClosingTag);
                                fixerrors.put(i, fixedLine);
                            }
                            if (!stack.isEmpty() && stack.peek().tag.equals(lineOpenedTag)) {
                                stack.pop();
                            }
                            
                            lineOpenedTag = null;
                            hasContentAfterTag = false;
                            skipTagChecker = true; // Already handled
                        }
                    }
                    
                    if (!skipTagChecker) {
                        tagChecker(tag, i);
                    }
                    tag.setLength(0);
                    index++;
                    continue;
                }

                if (tagFlag) {
                    tag.append(sb.charAt(index));
                } else {
                    text.append(sb.charAt(index));
                    if (lineOpenedTag != null && !text.toString().trim().isEmpty()) {
                        hasContentAfterTag = true;
                    }
                }
                index++;
            }
            if (lineOpenedTag != null && hasContentAfterTag) {
                String currentLine = fixerrors.containsKey(i) ? fixerrors.get(i) : XML.get(i);
                if (currentLine != null && !currentLine.contains("</" + lineOpenedTag + ">")) {
                    errors.put(i, "Unclosed tag <" + lineOpenedTag + "> (missing </" + lineOpenedTag + ">)");
                    fixerrors.put(i, currentLine + "</" + lineOpenedTag + ">");
                    if (!stack.isEmpty() && stack.peek().tag.equals(lineOpenedTag)) {
                        stack.pop();
                    }
                }
            }
        }
        stackChecker();
    }

    private void tagChecker(StringBuilder tag, int index) {
        String originalTag = tag.toString();
        String isClosing = "";
        if (originalTag.startsWith("/")) {
            isClosing = "/";
            originalTag = originalTag.substring(1);
        }

        String fixedTagName = originalTag.replaceFirst("^\\d+", "");
        if (!fixedTagName.equals(originalTag)) {
            String fixedLine = XML.get(index);
            if (fixedLine.contains("<")) {
                int start = fixedLine.indexOf("<");
                int end = fixedLine.indexOf(">");
                fixedLine = fixedLine.substring(0, start+1) + (isClosing.equals("/") ? "/" : "") + fixedTagName + fixedLine.substring(end);
            } else {
                fixedLine = "<" + (isClosing.equals("/") ? "/" : "") + fixedTagName + ">";
            }
            fixerrors.put(index, fixedLine);
            errors.put(index, "Invalid tag format, removed leading numbers: " + tag);
            tag = new StringBuilder((isClosing.equals("/") ? "/" : "") + fixedTagName);
        }

        if (tag.toString().matches(openTagRegex)) {
            stackPush(tag, true, index);
        } else if (tag.toString().matches(closeTagRegex)) {
            stackPush(tag, false, index);
        } else {
            errors.put(index, "The tag " + tag + " has invalid format");
        }
    }

    private void textChecker(StringBuilder text, int index) {

        if (text.toString().matches(textRegex)) {
            return;
        }
        errors.put(index, "The text " + text + " contains invalid characters (< or > or /)");
    }

    private void stackPush(StringBuilder tag, boolean openTag, int index) {

        if (openTag) {
            String tagName = tag.toString();
            if (!stack.isEmpty() && stack.peek().tag.equals(tagName)) {
                handleSiblingAutoClose(stack.peek().tag, index);
            }
            else if (!stack.isEmpty()) {
                String currentLine = XML.get(index);
                String stackTopLine = XML.get(stack.peek().line);
                
                if (currentLine != null && stackTopLine != null) {
                    int currentIndent = extractIndent(currentLine).length();
                    int stackTopIndent = extractIndent(stackTopLine).length();
                    if (currentIndent == stackTopIndent && currentIndent > 0) {
                        handleSiblingAutoClose(stack.peek().tag, index);
                    }
                }
            }
            stack.push(new TagInfo(tagName, index));
        } else {
            String tagName = tag.toString().substring(1);
            handleClosingTag(tagName, index);
        }
    }

    
    private void handleSiblingAutoClose(String unclosedTagName, int index) {
        TagInfo previousSibling = stack.pop();
        String missingCloser = "</" + unclosedTagName + ">";
        
        errors.put(previousSibling.line, "Unclosed tag <" + unclosedTagName + "> (missing " + missingCloser + ")");
        String currentLine = fixerrors.containsKey(index) ? fixerrors.get(index) : XML.get(index);
        if (currentLine != null) {
            String indent = extractIndent(currentLine);
            fixerrors.put(index, indent + missingCloser + "\n" + currentLine);
        }
    }

    private void stackChecker() {
        java.util.List<TagInfo> unclosedTags = new java.util.ArrayList<>();
        while (!stack.isEmpty()) {
            unclosedTags.add(stack.pop());
        }
        
        int currentInsertLine = nextFixLine;
        for (TagInfo tag : unclosedTags) {
            errors.put(tag.line, "Unclosed tag <" + tag.tag + "> (missing </" + tag.tag + ">)");
            String closingTag = "</" + tag.tag + ">";
            String indent = extractIndent(XML.get(tag.line));
            fixerrors.put(currentInsertLine, indent + closingTag);
            currentInsertLine++;
        }
        nextFixLine = currentInsertLine;
    }

    
    private void handleClosingTag(String tagName, int index) {
        String closingTag = "</" + tagName + ">";
        String openingTag = "<" + tagName + ">";
        if (!stack.isEmpty() && stack.peek().tag.equals(tagName)) {
            stack.pop();
            return;
        }
        if (!stack.isEmpty() && isTagInStack(tagName)) {
            handleMissingClosers(tagName, closingTag, index);
            return;
        }
        if (!stack.isEmpty() && hasContentBetweenOpenerAndCloser(index)) {
            handleMissingOpenerWithStack(closingTag, openingTag, index);
            return;
        }
        if (!stack.isEmpty()) {
            handleTypoFix(tagName, closingTag, index);
            return;
        }
        if (isDuplicateOrphan(closingTag, openingTag, index)) {
            handleOrphanRemoval(closingTag, index);
            return;
        }
        handleMissingOpener(closingTag, openingTag, index);
    }

    
    private boolean hasContentBetweenOpenerAndCloser(int closerIndex) {
        if (stack.isEmpty()) return false;
        
        TagInfo lastOpener = stack.peek();
        int openerLine = lastOpener.line;
        for (int line = openerLine + 1; line < closerIndex; line++) {
            String content = XML.get(line);
            if (content == null) continue;
            
            String trimmed = content.trim();
            if (trimmed.isEmpty()) continue;
            if (!trimmed.isEmpty()) {
                return true;
            }
        }
        String closerLine = XML.get(closerIndex);
        if (closerLine != null) {
            int closerPos = closerLine.indexOf("</");
            if (closerPos > 0) {
                String beforeCloser = closerLine.substring(0, closerPos).trim();
                if (!beforeCloser.isEmpty() && !beforeCloser.endsWith(">")) {
                    return true; // Text content before closer
                }
            }
        }
        
        return false;
    }

    
    private void handleMissingOpenerWithStack(String closingTag, String openingTag, int index) {
        errors.put(index, "Missing opening tag " + openingTag + " - inserting before content");
        String tagName = openingTag.substring(1, openingTag.length() - 1);
        String singularChildName = tagName.endsWith("s") ? tagName.substring(0, tagName.length() - 1) : null;
        String closingLine = XML.get(index);
        String closingIndent = extractIndent(closingLine);
        int closingIndentLen = closingIndent.length();
        int firstContentLine = -1;
        boolean foundSingularChild = false;
        
        for (int line = index - 1; line >= 1; line--) {
            String lineContent = XML.get(line);
            if (lineContent == null) continue;
            
            String lineIndent = extractIndent(lineContent);
            String trimmed = lineContent.trim();
            int lineIndentLen = lineIndent.length();
            if (trimmed.isEmpty()) continue;
            if (lineIndentLen < closingIndentLen) {
                break;
            }
            if (lineIndentLen == closingIndentLen) {
                if (trimmed.startsWith("<") && !trimmed.startsWith("</") && !trimmed.startsWith("<?")) {
                    int endPos = trimmed.indexOf('>');
                    if (endPos > 1) {
                        String foundTag = trimmed.substring(1, endPos);
                        int spacePos = foundTag.indexOf(' ');
                        if (spacePos > 0) foundTag = foundTag.substring(0, spacePos);
                        if (singularChildName != null && foundTag.equals(singularChildName)) {
                            firstContentLine = line;
                            foundSingularChild = true;
                        } else {
                            break;
                        }
                    }
                }
                else if (trimmed.startsWith("</")) {
                    int endPos = trimmed.indexOf('>');
                    if (endPos > 2) {
                        String foundTag = trimmed.substring(2, endPos);
                        if (singularChildName == null || !foundTag.equals(singularChildName)) {
                            break;
                        }
                    }
                }
            }
            else if (lineIndentLen > closingIndentLen) {
                firstContentLine = line;
            }
        }
        int insertLine;
        if (firstContentLine > 0) {
            insertLine = firstContentLine;
        } else {
            insertLine = index;
        }
        String indent = closingIndent;
        String existingLine = fixerrors.containsKey(insertLine) ? fixerrors.get(insertLine) : XML.get(insertLine);
        if (existingLine != null) {
            fixerrors.put(insertLine, indent + openingTag + "\n" + existingLine);
        }
    }

    
    private boolean isTagInStack(String tagName) {
        for (TagInfo info : stack) {
            if (info.tag.equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    
    private void handleMissingClosers(String matchingTagName, String closingTag, int index) {
        while (!stack.isEmpty() && !stack.peek().tag.equals(matchingTagName)) {
            TagInfo unclosed = stack.pop();
            String missingCloser = "</" + unclosed.tag + ">";
            
            errors.put(unclosed.line, "Unclosed tag <" + unclosed.tag + "> (missing " + missingCloser + ")");
            String currentLine = fixerrors.containsKey(index) ? fixerrors.get(index) : XML.get(index);
            if (currentLine != null) {
                String indent = extractIndent(currentLine);
                fixerrors.put(index, indent + missingCloser + "\n" + currentLine);
            }
        }
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    
    private void handleTypoFix(String wrongTagName, String wrongTag, int index) {
        TagInfo expected = stack.peek();
        String correctTag = "</" + expected.tag + ">";
        
        errors.put(index, "Wrong closing tag " + wrongTag + " - expected " + correctTag);
        
        String currentLine = XML.get(index);
        if (currentLine != null) {
            fixerrors.put(index, currentLine.replace(wrongTag, correctTag));
        }
        
        stack.pop();
    }

    
    private boolean isDuplicateOrphan(String closingTag, String openingTag, int index) {
        for (int line = index - 1; line >= 1; line--) {
            String content = XML.get(line);
            if (content == null) continue;
            
            if (content.contains(closingTag)) return true;  // Duplicate found
            if (content.contains(openingTag)) return false; // Has opener
        }
        return false;
    }

    
    private void handleOrphanRemoval(String closingTag, int index) {
        errors.put(index, "Orphaned closing tag " + closingTag + " - removing (duplicate)");
        
        String currentLine = XML.get(index);
        if (currentLine != null) {
            String fixed = currentLine.replace(closingTag, "").trim();
            fixerrors.put(index, fixed.isEmpty() ? "" : fixed);
        }
    }

    
    private void handleMissingOpener(String closingTag, String openingTag, int index) {
        errors.put(index, "Closing tag " + closingTag + " has no matching opening tag");
        
        String currentLine = XML.get(index);
        if (currentLine == null) return;
        
        String indent = extractIndent(currentLine);
        int closingPos = currentLine.indexOf(closingTag);
        String tagName = openingTag.substring(1, openingTag.length() - 1);
        String beforeCloser = currentLine.substring(0, Math.max(0, closingPos)).trim();
        String contentOnly = beforeCloser.replaceAll("^\\s*", "");
        
        if (!contentOnly.isEmpty() && !contentOnly.startsWith("<")) {
            fixerrors.put(index, indent + openingTag + "\n" + 
                indent + "    " + contentOnly + "\n" + 
                indent + closingTag);
        } else {
            int insertLine = findOpeningTagInsertPoint(tagName, index);
            insertOpeningTagAt(insertLine, openingTag, indent);
        }
    }

    
    private String extractIndent(String line) {
        if (line == null) return "";
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < line.length() && Character.isWhitespace(line.charAt(i)); i++) {
            indent.append(line.charAt(i));
        }
        return indent.toString();
    }

    
    private int findOpeningTagInsertPoint(String tagName, int closingIndex) {
        int firstChildLine = -1;
        
        for (int line = closingIndex - 1; line >= 1; line--) {
            String content = XML.get(line);
            if (content == null) continue;
            
            String trimmed = content.trim();
            if (trimmed.startsWith("</")) {
                String closerName = trimmed.substring(2, trimmed.indexOf('>'));
                if (tagName.startsWith(closerName)) {
                    continue;
                } else {
                    return line + 1;
                }
            }
            if (trimmed.startsWith("<") && !trimmed.startsWith("</")) {
                int endPos = trimmed.indexOf('>');
                if (endPos > 1) {
                    String openerName = trimmed.substring(1, endPos);
                    int spacePos = openerName.indexOf(' ');
                    if (spacePos > 0) openerName = openerName.substring(0, spacePos);
                    
                    if (tagName.startsWith(openerName)) {
                        firstChildLine = line;
                    } else {
                        if (firstChildLine > 0) return firstChildLine;
                        return line + 1;
                    }
                }
            }
        }
        
        return firstChildLine > 0 ? firstChildLine : 1;
    }

    
    private void insertOpeningTagAt(int line, String openingTag, String indent) {
        String existing = fixerrors.containsKey(line) ? fixerrors.get(line) : XML.get(line);
        if (existing != null) {
            fixerrors.put(line, indent + openingTag + "\n" + existing);
        }
    }

    public void PrintErrors() {
        if (errors.isEmpty()) {
            System.out.println("No Errors found");
        } else {
            System.out.println("Errors:");
            System.out.println(errors);
        }
    }

    private HashMap<Integer, String> reindex(HashMap<Integer, String> map) {
        HashMap<Integer, String> result = new HashMap<>();
        int newKey = 1;
        for (int i = 1; i <= map.size() + 100; i++) {
            if (map.containsKey(i)) {
                result.put(newKey++, map.get(i));
            }
        }
        return result;
    }

    public HashMap<Integer, String> applyFixes() {
        HashMap<Integer, String> fixedXML = new HashMap<>(XML);
        for (Integer line : fixerrors.keySet()) {
            String fix = fixerrors.get(line);
            if (fix.isEmpty()) {
                fixedXML.remove(line);
            } else {
                fixedXML.put(line, fix);
            }
        }
        HashMap<Integer, String> reindexed = reindex(fixedXML);
        HashMap<Integer, String> result = new HashMap<>();
        int newLineNum = 1;
        int maxKey = 0;
        for (Integer key : reindexed.keySet()) {
            if (key > maxKey) maxKey = key;
        }
        
        for (int i = 1; i <= maxKey; i++) {
            String line = reindexed.get(i);
            if (line == null) continue;
            String[] parts = line.split("\n", -1);
            for (String part : parts) {
                if (!part.isEmpty()) {
                    result.put(newLineNum, part);
                    newLineNum++;
                }
            }
        }

        return result;
    }

    
    public void applyFixesToFile(String outputPath) {
        HashMap<Integer, String> fixedXml = applyFixes();
        String content = Xml_Rearder.mapToString(fixedXml);
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(outputPath))) {
            writer.write(content);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error writing fixed XML: " + e.getMessage(), e);
        }
    }

    public void PrintFixes() {
        if (fixerrors.isEmpty()) {
            System.out.println("No Fixes needed");
        } else {
            System.out.println("Fixes:");
            System.out.println(fixerrors);
        }
    }
}
