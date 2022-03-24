package sy;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import utils.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class Matcher {

    Map<String, List<AddrInfo>> abbrNameShareListMap;
    Trie.TrieBuilder acTrieBuilder;
    Trie trie;
    String regEx;
    static Map<String, String> specialAbbre;
    static {
        specialAbbre = Stream.of(new Object[][]{
                {"内蒙古自治区", "内蒙古"},
                {"广西壮族自治区", "广西"},
                {"西藏自治区", "西藏"},
                {"新疆维吾尔自治区", "新疆"},
                {"宁夏回族自治区", "宁夏"}
        }).collect(Collectors.toMap(data -> (String)data[0], data -> (String)data[1]));
    }

    public Matcher(String regEx) {
        this.acTrieBuilder = Trie.builder();
        this.acTrieBuilder.onlyWholeWords();

        this.regEx = regEx;
        abbrNameShareListMap = CollectionUtil.newHashMap();
    }

    private String abbrName(String originName) {
        if(specialAbbre.containsKey(originName)) {
            return specialAbbre.get(originName);
        } else {
            return originName.replaceAll(this.regEx, "");
        }
    }

    private String firstAddAddr(AddrInfo addrInfo) {
        String abbrName = this.abbrName(addrInfo.name);
        List<AddrInfo> shareList = CollectionUtil.newArrayList();
        this.acTrieBuilder.addKeyword(abbrName);
        this.abbrNameShareListMap.put(abbrName, shareList);
        this.acTrieBuilder.addKeyword(addrInfo.name);
        this.abbrNameShareListMap.put(addrInfo.name, shareList);
        return abbrName;
    }

    public void addAddrInfo(AddrInfo addrInfo) {
        if(this.abbrNameShareListMap.containsKey(addrInfo.name)) {
            List<AddrInfo> addrInfoList = this.abbrNameShareListMap.get(addrInfo.name);
            addrInfoList.add(addrInfo);
        } else {
            String abbrName = firstAddAddr(addrInfo);
            List<AddrInfo> addrInfoList = this.abbrNameShareListMap.get(abbrName);
            addrInfoList.add(addrInfo);
        }
    }

    public void buildAcTrie() {
        System.out.println("build ac trie...");
        this.trie = this.acTrieBuilder.build();
    }

    public List<String> maxMatch(String sentence) {
        return maxMatch(sentence, 15);
    }

    public List<String> maxMatch(String sentence, int maxMatchLen) {

        List<String> wordsList = CollectionUtil.newArrayList();
        int start = 0;
        int sentLen = sentence.length();
        while(start < sentLen) {
            for(int i = maxMatchLen; i > 0; i--) {
                String maxStr;
                if((start + i) > sentLen) {
                    maxStr = sentence.substring(start);
                } else {
                    maxStr = sentence.substring(start, start + i);
                }
                Emit emit = LoadPlace.userTrie.firstMatch(maxStr);
                if(Optional.ofNullable(emit).isPresent()) {
                    wordsList.add(maxStr);
                    start += maxStr.length() - 1;
                    break;
                }
            }
            start += 1;
        }

        return wordsList;
    }

    public List<MatchInfo> maxIter(String sentence) {
        List<MatchInfo> matchInfoList = CollectionUtil.newArrayList();
        List<String> matchWordList = maxMatch(sentence, 15);
        matchWordList.stream().forEach(word -> {
            MatchInfo curMatchInfo = new MatchInfo(this.abbrNameShareListMap.get(word), -1, -1, word);
            matchInfoList.add(curMatchInfo);
        });
        return matchInfoList;
    }

}


