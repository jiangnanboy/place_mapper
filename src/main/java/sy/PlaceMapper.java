package sy;

import org.apache.commons.lang3.StringUtils;
import utils.CollectionUtil;

import java.util.Map;
import java.util.Optional;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class PlaceMapper {

    public static Map<String, String> check(String sentence) {
        return getOneAddr(sentence);
    }

    public static Map<String, String> emptyRecord() {
        Map<String, String> empty = CollectionUtil.newHashMap();
        empty.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.PROVINCE), null);
        empty.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.CITY), null);
        empty.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.COUNTY), null);
        empty.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.Other), null);
        return empty;
    }

    private static Map<String, String> getOneAddr(String sentence) {
        return extractAddrs(sentence);
    }

    private static Map<String, String> extractAddrs(String sentence)
    {
     return extractAddrs(sentence, true, false);
    }

    private static Map<String, String> extractAddrs(String sentence, boolean truncatePos, boolean newEntryWhenNotBelong) {

        Map<String, String> resMap = emptyRecord();
        AddrInfo lastInfo = null;
        String adCode = null;
        int truncateIndex = -1;
        for(MatchInfo matchInfo : LoadPlace.matcher.maxIter(sentence)) {
            AddrInfo curAddr = matchInfo.getMatchAddr(lastInfo);
            if(null != curAddr) {
                lastInfo = curAddr;
                adCode = curAddr.adCode;
                truncateIndex = matchInfo.endIndex;
                if(curAddr.rank == new AddrInfo().RANK_COUNTY) {
                    updateResByAdcode(resMap, adCode);
                    if(truncatePos) {
                        resMap.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.Other), sentence.substring(truncateIndex + 1));
                    } else {
                        resMap.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.Other), "");
                    }
                    return resMap;
                }
            }
        }

        if(StringUtils.isBlank(adCode)) {
            return resMap;
        }

        updateResByAdcode(resMap, adCode);
        if(truncatePos) {
            resMap.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.Other), sentence.substring(truncateIndex + 1));
        } else {
            resMap.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.Other), "");
        }

        return resMap;
    }

    private static String fillAdcode(String adCode) {
        return StringUtils.rightPad(adCode, 12, "0");
    }

    public static String adcodeName(String partAdcode) {
        AddrInfo addrInfo = LoadPlace.adMap.get(fillAdcode(partAdcode));
        return Optional.ofNullable(addrInfo.name).orElse(null);
    }

    public static void updateResByAdcode(Map<String, String> resDict, String adCode) {
        if(adCode.endsWith("0000")) {
            resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.PROVINCE), adcodeName(adCode.substring(0, 2)));
            return;
        }
        if(adCode.endsWith("00")) {
            resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.PROVINCE), adcodeName(adCode.substring(0, 2)));
            resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.CITY), adcodeName(adCode.substring(0, 4)));
            return;
        }
        resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.PROVINCE), adcodeName(adCode.substring(0, 2)));
        resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.CITY), adcodeName(adCode.substring(0, 4)));
        resDict.put(PlaceEnumMap.placeEnumCategory.get(PlaceEnum.COUNTY), adcodeName(adCode));
    }

}


