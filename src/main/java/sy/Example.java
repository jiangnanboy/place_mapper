package sy;

import java.util.Map;
import java.util.Scanner;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class Example {
    public static void main(String[] args) {
        String place;
        while (true) {
            System.out.println("Please input ");
            Scanner in = new Scanner(System.in);
            place = in.next();
            Map<String, String> placeMap = PlaceMapper.check(place);
            placeMap.forEach((key, value) -> System.out.println(key + " -> " + value + " -> " + LoadPlace.placeIdMap.get(value)));
        }

    }
}
