import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;


public class Demo {

    public static void main(String[] args) throws IOException {
        //ServiceLoader.load(SiemensCSANonImage.class);
//        DicomInputStream dis = new DicomInputStream(Files.newInputStream(Path.of("")));
//        Attributes attributes = dis.readDataset(10, 10);
//        attributes.getString("",10);

        //ElementDictionary siemens_csa_header = ElementDictionary.getElementDictionary("GEMS_CT_CARDIAC_001");
        System.out.println(ElementDictionary.keywordOf(0x00491001,"GEMS_CT_CARDIAC_001"));
        System.out.println(ElementDictionary.keywordOf(0x00290008,"SIEMENS CSA NON-IMAGE"));
        //System.out.println(siemens_csa_header.keywordOf(SiemensCSANonImage.CSADataVersion));

    }
}
