package sy;

import utils.CollectionUtil;

import java.util.Map;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class PlaceEnumMap {

    public static final Map<PlaceEnum, String> placeEnumCategory = CollectionUtil.newHashMap();

    static {
        placeEnumCategory.put(PlaceEnum.PROVINCE, "province");
        placeEnumCategory.put(PlaceEnum.PROVINCE_POS, "province_pos");
        placeEnumCategory.put(PlaceEnum.CITY, "city");
        placeEnumCategory.put(PlaceEnum.CITY_POS, "city_pos");
        placeEnumCategory.put(PlaceEnum.COUNTY, "county");
        placeEnumCategory.put(PlaceEnum.COUNTY_POS, "county_pos");
        placeEnumCategory.put(PlaceEnum.Other, "other");
    }

}
