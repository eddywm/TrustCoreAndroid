package trust.core.zcash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import trust.core.util.Hex;

import static trust.core.zcash.ZcashUtil.BLAKE2B;

public class ZcashTransaction {
    public String transactionHash;

    public TransactionInput[] transactionInputs;
    public TransactionOutput[] transactionOutputs;


    // BLAKE2B hash function is used for hashing all transaction inputs and outputs
    // BLAKE2B is the standard hash function for Zcash transactions : https://github.com/zcash/zips/blob/master/zip-0243.rst
    String toHash() throws IOException {
        StringBuilder inputData = new StringBuilder();
        StringBuilder outputData = new StringBuilder();

        for (TransactionInput input : transactionInputs) {
            inputData.append(input.toString());
        }

        for (TransactionOutput output : transactionOutputs) {
            outputData.append(output.toString());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();


        byte[] inputByteArray = inputData.toString().getBytes(StandardCharsets.UTF_8);
        byte[] outputByteArray = outputData.toString().getBytes(StandardCharsets.UTF_8);

        output.write(inputByteArray);
        output.write(outputByteArray);


        return Hex.byteArrayToHexString(BLAKE2B(
                output.toByteArray()
        ));
    }


}
