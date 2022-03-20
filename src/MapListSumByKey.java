import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapListSumByKey {
  public static void main(String[] args) {
    Map<String, Integer> map1 = new HashMap<>();
    map1.put("col1", 10);
    map1.put("col2", 10);
    map1.put("col3", 10);
    map1.put("col4", 10);
    map1.put("col5", 10);

    Map<String, Integer> map2 = new HashMap<>();
    map2.put("col1", 1);
    map2.put("col2", 2);
    map2.put("col3", 3);
    map2.put("col4", 4);
    map2.put("col5", 5);

    List<Map<String, Integer>> list = new ArrayList<>();
    list.add(map1);
    list.add(map2);

    list.forEach(x -> System.out.println(x));

    MapListSumByKey m = new MapListSumByKey();
    System.out.println(m.sum(list));
  }

  private Map<String, Integer> sum(List<Map<String, Integer>> list) {
    return list.stream()
            .flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), Integer::sum));
  }
}
