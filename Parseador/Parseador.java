import java.text.Normalizer;
import java.util.Vector;

public class Parseador {

    private String prs;
    private Stopwords sp;

    public Parseador(String prs, Stopwords sp) {
        this.prs = prs;
        this.sp = sp;
    }


    private boolean esStopWord(String palabra) {
        int numsp = sp.getNumStopwords();
        boolean coincidencia = false;

        for (int i=0; i<numsp && !coincidencia; ++i) {
            if (palabra.equals(this.sp.getStopWord(i))) {
                coincidencia = true;
            }
        }

        return coincidencia;
    }

    public Vector parsear() {
        String[] palabras = prs.replaceAll("[,;?¿¡!.:]", "").split("\\s+");
        Vector palabrasClave = new Vector();

        int nump = palabras.length;

        for (int i=0; i<nump; ++i) {
            if (!esStopWord(palabras[i])) {
                palabras[i] = Normalizer.normalize(palabras[i], Normalizer.Form.NFD);
                palabras[i] = palabras[i].replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                palabrasClave.add(palabras[i].toUpperCase());
            }
        }

        return palabrasClave;
    }
}
