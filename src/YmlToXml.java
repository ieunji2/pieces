import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class YmlToXml {

  public static void main(String[] args) throws IOException {
    byte[] content = readFile();
    writeFile(mapToXml(convertYmlToMapBySnakeYml(content)));
    writeFile(voToXml(convertYmlToMapByJackson(content)));
  }

  private static byte[] readFile() throws IOException {
    String ymlFilePath = "resources/yml/sample.yaml";
    return Files.readAllBytes(Paths.get(ymlFilePath));
  }

  private static void writeFile(String xmlContent) {
    String xmlFilePath = "resources/xml/sample_" + getDateTimeMs() + ".xml";
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFilePath))) {
      bw.write(xmlContent);
      bw.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private static Map<String, Object> convertYmlToMapBySnakeYml(byte[] content) {
    Yaml yaml = new Yaml();
    Map<String, Object> m = yaml.load(new String(content));
    return m;
  }

  private static String mapToXml(Map map) {
    CodeVo vo = new CodeVo();
    vo.setName(map.get("name").toString());

    StringBuffer xml = new StringBuffer();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\r\n");
    xml.append("<category name=\"" + map.get("name") + "\" lang=\"" + map.get("lang") + "\">").append("\r\n");
    List<Map> list = (List<Map>) map.get("items");
    list.stream().forEach(value -> {
      xml.append("\t").append(new MessageFormat("<name>{0}</name>\r\n\t<wage>{1}</wage>\r\n\t<position>{2}</position>").format(new Object[]{value.get("name"), value.get("wage"), value.get("position")})).append("\r\n");
    });
    xml.append("</category>");
    return xml.toString();
  }

  private static CodeVo convertYmlToMapByJackson(byte[] content) throws IOException {
    ObjectMapper om = new ObjectMapper(new YAMLFactory());
    // Map<String, Object> m = om.readValue(content, Map.class);
    CodeVo vo = om.readValue(content, CodeVo.class);
    return vo;
  }

  private static String voToXml(CodeVo vo) {
    StringBuffer xml = new StringBuffer();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\r\n");
    xml.append("<category name=\"" + vo.getName() + "\" lang=\"" + vo.getLang() + "\">").append("\r\n");
    List<Map> list = (List<Map>) vo.items;
    list.stream().forEach(value -> {
      xml.append("\t").append(new MessageFormat("<name>{0}</name>\r\n\t<wage>{1}</wage>\r\n\t<position>{2}</position>").format(new Object[]{value.get("name"), value.get("wage"), value.get("position")})).append("\r\n");
    });
    xml.append("</category>");
    return xml.toString();
  }

  static class CodeVo {
    private String name;
    private String lang;
    private List items;
    private String wage;
    private String position;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getLang() {
      return lang;
    }

    public void setLang(String lang) {
      this.lang = lang;
    }

    public List getItems() {
      return items;
    }

    public void setItems(List items) {
      this.items = items;
    }

    public String getWage() {
      return wage;
    }

    public void setWage(String wage) {
      this.wage = wage;
    }

    public String getPosition() {
      return position;
    }

    public void setPosition(String position) {
      this.position = position;
    }
  }

  private static String getDate() {
    LocalDate date = LocalDate.now();
    return date.format(DateTimeFormatter.BASIC_ISO_DATE);
  }

  private static String getTime() {
    LocalTime time = LocalTime.now();
    String timeStr = time.format(DateTimeFormatter.ISO_TIME)
            .replace(":", "")
            .replace(".", "");
    return timeStr.substring(0, 6);
  }

  private static String getTimeMs() {
    LocalTime time = LocalTime.now();
    String timeStr = time.format(DateTimeFormatter.ISO_TIME)
            .replace(":", "")
            .replace(".", "");
    if (timeStr.length() < 9) {
      timeStr = String.format("%-9s", timeStr).replace(" ", "0");
    } else {
      timeStr = timeStr.substring(0, 9);
    }
    return timeStr;
  }

  private static String getDateTime() {
    return getDate() + getTime();
  }

  private static String getDateTimeMs() {
    return getDate() + getTimeMs();
  }
}
