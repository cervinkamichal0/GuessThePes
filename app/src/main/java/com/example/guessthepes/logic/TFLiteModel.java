/**
 *  Třže TFLiteModel - spravuje funkce datového modelu, který se používá pro vyhodnocení plemen
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Filip Dvořák
 *@created    leden 2024
 */
package com.example.guessthepes.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.guessthepes.ml.Dogs;
import com.example.guessthepes.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TFLiteModel {
    Context context;
    final int imageSize=224;

    public TFLiteModel(Context context) {
        this.context = context;
    }

    /**
     * Převod bitmapu na byteBuffer pro tflite model
     *
     *param  bitmap obrázku
     *@return byteBuffer
     */
    public ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {

        int inputSize = 224;
        int numBytesPerChannel = 4; // Float.SIZE / Byte.SIZE
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * numBytesPerChannel);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[inputSize * inputSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int value = intValues[pixel++];
                byteBuffer.putFloat((float) ((value >> 16) & 0xFF) / 255.0f);
                byteBuffer.putFloat((float) ((value >> 8) & 0xFF) / 255.0f);
                byteBuffer.putFloat((float) (value & 0xFF) / 255.0f);
            }
        }

        return byteBuffer;
    }

    /**
     * Metoda pro zjištění plemene z obrázku
     *
     *param  bitmap obrázku
     *@return String s plemenem
     */
    public String classifyImage(Bitmap image)
    {
        try {
            Model model = Model.newInstance(context); //template

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32); //template

            ByteBuffer inputBuffer = convertBitmapToByteBuffer(image);
            inputBuffer.order(ByteOrder.nativeOrder());

            inputFeature0.loadBuffer(inputBuffer); //template

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0); //template
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer(); //template

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"chihuahua", "japanese spaniel", "maltese dog", "pekinese", "shih tzu", "blenheim spaniel", "papillon", "toy terrier", "rhodesian ridgeback", "afghan hound", "basset", "beagle", "bloodhound", "bluetick", "black and tan coonhound", "walker hound", "english foxhound", "redbone", "borzoi", "irish wolfhound", "italian greyhound", "whippet", "ibizan hound", "norwegian elkhound", "otterhound", "saluki", "scottish deerhound", "weimaraner", "staffordshire bullterrier", "american staffordshire terrier", "bedlington terrier", "border terrier", "kerry blue terrier", "irish terrier", "norfolk terrier", "norwich terrier", "yorkshire terrier", "wire haired fox terrier", "lakeland terrier", "sealyham terrier", "airedale", "cairn", "australian terrier", "dandie dinmont", "boston bull", "miniature schnauzer", "giant schnauzer", "standard schnauzer", "scotch terrier", "tibetan terrier", "silky terrier", "soft coated wheaten terrier", "west highland white terrier", "lhasa", "flat coated retriever", "curly coated retriever", "golden retriever", "labrador retriever", "chesapeake bay retriever", "german short haired pointer", "vizsla", "english setter", "irish setter", "gordon setter", "brittany spaniel", "clumber", "english springer", "welsh springer spaniel", "cocker spaniel", "sussex spaniel", "irish water spaniel", "kuvasz", "schipperke", "groenendael", "malinois", "briard", "kelpie", "komondor", "old english sheepdog", "shetland sheepdog", "collie", "border collie", "bouvier des flandres", "rottweiler", "german shepherd", "doberman", "miniature pinscher", "greater swiss mountain dog", "bernese mountain dog", "appenzeller", "entlebucher", "boxer", "bull mastiff", "tibetan mastiff", "french bulldog", "great dane", "saint bernard", "eskimo dog", "malamute", "siberian husky", "affenpinscher", "basenji", "pug", "leonberg", "newfoundland", "great pyrenees", "samoyed", "pomeranian", "chow", "keeshond", "brabancon griffon", "pembroke", "cardigan", "toy poodle", "miniature poodle", "standard poodle", "mexican hairless", "dingo", "dhole", "african hunting dog"};
            String[] classesPreklad = {"čivava", "japonský chin", "maltézský psík", "pekingský palácový psík", "shih-tzu", "King Charles španěl", "papillon", "anglický toy teriér", "rhodéský ridgeback", "afghánský chrt", "baset", "bígl", "bloodhound", "modrý coonhound", "black and tan coonhound", "walker hound", "anglický foxhound", "redbone", "barzoj", "irský vlkodav", "italský chrtík", "whippet", "ibizský podenco", "Norský losí pes šedý", "otterhound", "saluki", "skotský jelení pes", "výmarský ohař", "stafordšírský bulteriér", "americký stafordšírský teriér", "bedlington teriér", "border teriér", "kerry blue teriér", "irský teriér", "norfolský teriér", "norwich terrier", "jorkšírský teriér", "foxteriér drsnosrstý", "lakeland teriér", "sealyham teriér", "erdelteriér", "cairn", "australský teriér", "dandie dinmont teriér", "bostonský teriér", "knírač malý", "knírač velký", "knírač střední", "skotský teriér", "tibetský teriér", "silky teriér", "soft coated wheaten teriér", "west highland white teriér", "lhasa", "hladkosrstý retrívr", "kudrnatý retrívr", "zlatý retrívr", "labrador retrívr", "chesapeake bay retrívr", "německý ohař krátkosrstý", "maďarský ohař krátkosrstý", "anglický setr", "irský setr", "gordonsetr", "bretaňský ohař", "clumber španěl", "anglický špringeršpaněl", "velššpringršpaněl", "kokršpaněl", "sussex španěl", "irský vodní španěl", "kuvasz", "šiperka", "groenendael", "malinois", "briard", "australská kelpie", "komondor", "bobtail", "šeltie", "kolie", "border kolie", "flanderský bouvier", "rotvajler", "německý ovčák", "dobrman", "trpasličí pinč", "velký švýcarský salašnický pes", "bernský salašnický pes", "appenzellský salašnický pes", "entlebuchský salašnický pes", "boxer", "bullmastif", "tibetský mastif", "francouzský buldoček", "německá doga", "bernardýn", "eskymácký pes", "malamut", "sibiřský husky", "opičí pinč", "basenji", "mops", "leonberger", "novofundlandský pes", "pyrenejský horský pes", "samojed", "pomeranian", "Čau-čau", "vlčí špic", "bruselský grifonek", "corgi pembroke", "corgi cardigan", "toy pudl", "trpasličí pudl", "pudl velký", "mexický naháč", "dingo", "dhoul", "pes hyenový"};

            // Releases model resources if no longer used.
            model.close(); //template
            return classesPreklad[maxPos];
        } catch (IOException e) {
            // TODO Handle the exception //template
        }
        return null;
    }
}
