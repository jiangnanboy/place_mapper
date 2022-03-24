package sy;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import utils.CollectionUtil;
import utils.PropertiesReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class LoadPlace {

    public static Trie userTrie;

    static {
        userTrie = loadUserDict(LoadPlace.class.getClassLoader().getResource(PropertiesReader.get("place_pcdd_user_dict")).getPath().replaceFirst("/", ""));
    }

    private static Trie loadUserDict(String filePath) {
        System.out.println("load user dict from : " + filePath);
        Trie.TrieBuilder trieUserDict = Trie.builder();
        List<String> wordsList = CollectionUtil.newArrayList();
        try(Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> {
                String[] tokens = line.trim().split(" ");
                wordsList.add(tokens[0]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        trieUserDict.onlyWholeWords();
        trieUserDict.addKeywords(wordsList);
        return trieUserDict.build();
    }

    public static Map<String, String> placeIdMap;
    static {
        loadPlaceId(LoadPlace.class.getClassLoader().getResource(PropertiesReader.get("place_aud_new_adcodes")).getPath().replaceFirst("/", ""));
    }

    private static void loadPlaceId(String filePath) {
        placeIdMap = CollectionUtil.newHashMap();
        try(Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> {
                String[] tokens = line.trim().split(",");
                String adCode = tokens[0];
                String placeName = tokens[1];
                placeIdMap.put(placeName, adCode);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, AddrInfo> adMap;
    public static Matcher matcher;

    static {
        loadAdCodes(LoadPlace.class.getClassLoader().getResource(PropertiesReader.get("place_aud_new_adcodes")).getPath().replaceFirst("/", ""));
    }

    private static void loadAdCodes(String filePath) {
        System.out.println("load adcode dict from : " + filePath);
        adMap = CollectionUtil.newHashMap();
        matcher = new Matcher("([省市县区]|自治区)$");
        try(Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> {
                String[] tokens = line.trim().split(",");
                String name = tokens[1];
                String adCode = tokens[0];
                AddrInfo addrInfo = new AddrInfo(name, adCode);
                adMap.put(adCode, addrInfo);
                matcher.addAddrInfo(addrInfo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        matcher.buildAcTrie();
    }

    public static List<String> maxMatch(String sentence) {
        return maxMatch(sentence, 15);
    }

    // maxMatchLen : the length of the longest word in the dict
    public static List<String> maxMatch(String sentence, int maxMatchLen) {
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
                Emit emit = userTrie.firstMatch(maxStr);
                if(Optional.ofNullable(emit).isPresent()) {
                    start += maxStr.length() - 1;
                    wordsList.add(maxStr);
                    break;
                }
            }
            start += 1;

        }

        if(wordsList.size() == 0) {
            return Arrays.asList(sentence.split(""));
        } else {
            return getAllSentenceWords(sentence, wordsList);
        }
    }

    private static List<String> getAllSentenceWords(String sentence, List<String> wordsList) {
        List<String> allWordsList = CollectionUtil.newArrayList();
        int start = 0;
        for(String word : wordsList) {
            String subString = sentence.substring(start);
            int wordStartIdx = subString.indexOf(word);
            if(wordStartIdx > 0) {
                allWordsList.addAll(Arrays.asList(subString.substring(0, wordStartIdx).split("")));
            }
            allWordsList.add(word);
            start +=  wordStartIdx + word.length();
        }
        if(start < sentence.length()) {
            allWordsList.addAll(Arrays.asList(sentence.substring(start).split("")));
        }
        return allWordsList;
    }

}

