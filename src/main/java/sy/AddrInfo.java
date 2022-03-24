package sy;

/**
 * @author sy
 * @date 2022/3/20 15:00
 */
public class AddrInfo {
    final int RANK_PROVINCE = 0;
    final int RANK_CITY = 1;
    final int RANK_COUNTY = 2;
    String name;
    String adCode;
    int rank;

    public AddrInfo(){}
    public AddrInfo(String name, String adCode) {
        this.name = name;
        // adcode 的前 6 位代表省市区三级
        this.adCode = adCode.substring(0, 6);
        // rank 代表行政区划级别 0: 省 1: 市 2: 县
        if(this.adCode.endsWith("0000")) {
            this.rank = this.RANK_PROVINCE;
        } else if(this.adCode.endsWith("00")) {
            this.rank = this.RANK_CITY;
        } else {
            this.rank = this.RANK_COUNTY;
        }
    }

    public boolean belongTo(AddrInfo other) {
        return this.adCode.startsWith(other.adCode.substring(0, (other.rank + 1) * 2));
    }

}
