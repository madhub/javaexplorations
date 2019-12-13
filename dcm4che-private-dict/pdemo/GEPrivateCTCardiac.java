package pdemo;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.VR;

public class GEPrivateCTCardiac extends ElementDictionary {
    public static final String PrivateCreator = "GEMS_CT_CARDIAC_001";

    /**  VR=SQ VM=1 */
    public static final int CTCardiacSequence= 0x00490001;
    /**  VR=CS VM=1 */
    public static final int HeartRateAtConfirm = 0x00490002;
    /**  VR=FL VM=1 */
    public static final int AvgHeartRatePriorToConfirm      = 0x00490003;

    public GEPrivateCTCardiac() {
        super(PrivateCreator, GEPrivateCTCardiac.class);
    }

    @Override
    public VR vrOf(int tag) {
        switch (tag & 0xFFFF00FF) {
            case CTCardiacSequence:
                return VR.SQ;
            case HeartRateAtConfirm:
                return VR.CS;
            case AvgHeartRatePriorToConfirm:
                return VR.FL;
        }
        return null;
    }

    @Override
    public String keywordOf(int tag) {
        switch (tag & 0xFFFF00FF) {
            case CTCardiacSequence:
                return "CTCardiacSequence";
            case HeartRateAtConfirm:
                return "HeartRateAtConfirm";
            case AvgHeartRatePriorToConfirm:
                return "AvgHeartRatePriorToConfirm";
        }
        return null;
    }
}
