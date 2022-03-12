import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.math.RoundingMode.HALF_DOWN;

public record AntOld(BigDecimal position, Boolean facingRight) {
    static List<AntOld> ants = new ArrayList<>();

    //time in ms
    static BigDecimal time = BigDecimal.valueOf(0.000);
    static String[] output = new String[101];
    static Logger logger = LogManager.getLogger(AntOld.class);

    public static void main(String[] args) throws InterruptedException {

        //initialize ants state
        for (int i = 0; i < 24; i++) {
            ants.add(new AntOld(BigDecimal.valueOf(Math.random()).setScale(3, HALF_DOWN), Math.random() > 0.5));
        }
        initializeOutput();

        while (ants.size() > 0) {
            updateAndDisplayOutput();

            //increment time by 1ms
            time = time.add(BigDecimal.valueOf(0.001));
            Thread.sleep(100);
            updateAnts();
        }
        //time in seconds
        System.out.println(time.multiply(BigDecimal.valueOf(100)) + " seconds");
    }

    private static void updateAndDisplayOutput() {
        initializeOutput();
        for (AntOld ant : ants) {
            BigDecimal antPos = ant.position.setScale(3, HALF_DOWN);
            var pos100 = antPos.multiply(BigDecimal.valueOf(100)).setScale(3, RoundingMode.HALF_UP);
            String s = String.valueOf(pos100);
            int roundPos = Integer.parseInt(s.substring(0, s.indexOf(".")));
            logger.debug("position = " + ant.position + " ROUNDPOS= " + roundPos + " antSize" + ants.size());
            output[roundPos] = ant.facingRight ? ">" : "<";
        }
        for (int i = 0; i < 101; i++) {
            System.out.print(output[i]);
        }
        System.out.println();
    }

    private static void initializeOutput() {
        for (int i = 0; i < 101; i++) {
            output[i] = ".";
        }
    }


    //1m Rod is divided in 100 spaces marked with dot for empty, < for ant going left and > for ant going right
    //for simplicity speed=1 therefore ignored in the update position calculation
    private static void updateAnts() {
        for (AntOld ant : ants) {
            var antPos = ant.position;
            var newPos = ant.facingRight ? antPos.add(time) : antPos.subtract(time);
            if (newPos.compareTo(BigDecimal.ZERO) < 0 || newPos.compareTo(BigDecimal.ONE) > 0) {
                ants.remove(ant);
                //System.out.println("Removed one ant " + ants.size());
            } else {
                //System.out.println("ant updated");
                ants.remove(ant);
                ants.add(new AntOld(newPos, ant.facingRight));
                reverseDirectionIfCollisionOccurs(ant);
            }
            break;
        }
    }

    private static void reverseDirectionIfCollisionOccurs(AntOld a) {
        for (int i = 0; i < ants.size(); i++) {
            boolean samePosition = a.position.equals(ants.get(i).position);
            boolean willIntersectOnNextIncrement = ((a.position.subtract(ants.get(i).position)).equals(BigDecimal.valueOf(0.001))
                    || (a.position.subtract(ants.get(i).position)).equals(BigDecimal.valueOf(-0.001))
                    && (a.facingRight != ants.get(i).facingRight));
            if (samePosition || (willIntersectOnNextIncrement && facingEachOther(a, ants.get(i))) && (!a.equals(ants.get(i)))) {
                ants.remove(a);
                ants.add(new AntOld(a.position.add(BigDecimal.valueOf(0.001)), !a.facingRight));

                ants.remove(i);
                ants.set(i, new AntOld(ants.get(i).position, !ants.get(i).facingRight));
                break;
            }
        }
    }

    private static boolean facingEachOther(AntOld a, AntOld b) {
        return (a.facingRight && !b.facingRight) && (a.position.compareTo(b.position) < 0) ||
                (!a.facingRight && b.facingRight) && (a.position.compareTo(b.position) > 0);
    }

}
