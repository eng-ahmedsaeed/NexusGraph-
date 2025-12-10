package Formating;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String xml = "<companyDatabase>\n" +
                " <company id=\"101\" name=\"TechSoft\">\n" +
                "                   <department id=\"D01\" name=\"Development\">\n" +
                "  <employee id=\"E001\">\n" +
                "  <name>Ahmad Sayed</name>\n" +
                "  <position>Software Engineer</position>\n" +
                "  <salary currency=\"USD\">5000</salary>\n" +
                "  <skills>\n" +
                "  <skill>Java</skill>\n" +
                "  <skill>Python</skill>\n" +
                "  <skill>SQL</skill>\n" +
                "  </skills>\n" +
                "  </employee>\n" +
                "  <employee id=\"E002\">\n" +
                "  <name>Sara Ahmed</name>\n" +
                "  <position>Frontend Developer</position>\n" +
                "  <salary currency=\"USD\">4500</salary>\n" +
                "  <skills>\n" +
                "  <skill>HTML</skill>\n" +
                "  <skill>CSS</skill>\n" +
                "  <skill>JavaScript</skill>\n" +
                "  </skills>\n" +
                "  </employee>\n" +
                "  </department>\n" +
                "  <department id=\"D02\" name=\"Marketing\">\n" +
                "  <employee id=\"E003\">\n" +
                "  <name>Omar Khaled</name>\n" +
                "  <position>Marketing Specialist</position>\n" +
                "  <salary currency=\"USD\">4000</salary>\n" +
                "  </employee>\n" +
                "  </department>\n" +
                "  </company>\n" +
                "  <company id=\"102\" name=\"HealthCorp\">\n" +
                "  <department id=\"D01\" name=\"Research\">\n" +
                "  <employee id=\"E004\">\n" +
                "  <name>Laila Hassan</name>\n" +
                "  <position>Research Scientist</position>\n" +
                "  <salary currency=\"USD\">6000</salary>\n" +
                "  <projects>\n" +
                "  <project>Cancer Study</project>\n" +
                "  <project>Vaccine Development</project>\n" +
                "  </projects>\n" +
                "  </employee>\n" +
                "  </department>\n" +
                "  <department id=\"D02\" name=\"HR\">\n" +
                "  <employee id=\"E005\">\n" +
                "  <name>Mohamed Ali</name>\n" +
                "  <position>HR Manager</position>\n" +
                "  <salary currency=\"USD\">5500</salary>\n" +
                "  </employee>\n" +
                "  <employee id=\"E006\">\n" +
                "  <name>Nour Farouk</name>\n" +
                "  <position>Recruiter</position>\n" +
                "  <salary currency=\"USD\">4200</salary>\n" +
                "  </employee>\n" +
                "  </department>\n" +
                "  </company>\n" +
                "</companyDatabase>";

        String[] lines = xml.split("(?<=\\n)");
        Map<Integer, String> xmlMap = new LinkedHashMap<>();
        for (int i = 0; i < lines.length; i++) {
            xmlMap.put(i + 1, lines[i]);
        }
        FormatingFile file = new FormatingFile();
        file.foramtXml(xmlMap);
    }
}