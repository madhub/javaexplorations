import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class BytebufferDemo {


    public static void main(String[] args) throws IOException {
                long offset = 0;
                long bytesToRead = 100;
                try(InputStream inputStream = Files.newInputStream(path,StandardOpenOption.READ))
                {
                    inputStream.skip(offset);
                    byte[] bytes = new byte[bytesToRead];
                    inputStream.read(bytes);
                    // wrap with bytes byte buffer - by defaul Java interprests bytes as BigEndian
                    ByteBuffer littleEnOrder = ByteBuffer.wrap(bytes);
                    // print current byte order
                    System.out.println(littleEnOrder.order());
                    // change the byte order
                    littleEnOrder.order(ByteOrder.LITTLE_ENDIAN);
                    // interpret bytes buffer as int buffer
                    IntBuffer intBuffer = littleEnOrder.asIntBuffer();
                     while (intBuffer.hasRemaining()) {
                        System.out.println(intBuffer.get());
                 }

    }


}
