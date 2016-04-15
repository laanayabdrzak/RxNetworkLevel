package laanayabdrzak.ma.rxnetworklevel;

/**
 * Created by abderrazak on 14/04/2016.
 */
public class UtilityNetworkLevel {


    /**
     * Gets NetworkSignalLevel enum basing on integer value
     */
    public enum NetworkSignalLevel {
        NO_SIGNAL(0),
        POOR(1),
        FAIR(2),
        GOOD(3),
        EXCELLENT(4);

        private final int level;

        NetworkSignalLevel(final int level){
            this.level = level;
        }

        public static int getMaxLevel() {
            return EXCELLENT.level;
        }
    }

    public final int level;
    public final String description;

    public UtilityNetworkLevel(final int level, final String description) {
        this.level = level;
        this.description = description;
    }


    /**
     * Gets WifiSignalLevel enum basing on integer value
     * @param level as an integer
     * @return WifiSignalLevel enum
     */
    public static NetworkSignalLevel fromLevel(final int level) {
        switch (level) {
            case 0:
                return NetworkSignalLevel.NO_SIGNAL;
            case 1:
                return NetworkSignalLevel.POOR;
            case 2:
                return NetworkSignalLevel.FAIR;
            case 3:
                return NetworkSignalLevel.GOOD;
            case 4:
                return NetworkSignalLevel.EXCELLENT;
            default:
                return NetworkSignalLevel.NO_SIGNAL;
        }
    }
}
