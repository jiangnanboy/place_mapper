package sy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class MatchInfo {
    List<AddrInfo> addrInfos;
    int startIndex;
    int endIndex;
    String originValue;
    public MatchInfo(List<AddrInfo> addrInfos, int startIndex, int endIndex, String originValue) {
        this.addrInfos = addrInfos;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.originValue = originValue;
    }

    public AddrInfo getMatchAddr(AddrInfo parentAddr) {
        if(Optional.ofNullable(parentAddr).isPresent()) {
            List<AddrInfo> otherAddrInfo = this.addrInfos.stream().filter(addrInfo -> addrInfo.belongTo(parentAddr)).collect(Collectors.toList());
            if(otherAddrInfo.size() > 0) {
                return otherAddrInfo.get(0);
            } else {
                return null;
            }
        } else {
            return this.addrInfos.get(0);
        }
    }

    public int getRank() {
        return this.addrInfos.get(0).rank;
    }

    public AddrInfo getOneAddr() {
       return this.addrInfos.get(0);
    }

    @Override
    public String toString() {
        return "from " + this.startIndex + " to " + this.endIndex + " value " + this.originValue;
    }

}
