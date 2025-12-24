package org.example.Level_1;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Compression {
	private List<String> bufferList;
	HuffmanEncoder huffEncode;
	private HashMap<String, Character> KeysToTokens;
	private char randomChar = 'A';
	private String outputPath;
	
	public Compression(){
		bufferList = new ArrayList<>();
		KeysToTokens = new HashMap<>();
		huffEncode = new HuffmanEncoder();
	} // default constructor
	
	boolean processFile(String path) //stores all the data inside the XML file inside a queue
	{
		String content = Xml_Rearder.readFileToString(path);
		if (content.isEmpty()) {
			System.out.println("File not found or empty");
			return false;
		}
		String[] lines = content.split("\n");
		for (String line : lines) {
			bufferList.add(line.replace("\r", ""));
		}
		return true;
	}
	
	
	private String removeSpace(String next) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(i < next.length())
		{
			if(next.charAt(i) == ' ')
				i++;
			else
				break;
		}
		while(i < next.length())
		{
			sb.append(next.charAt(i));
			i++;
		}
		return sb.toString();
	}
	
	private String xmlTokenizer(String next)
	{
		StringBuilder sb = new StringBuilder();
		StringBuilder token = new StringBuilder();
		int i = 0;
		while(i < next.length())
		{
			if(next.charAt(i) == '<') {
				token.delete(0, token.length());//make sure the builder is empty
				while(i < next.length()) {
					token.append(next.charAt(i));
					if(next.charAt(i) == '>')break;
					i++;
				}
				
				if(!KeysToTokens.containsKey(token.toString())) {
					KeysToTokens.put(token.toString(), randomChar);
					sb.append('<');
					sb.append(randomChar);
					randomChar++;
				}
				else {
					sb.append('<');
					sb.append(KeysToTokens.get(token.toString()));
				}
			}
			else {
				sb.append(next.charAt(i));
			}
			i++;
		}
		return sb.toString();
	}
	
	boolean createCompressedFile(String type)
	{
		String filePath = outputPath;
		java.io.File outFile = new java.io.File(outputPath);
		String keyFileName = String.format("KeyFile%s.comp", type);
		String KeyPath;
		if (outFile.getParent() != null) {
			KeyPath = new java.io.File(outFile.getParent(), keyFileName).getAbsolutePath();
		} else {
			KeyPath = keyFileName; // Fallback to current directory
		}
		
		boolean typeCheck = ("XML").equals(type);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { 
			for (String line : bufferList)	
			{ 
				writer.write(line); 
				writer.newLine(); 
			}
		}catch (IOException e) { 
			return false;
		}
		
		try (BufferedWriter keyWriter = new BufferedWriter(new FileWriter(KeyPath))) { 
			for (Entry<String, Character> entry : KeysToTokens.entrySet()) 
			{ 
				if(typeCheck) {
					keyWriter.write("<" + entry.getValue()+ entry.getKey()); 
				}
				else {
					keyWriter.write("'" + entry.getValue()+ entry.getKey()); 
				}
				keyWriter.newLine(); 
			}
		}catch (IOException e) { 
			return false;
		}
		
		bufferList.removeAll(bufferList);
		KeysToTokens.clear();
		randomChar = 'A';
		return true;
	}
	
	boolean createCompressedFile(String type, List<BitSet> bstList)
	{
		String filePath = String.format("./%s", outputPath);
		String KeyPath = String.format("./KeyFile%s.comp", type);
		
		try (FileOutputStream fos = new FileOutputStream(filePath);
	             BufferedOutputStream bos = new BufferedOutputStream(fos))  { 
			for (BitSet line : bstList)	
			{ 
				byte[] bitSetBytes = line.toByteArray();

				bos.write((byte)bitSetBytes.length);
				bos.write(bitSetBytes);
				bos.write('\n'); // Write a new line to separate lines in the file 
			}
		}catch (IOException e) { 
			return false;
		}
		
		try (FileOutputStream fos = new FileOutputStream(KeyPath);
	             BufferedOutputStream kbos = new BufferedOutputStream(fos)) { 
			
			HashMap<Character, String> hm = huffEncode.getHuffmanMap();
			for(Map.Entry<Character, String> entry: hm.entrySet()) {
				String str;
				str = entry.getValue();
				BitSet bst = new BitSet(str.length()+1);
				int i;
				for(i = 0; i < str.length(); i++) {
					if(str.charAt(i) == '1') {
						bst.set(i);
					}
				}
				bst.set(i);
				
				byte[] bitSetBytes = bst.toByteArray();
				kbos.write(entry.getKey());
				kbos.write((byte)bitSetBytes.length);
				kbos.write(bitSetBytes);
				kbos.write('\n'); // Write a new line to separate lines in the file 
			}
		}catch (IOException e) { 
			return false;
		}
		
		bufferList.removeAll(bufferList);
		KeysToTokens.clear();
		randomChar = 'A';
		return true;
	}

	public boolean compressXML(String xmlPath) {
		return compressTokenized(xmlPath, false);
	}

	public boolean compressJSON_tokenization(String jsonPath) {
		return compressTokenized(jsonPath, true);
	}

	
	private boolean compressTokenized(String path, boolean isJson) {
		int counter = 0;
		if(processFile(path)) {
			for(String str : bufferList) {
				String tokenized = isJson 
					? jsonTokenizer(removeSpace(str)) 
					: xmlTokenizer(removeSpace(str));
				bufferList.set(counter, tokenized);
				counter++;
			}
			return createCompressedFile(isJson ? "JSON" : "XML");
		}
		else
			return false;
	}
	
	boolean compressJSON(String jsonPath) {
		List<BitSet> bstList = new ArrayList<>();
		if(processFile(jsonPath))
		{
			bstList = huffEncode.encodeList(bufferList);
			return createCompressedFile("JSON", bstList);
		}
		else
			return false;
	}
	
	private String jsonTokenizer(String next) {
	    StringBuilder sb = new StringBuilder();
	    StringBuilder token = new StringBuilder();
	    int i = 0, j = 0;
	    boolean tokenDetected = false;
	    while (i < next.length()) {
	        if (next.charAt(i) == '"') {
	            token.setLength(0); // Clear the token builder
	            token.append(next.charAt(i));
	            j = i + 1;
	            while (j < next.length() && next.charAt(j) != '"') {
	                token.append(next.charAt(j));
	                j++;
	            }
	            if (j < next.length()) {
	                token.append(next.charAt(j));
	                j++;
	            }
	            if (j < next.length() && next.charAt(j) == ':') {
	                tokenDetected = true;
	                j++;
	            }
	            if (tokenDetected) {
	                if (!KeysToTokens.containsKey(token.toString())) {
	                    KeysToTokens.put(token.toString(), randomChar);
	                    sb.append('\'');
	                    sb.append(randomChar);
	                    randomChar++;
	                } else {
	                	sb.append('\'');
	                    sb.append(KeysToTokens.get(token.toString()));
	                }
	                i = j; // Move the main index to continue after the token
	            } else {
	                sb.append(token);
	                i = j; // Move the main index to continue after the token
	            }
	            tokenDetected = false;
	        } else {
	            sb.append(next.charAt(i));
	            i++;
	        }
	    }
	    return sb.toString();
	}
	
	public void printHuffmanCodes() {
		HashMap<Character, String> hm = huffEncode.getHuffmanMap();
		for(Map.Entry<Character, String> entry: hm.entrySet()) {
			System.out.println("Key: " + entry.getKey()+ " Code: "+ entry.getValue());
		}
	}

	public List<String> getBufferedList()
	{
		return bufferList;
		
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
}
