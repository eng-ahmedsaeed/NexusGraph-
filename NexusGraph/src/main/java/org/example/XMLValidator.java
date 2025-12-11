package org.example;

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
            StringBuilder sb = new StringBuilder(s);
            sb.append('\n');

            while (sb.charAt(index) != '\n') {

                if (sb.charAt(index) == '<') {
                    tagFlag = true;
                    textChecker(text, i);
                    text.setLength(0);
                    index++;
                    continue;
                }

                else if (sb.charAt(index) == '>') {
                    tagFlag = false;
                    tagChecker(tag, i);
                    tag.setLength(0);
                    index++;
                    continue;
                }

                if (tagFlag) {
                    tag.append(sb.charAt(index));
                } else {
                    text.append(sb.charAt(index));
                }
                index++;
            }
        }
        stackChecker(i);
    }

    private void tagChecker(StringBuilder tag, int index) {
        // إزالة الأرقام من بداية tag سواء opening أو closing
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
            stack.push(new TagInfo(tag.toString(), index));
        } else {

            if (stack.isEmpty()) {
                errors.put(index, "wrong closing tag for unopened tag");
                fixerrors.put(index, ""); // حذف السطر الغير مرغوب
                return;
            }

            String s = tag.toString().substring(1); // اسم الوسم بدون /
            TagInfo top = stack.peek();

            if (top.tag.equals(s)) {
                stack.pop();
            } else {
                // تعديل closing tag فقط بدون أي مسافة أو سطر جديد
                String originalLine = XML.get(index);
                int start = originalLine.indexOf("</");
                String fixedLine;
                if (start != -1) {
                    fixedLine = originalLine.substring(0, start) + "</" + top.tag + ">";
                } else {
                    fixedLine = "</" + top.tag + ">";
                }
                errors.put(index, "Tag mismatch expected </" + top.tag + ">");
                fixerrors.put(index, fixedLine);
                stack.pop();
            }
        }
    }

    private void stackChecker(int i) {
        while (!stack.isEmpty()) {
            TagInfo t = stack.pop();
            String s = t.tag;
            int lineToInsert = XML.size() + 1;
            errors.put(lineToInsert, "expecting closing tag </" + s + ">");
            fixerrors.put(lineToInsert, "</" + s + ">");
            // نضيف closing tag مباشرة بعد النص الأخير بدون أي مسافة إضافية
            XML.put(lineToInsert, "</" + s + ">");
        }
    }

    public void PrintErrors() {
        if (errors.isEmpty()) {
            System.out.println("No Errors found");
        } else {
            System.out.println("Errors:");
            System.out.println(errors);
        }
        errors.clear();
    }

    private HashMap<Integer, String> reindex(HashMap<Integer, String> map) {
        HashMap<Integer, String> newMap = new HashMap<>();
        int line = 1;
        for (int key = 1; key <= map.size(); key++) {
            String v = map.get(key);
            if (v != null && !v.trim().isEmpty()) {
                newMap.put(line, v);
                line++;
            }
        }
        return newMap;
    }

    public HashMap<Integer, String> applyFixes() {
        HashMap<Integer, String> fixedXML = new HashMap<>(XML);

        for (Integer line : fixerrors.keySet()) {
            String fix = fixerrors.get(line);

            if (fix.equals("")) {
                fixedXML.put(line, "");
                continue;
            }

            fixedXML.put(line, fix);
        }

        return reindex(fixedXML);
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


