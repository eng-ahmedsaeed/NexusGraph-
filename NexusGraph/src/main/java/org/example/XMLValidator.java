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
    //*stack to use in tags matching validation
    private Stack<TagInfo> stack = new Stack<>();
    //*map to hold the error lines with the message
    public HashMap<Integer, String> errors = new HashMap<>();
    //*map to hold the xml file
    private HashMap<Integer, String> XML = new HashMap<>();
    //*Regex for the open tag format
    static final private String openTagRegex = "^[a-zA-Z_:][a-zA-Z0-9_.:-]*$";
    //*Regex for the closingtag format
    static final private String closeTagRegex = "^/[a-zA-Z_:][a-zA-Z0-9_.:-]*$";
    //*Regex for the text data for a tag
    static final private String textRegex = "^[^</>]*$";

    public XMLValidator(HashMap<Integer, String> XML) {
        this.XML = XML;
    }
    public void XMLSetter(HashMap<Integer, String> XML)
    {
        this.XML=XML;
    }

    public void validate() {
        //*string to hold the string data from the xml file
        String s;
        //*flag to decide whiter when i am scanning if i am inside tag char
        boolean tagFlag = false;
        //*index for map keys
        int index;
        int i;
        //*string builder for tag and for the text
        StringBuilder tag = new StringBuilder("");
        StringBuilder text = new StringBuilder("");
        //* for loop to move throw the lines
        for ( i = 1; i <= XML.size(); i++) {
            //*empty the 2 buffer after each line
            text.delete(0, text.length());
            tag.delete(0, tag.length());

            index = 0;
            //*extracting the line as string from the map and pass it to the builder
            //*then adding a '/n' to mark the end of the array
            s = XML.get(i);
            StringBuilder sb = new StringBuilder(s);
            sb.append('\n');
            //*here i loop throw the whole line and take out the tags checking its syntex and push in  stack and also check the
            //*syntex of the text

            while (sb.charAt(index) != '\n') {
                if (sb.charAt(index) == '<') {
                    tagFlag = true;
                    textChecker(text, i);
                    //*here i empty the text variable
                    text.delete(0, text.length());
                    index++;
                    continue;
                } else if (sb.charAt(index) == '>') {
                    tagFlag = false;
                    tagChecker(tag, i);
                    //*here i empty the tag variable
                    tag.delete(0, tag.length());
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
        //*here i check the syntex of the tag and also its type to tell the stack
        if (tag.toString().matches(openTagRegex)) {

            stackPush(tag, true,index);
            return;
        } else if (tag.toString().matches(closeTagRegex)) {

            stackPush(tag, false,index);
            return;
        }
        errors.put(index, "The tag" + tag.toString() + " has invalid format");
    }

    private void textChecker(StringBuilder text, int index) {
        //*here i check the text syntex against a regex
        if (text.toString().matches(textRegex)) {
            return;
        }
        errors.put(index, "The text" + text + "contains invalid characters(< or > or /");
    }

    private void stackPush(StringBuilder tag, boolean openTag,int index) {
        if (openTag) {
            TagInfo t=new TagInfo(tag.toString(), index);
            //*if its open tag then push it directly put convert it to string firstly
            stack.push(t);
        } else {
            //* these conition to handle the case of mismatch leading to
            //*adding an extra closing tag to empty stack
            if(stack.isEmpty()){
                errors.put(index, "wrong closing tag for unopened tag");
           return;
            }
            String s = tag.toString().substring(1);
            //*removing the number line before comparing

            if (stack.peek().tag.equals(s)) {
                stack.pop();
            } else {
                errors.put(index,"Tag mismatch expected"+"</"+stack.peek().tag+">");
                stack.pop();

            }

        }
    }
    private void stackChecker( int i) {
        int line;
        while (!stack.isEmpty()) {
            TagInfo t = stack.pop();
            String s=t.tag;
            //*here i convert the lie value from char to integer
            line = t.line;
            errors.put(i+1,"expecting closing tag " + "</"+s+">");
        }
    }
    public void PrintErrors() {
        if (errors.isEmpty()) {
            System.out.println("No Errors is found ");
        } else {
            System.out.println("Errors:");
            System.out.println(errors);

        }
      errors.clear();

    }
}


